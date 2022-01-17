package com.example.domain.database.basket

import androidx.room.*
import com.example.domain.convertorBig.BigIntegerConverter
import java.math.BigInteger

@Entity(tableName = "basket")
class Basket{
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Long? = null
    @ColumnInfo(name = "name")
    var name: String? = null
    @ColumnInfo(name = "bar_code")
    var barCode: String? = null
    @ColumnInfo(name = "classCode")
    var classCode: String? = null
    @ColumnInfo(name = "label")
    var label: String? = null
    @TypeConverters(BigIntegerConverter::class)
    @ColumnInfo(name = "price")
    var price: BigInteger?=null

    @TypeConverters(BigIntegerConverter::class)
    @ColumnInfo(name = "discount")
    var discount: BigInteger? = null

    @TypeConverters(BigIntegerConverter::class)
    @ColumnInfo(name = "vat")
    var vat: BigInteger? = null
    @ColumnInfo(name = "groupId")
    var groupId: Double? = null

    @ColumnInfo(name = "measure_name")
    var measure_name: String? = null

    @TypeConverters(BigIntegerConverter::class)
    @ColumnInfo(name = "count")
    var count: BigInteger ?=null

    @ColumnInfo(name = "cat_title")
    var cat_title: String? = null

    @ColumnInfo(name = "photo")
    var photo: String? = null

    @TypeConverters(BigIntegerConverter::class)
    @ColumnInfo(name = "persent_value")
    var perValue: BigInteger = BigInteger.valueOf(0)

    @ColumnInfo(name = "update_count")
    var count_up:Int =0

    @ColumnInfo(name = "measure_id")
    var measure_id: Long? = null

    @ColumnInfo(name = "commission_tin")
    var commissionTin: String? = null



    override fun toString(): String {
        return "Basket(id=$id, name=$name, barCode=$barCode, classCode=$classCode, label=$label, price=$price, discount=$discount, vat=$vat, groupId=$groupId, measure_name=$measure_name, count=$count, cat_title=$cat_title, photo=$photo, perValue=$perValue, count_up=$count_up, measure_id=$measure_id, commissionTin=$commissionTin)"
    }


}