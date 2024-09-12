package com.dicoding.asclepius.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "classification")
@Parcelize
data class Classification(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "image_uri") val imageUri: String,
    @ColumnInfo(name = "label") val label: String,
    @ColumnInfo(name = "score") val score: Float,
): Parcelable
