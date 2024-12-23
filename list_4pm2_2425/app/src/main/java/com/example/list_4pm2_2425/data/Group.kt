package com.example.list_4pm2_2425.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.UUID

@Entity(tableName = "groups",
    indices = [Index("id"), Index("carModel_id")],
)
data class Group(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @SerializedName("group_name") @ColumnInfo(name = "group_name") var name: String = "",
    @SerializedName("carModel_id") @ColumnInfo(name = "carModel_id") var facultyID: UUID?= null
)