package com.example.marcellinapizzas

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
    val name: String,
    var value: Int
) : Parcelable