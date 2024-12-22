package com.example.list_4pm2_2425.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.UUID

@Entity(tableName = "faculties",
    indices = [Index("id"), Index("faculty_name")],
)
data class Faculty(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @SerializedName("faculty_name") @ColumnInfo(name = "faculty_name") var name: String = "",
)
