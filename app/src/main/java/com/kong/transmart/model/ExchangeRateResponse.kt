package com.kong.transmart.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

data class ExchangeRateResponse(
    val result: Int,
    val cur_unit: String,
    val cur_nm: String,
    val ttb: String,
    val tts: String,
    val deal_bas_r: String,
    val bkpr: String,
    val yy_efee_r: String,
    val ten_dd_efee_r: String,
    val kftc_deal_bas_r: String,
    val kftc_bkpr: String
)

@Entity(tableName = "exchange-rate-table")
data class ExchangeRateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "exchange-rate-rate")
    val rate: Double,
    @ColumnInfo(name = "exchange-rate-date")
    val date: Date
)

