package com.example.list_4pm2_2425.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.list_4pm2_2425.data.CarModel
import com.example.list_4pm2_2425.data.Catalog
import com.example.list_4pm2_2425.data.Sparepart
import com.example.list_4pm2_2425.data.User

@Database(
    entities = [CarModel::class, Catalog::class, Sparepart::class, User::class],
    version = 10,
    exportSchema = false
)
@TypeConverters(ListTypeConverters::class)
abstract class ListDatabase : RoomDatabase() {
    abstract fun listDAO(): ListDAO

    companion object {
        @Volatile
        private var INSTANCE: ListDatabase? = null

        fun getDatabase(context: Context): ListDatabase {
            return INSTANCE ?: synchronized(this) {
                buildDatabase(context).also { INSTANCE = it }
            }
        }



        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            ListDatabase::class.java,
            "list_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
