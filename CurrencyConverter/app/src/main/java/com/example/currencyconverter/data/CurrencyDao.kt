package com.example.currencyconverter.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import androidx.room.Query
import com.example.currencyconverter.data.entity.ConversionResultEntity
import com.example.currencyconverter.data.entity.CurrencyFlagEntity
import com.example.currencyconverter.data.entity.HistoricalDataEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CurrencyDao {

    @Insert(entity = CurrencyFlagEntity::class)
    suspend fun insertCurrencySymbol(currencySymbol: List<CurrencyFlagEntity>)

    @Query("SELECT * FROM currencyFlag")
    fun getListOfCurrencySymbol(): Flow<List<CurrencyFlagEntity>>

    @Query("SELECT * FROM currencyFlag")
    suspend fun getSomething():List<CurrencyFlagEntity>

    @Insert(entity = ConversionResultEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversionResult(conversionResult: ConversionResultEntity)

    @Query("SELECT * FROM conversionResult")
    fun getConversionResult(): Flow<List<ConversionResultEntity>>

    @Query("SELECT * FROM historicalData ORDER BY  date DESC LIMIT 1")
    fun getHistoricalDataSample(): Flow<HistoricalDataEntity?>

    @Insert(entity = HistoricalDataEntity::class, onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    suspend fun insertHistoricalData(data: HistoricalDataEntity)

    @Query("DELETE FROM historicalData")
    suspend fun deleteHistoricalData()

}