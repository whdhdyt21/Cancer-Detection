package com.dicoding.asclepius.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class ClassificationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "confidence")
    val confidence: String,

    @ColumnInfo(name = "imageUri")
    val imageUri: String
) : Parcelable
