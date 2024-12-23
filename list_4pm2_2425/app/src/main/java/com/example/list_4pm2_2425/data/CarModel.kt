package com.example.list_4pm2_2425.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.UUID

@Entity(tableName = "CarModels",
    indices = [Index("id"), Index("carModel_name")],
)
data class CarModel(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @SerializedName("carModel_name") @ColumnInfo(name = "carModel_name") var name: String = "",
)
