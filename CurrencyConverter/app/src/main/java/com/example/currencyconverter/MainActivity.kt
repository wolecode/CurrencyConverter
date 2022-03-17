package com.example.currencyconverter

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.currencyconverter.databinding.ActivityMainBinding
import com.example.currencyconverter.network.Results
import com.example.currencyconverter.network.RetrofitObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Error
import kotlin.coroutines.coroutineContext

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var list: List<String>
    lateinit var viewModel: ConverterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = getCurrencyFlag()
        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory(application))[ConverterViewModel::class.java]

        setUpView()
        setCurrencySymbol()

    }
    private fun setUpView() {
        binding.convertButton.setOnClickListener {
            convert()
        }
        val firstSpinner = binding.spinnerLayout.firstSpinner
        val secondSpinner = binding.spinnerLayout.secondSpinner

        ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
            list).apply {
            setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            firstSpinner.adapter = this
            secondSpinner.adapter = this
        }

        firstSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val item = p0?.getItemAtPosition(p2) as String
                viewModel.updateFirstCurrency(item.substring(4))
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        secondSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val item = p0?.getItemAtPosition(p2) as String
                viewModel.updateSecondCurrency(item.substring(4))
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun setCurrencySymbol() {
        viewModel.firstCurrency.observe(this) {
            binding.firstCurrencySymbol.text = it
            println("Observe $it")
        }
        viewModel.secondCurrency.observe(this) {
            binding.secondCurrencySymbol.text = it
        }
    }

    private fun convert() {
        observeConversionResult()
        val firstValue = binding.firstCurrencyEditText.text.toString()
        val secondValue = binding.secondCurrencyEditText.text.toString()

        if (firstValue == "." || secondValue == ".") {
             Toast.makeText(this, "Wrong Input", Toast.LENGTH_LONG).show()
         } 

        viewModel.convertCurrency(56.0f,"NGN","EUR",)
    }
    private fun observeConversionResult() {

        viewModel.conversion.observe(this) {
            when(it) {
                is Results.Success -> binding.conversionProgress.visibility = View.INVISIBLE
                is Error -> {
                    binding.conversionProgress.visibility = View.INVISIBLE
                    Toast.makeText(this, it.message?: "Error", Toast.LENGTH_LONG).show()
                }
                else -> binding.conversionProgress.visibility = View.VISIBLE
            }
        }
    }
}