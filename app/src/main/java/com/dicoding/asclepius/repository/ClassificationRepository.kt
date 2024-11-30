package com.dicoding.asclepius.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.local.entity.ClassificationDao
import com.dicoding.asclepius.data.local.entity.ClassificationEntity
import com.dicoding.asclepius.data.local.room.ClassificationRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ClassificationRepository(application: Application) {
    private val mClassifications: ClassificationDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = ClassificationRoomDatabase.getDatabase(application)
        mClassifications = db.classificationDao()
    }

    fun getAllClassification(): LiveData<List<ClassificationEntity>> =
        mClassifications.getAllClassification()

    fun insert(classifications: ClassificationEntity) {
        executorService.execute { mClassifications.insert(classifications) }
    }

    fun delete(classifications: ClassificationEntity) {
        executorService.execute { mClassifications.delete(classifications) }
    }
}