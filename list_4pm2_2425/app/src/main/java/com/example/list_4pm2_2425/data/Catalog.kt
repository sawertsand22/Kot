package com.example.list_4pm2_2425.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.UUID

@Entity(tableName = "catalog",
    indices = [Index("id"), Index("carModel_id")],
)
data class Catalog(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @SerializedName("catalog_name") @ColumnInfo(name = "catalog_name") var name: String = "",
    @SerializedName("carModel_id") @ColumnInfo(name = "carModel_id") var carModelID: UUID?= null
)