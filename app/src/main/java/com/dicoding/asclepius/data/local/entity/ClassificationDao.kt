package com.dicoding.asclepius.data.local.entity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ClassificationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(classification: ClassificationEntity)

    @Delete
    fun delete(classification: ClassificationEntity)

    @Query("SELECT * FROM classificationentity ORDER BY id ASC")
    fun getAllClassification(): LiveData<List<ClassificationEntity>>
}