package com.example.list_4pm2_2425.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.list_4pm2_2425.database.ListTypeConverters
import com.example.list_4pm2_2425.data.Faculty
import com.example.list_4pm2_2425.data.Group
import com.example.list_4pm2_2425.data.Student


@Database(
    entities = [Faculty::class, Group::class, Student::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(ListTypeConverters::class)

abstract class ListDatabase: RoomDatabase() {
    abstract fun listDAO(): ListDAO

    companion object{
        @Volatile
        private var INSTANCE: ListDatabase? = null

        fun getDatabase(context: Context): ListDatabase{
            return INSTANCE ?: synchronized(this){
                buildDatabase(context).also{ INSTANCE=it}
            }
        }

//    val MIGRAION_2_3 = object : Migration(2,3){
//        override fun migrate(db: SupportSQLiteDatabase) {
//            TODO("Not yet implemented")
//        }
//    }

    private fun buildDatabase(context: Context) = Room.databaseBuilder(
        context,
        ListDatabase::class.java,
        "list_database")
        .fallbackToDestructiveMigration()
//        .addMigrations(MIGRATION_2_3)
        .build()

    }
}