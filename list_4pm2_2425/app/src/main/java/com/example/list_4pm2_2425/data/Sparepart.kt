package com.example.list_4pm2_2425.data


import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

@Entity(tableName = "spareparts",
    indices = [Index("id"), Index("catalog_id")])

data class Sparepart(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @SerializedName("number_catalog") var numberCatalog: String = "",
    @SerializedName("spare_part_name") var sparePartName: String = "",
    @SerializedName("manufacturer") var manufacturer: String = "",
    @SerializedName("VIN") var VIN: String = "",
    @SerializedName("quantity") var quantity: String = "",
    @SerializedName("count_part") var count_part: String = "",
    @SerializedName("production_date") @ColumnInfo(name = "production_date_date") var birthDate: Date = Date(),
    @SerializedName("catalog_id") @ColumnInfo(name = "catalog_id") var catalogID: UUID?= null,
    //var phone: String="",
    var sex : Int=0
){
    val shortName
        get() = numberCatalog+
                (if(sparePartName.isNotBlank()){" ${sparePartName.subSequence(0,1)}."} else "") +
                (if(manufacturer.isNotBlank()){" ${manufacturer.subSequence(0,1)}."} else "")
    val longName
        get() = numberCatalog+
                (if(sparePartName.isNotBlank()){" ${sparePartName}"} else "") +
                (if(manufacturer.isNotBlank()){" ${manufacturer}"} else "")

    fun copy(
        id: UUID = this.id,
        firstName: String = this.sparePartName,
        middleName: String = this.manufacturer,
        lastName: String = this.numberCatalog,
        // ... копируем все поля
    ): Sparepart {
        return Sparepart(id, firstName, middleName, lastName) // Создаем НОВЫЙ объект
    }
}


