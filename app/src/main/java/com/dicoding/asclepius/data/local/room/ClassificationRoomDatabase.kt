package com.dicoding.asclepius.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.local.entity.ClassificationDao
import com.dicoding.asclepius.data.local.entity.ClassificationEntity

@Database(entities = [ClassificationEntity::class], version = 1, exportSchema = false)
abstract class ClassificationRoomDatabase : RoomDatabase() {
    abstract fun classificationDao(): ClassificationDao

    companion object {
        @Volatile
        private var INSTANCE: ClassificationRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ClassificationRoomDatabase {
            if (INSTANCE == null) {
                synchronized(ClassificationRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ClassificationRoomDatabase::class.java,
                        "classification_database"
                    ).build()
                }
            }
            return INSTANCE as ClassificationRoomDatabase
        }
    }
}