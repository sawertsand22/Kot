package com.example.list_4pm2_2425.data


import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

@Entity(tableName = "students",
    indices = [Index("id"), Index("group_id")])

data class Student(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @SerializedName("last_name") var lastName: String = "",
    @SerializedName("first_name") var firstName: String = "",
    @SerializedName("middle_name") var middleName: String = "",
    @SerializedName("VIN") var VIN: String = "",
    @SerializedName("quantity") var quantity: String = "",
    @SerializedName("count_part") var count_part: String = "",
    @SerializedName("production_date") @ColumnInfo(name = "production_date_date") var birthDate: Date = Date(),
    @SerializedName("group_id") @ColumnInfo(name = "group_id") var groupID: UUID?= null,
    //var phone: String="",
    var sex : Int=0
){
    val shortName
        get() = lastName+
                (if(firstName.isNotBlank()){" ${firstName.subSequence(0,1)}."} else "") +
                (if(middleName.isNotBlank()){" ${middleName.subSequence(0,1)}."} else "")
    val longName
        get() = lastName+
                (if(firstName.isNotBlank()){" ${firstName}"} else "") +
                (if(middleName.isNotBlank()){" ${middleName}"} else "")
}


