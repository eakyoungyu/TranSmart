package com.kong.transmart.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bank-table")
data class Bank (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name="bank-name")
    val name: String = "",
    @ColumnInfo(name="bank-fee")
    var fee: Double = 0.0,
    @ColumnInfo(name="bank-rate")
    var exchangeRate: Double = 0.0,
    @ColumnInfo(name="bank-removable")
    val removable: Boolean = true
)