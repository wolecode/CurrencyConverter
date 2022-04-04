package com.example.currencyconverter.ui

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.currencyconverter.databinding.ActivityMainBinding
import com.example.currencyconverter.getCurrencyFlag
import com.example.currencyconverter.network.Results
import java.lang.Error

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var list: List<String>
    lateinit var viewModel: ConverterViewModel
    lateinit var sharedPreference: SharedPreferences
    lateinit var spinnerAdapter: ArrayAdapter<String>
    var target: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[ConverterViewModel::class.java]
        spinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item).apply {
            setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        }

        setupSpinnerData()
        setCurrencySymbol()
        setUpView()
        observePersistedData()

    }

     private fun setupSpinnerData(first: Int? = null, second: Int? = null) {
        viewModel.currencySymbol.observe(this) {
            spinnerAdapter.addAll(it.map { a -> a.flagSymbol + " " + a.currency })
            spinnerAdapter.notifyDataSetChanged()
            if (first != null && second != null) {
                binding.spinnerLayout.firstSpinner.setSelection(first)
                binding.spinnerLayout.secondSpinner.setSelection(second)
            }
        }
    }

    private fun setUpView() {
        with(binding) {
            convertButton.setOnClickListener {
                convert()
            }
            spinnerLayout.firstSpinner.apply {
                adapter = spinnerAdapter
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        val item = p0?.getItemAtPosition(p2) as String
                        viewModel.updateFirstCurrency(item.substring(item.length - 3))
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
            spinnerLayout.secondSpinner.apply {
                adapter = spinnerAdapter
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        p1: View?,
                        p2: Int,
                        p3: Long
                    ) {
                        val item = p0?.getItemAtPosition(p2) as String
                        viewModel.updateSecondCurrency(item.substring(item.length - 3))
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
        }
    }

    private fun setCurrencySymbol() {
        viewModel.firstCurrency.observe(this) {
            binding.firstCurrencySymbol.text = it
        }
        viewModel.secondCurrency.observe(this) {
            binding.secondCurrencySymbol.text = it
        }
    }

    private fun convert() {
        //binding.conversionProgress.visibility = View.VISIBLE
        observeConversionResult()
        val firstValue = binding.firstCurrencyEditText.text.toString()
        val secondValue = binding.secondCurrencyEditText.text.toString()

        if (firstValue == "." || secondValue == ".") {
            Toast.makeText(this, "Wrong Input", Toast.LENGTH_LONG).show()
            binding.conversionProgress.visibility = View.INVISIBLE
        } else if (firstValue.isEmpty() && secondValue.isEmpty()) {
            Toast.makeText(this, "Enter a value", Toast.LENGTH_LONG).show()
            binding.conversionProgress.visibility = View.INVISIBLE
        } else if (firstValue.isNotEmpty() && secondValue.isEmpty()) {
            viewModel.convertCurrency(
                firstValue.toFloat(),
                binding.firstCurrencySymbol.text.toString(),
                binding.secondCurrencySymbol.text.toString(),
                getFirstCurrencyPosition(),
                getSecondCurrencyPosition()
            )
            target = "secondValue"
        } else if (firstValue.isEmpty() && secondValue.isNotEmpty()) {
            viewModel.convertCurrency(
                secondValue.toFloat(),
                binding.secondCurrencySymbol.text.toString(),
                binding.firstCurrencySymbol.text.toString(),
                getSecondCurrencyPosition(),
                getFirstCurrencyPosition()
            )
            target = "firstValue"
        } else {
            viewModel.convertCurrency(
                firstValue.toFloat(),
                binding.firstCurrencySymbol.text.toString(),
                binding.secondCurrencySymbol.text.toString(),
                getFirstCurrencyPosition(),
                getSecondCurrencyPosition()
            )
            target = "secondValue"
        }

    }

    private fun getFirstCurrencyPosition(): String {
        val spinnerItem = binding.spinnerLayout.firstSpinner.selectedItem as String
        return spinnerAdapter.getPosition(spinnerItem).toString()
    }

    private fun getSecondCurrencyPosition(): String {
        val spinnerItem = binding.spinnerLayout.secondSpinner.selectedItem as String
        return spinnerAdapter.getPosition(spinnerItem).toString()
    }

    private fun observeConversionResult() {

        viewModel.conversion.observe(this) {
            when (it) {
                is Results.Success -> {
                    binding.conversionProgress.visibility = View.INVISIBLE
                    if (target == "firstValue") {
                        binding.firstCurrencyEditText.text?.clear()
                        binding.firstCurrencyEditText.clearFocus()
                        binding.firstCurrencyEditText.setText(it.data.toString())
                    } else {
                        binding.secondCurrencyEditText.text?.clear()
                        binding.secondCurrencyEditText.clearFocus()
                        binding.secondCurrencyEditText.setText(it.data.toString())

                    }
                }
                is Results.Error -> {
                    binding.conversionProgress.visibility = View.INVISIBLE
                    Toast.makeText(this, it.error.message ?: "Error", Toast.LENGTH_LONG).show()
                }
                is Results.Loading -> binding.conversionProgress.visibility = View.VISIBLE
            }
        }
    }

    private fun observePersistedData() {
        viewModel.conversionResult.observe(this) {
            if (it.isNotEmpty()) {
                with(binding) {
                    firstCurrencyEditText.setText(it[0].amount.toString())
                    secondCurrencyEditText.setText(it[0].result.toString())
                    //binding.spinnerLayout.firstSpinner.setSelection(it[0].baseCurrency.toInt(), true)
                    //binding.spinnerLayout.secondSpinner.setSelection(it[0].targetCurrency.toInt(), true)
                    setupSpinnerData(it[0].baseCurrency.toInt(), it[0].targetCurrency.toInt() )
                }
            }
        }
    }
}