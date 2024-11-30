package com.dicoding.asclepius.data.local.entity

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClassificationResult(
    val category: String,
    val confidence: String,
    val imageUri: Uri
): Parcelable
