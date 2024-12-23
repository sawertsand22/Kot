package com.example.list_4pm2_2425.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.list_4pm2_2425.database.ListTypeConverters
import com.example.list_4pm2_2425.data.CarModel
import com.example.list_4pm2_2425.data.Group
import com.example.list_4pm2_2425.data.Student

@Database(
    entities = [CarModel::class, Group::class, Student::class],
    version = 5,
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

        // Исправлено название переменной: MIGRAION -> MIGRATION
//        val MIGRATION_2_3 = object : Migration(1, 3) {
//            override fun migrate(db: SupportSQLiteDatabase) {
//                // Создаём новую таблицу
//                db.execSQL("""
//                    CREATE TABLE CarModel (
//                        id INTEGER PRIMARY KEY NOT NULL,
//                        carModel TEXT NOT NULL
//                    )
//                """.trimIndent())
//
//                // Переносим данные из старой таблицы
//
//
//                // Удаляем старую таблицу
//
//            }
//        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            ListDatabase::class.java,
            "list_database"
        )
            .fallbackToDestructiveMigration()

            //.addMigrations(MIGRATION_2_3) // Теперь название правильно
            .build()
    }
}
