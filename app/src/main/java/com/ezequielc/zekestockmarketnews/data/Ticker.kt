package com.ezequielc.zekestockmarketnews.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ticker(
    val symbol: String,
    val name: String,
    val exchange: String
) : Parcelable
