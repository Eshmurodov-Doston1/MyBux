package com.example.domain.convertorBig

import androidx.room.TypeConverter
import java.math.BigInteger

object BigIntegerConverter {
    @TypeConverter
    fun toBigInteger(integer: String?): BigInteger{
        return BigInteger(integer)
    }

    @TypeConverter
    fun toStrings(bigInteger: BigInteger): String {
        return bigInteger.toString()
    }
}