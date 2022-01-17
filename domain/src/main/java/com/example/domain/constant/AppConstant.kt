package com.example.domain.constant

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object AppConstant {
    const val TAG = "MyBuxPos"
    const val SHARED_PREFERENCES = "SharedPreferences"
    const val LOCK_SCREEN = "LockScreen"
    const val LOCK_SCREEN_INTERVAL = "LockScreenInterval"
    const val APP_STOP_TIME = "AppStopTime"
    const val DATABASE_NAME = "cash_database.db"
    const val BASE_URL = "https://pos.mybux.uz/"
    const val BASE_URL_OFD = "http://192.168.0.117:3448/"
    const val APIKEY = "be9011ce17d3b497025ccaa0d5f04d21"
    const val CONNECT_TIMEOUT: Byte = 120
    const val WRITE_TIMEOUT: Byte = 120
    const val READ_TIMEOUT: Byte = 120

    fun getCurrentTimeStamp(): String? {
        @SuppressLint("SimpleDateFormat") val sdfDate =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss") //dd/MM/yyyy
        val now = Date()
        return sdfDate.format(now)
    }

    fun getOrderId(): String? {
        @SuppressLint("SimpleDateFormat") val sdfDate = SimpleDateFormat("yyyyMMdd") //dd/MM/yyyy
        val now = Date()
        return sdfDate.format(now)
    }

    fun getCurrentDate(): String? {
        @SuppressLint("SimpleDateFormat") val sdfDate =
            SimpleDateFormat("dd.MM.yyyy HH:mm") //dd/MM/yyyy
        val now = Date()
        return sdfDate.format(now)
    }

    fun getApduTime(): String? {
        @SuppressLint("SimpleDateFormat") val sdfDate =
            SimpleDateFormat("yyyyMMdd'T'HHmmss") //dd/MM/yyyy
        val now = Date()
        return sdfDate.format(now)
    }

    fun getApduTimeMy(): String? {
        @SuppressLint("SimpleDateFormat") val sdfDate =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss") //dd/MM/yyyy
        val now = Date()
        return sdfDate.format(now)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDate(tokenize: String?): String? {
        val inFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val date = LocalDate.parse(tokenize, inFormatter)
        val outFormatter = DateTimeFormatter.ofPattern("yyyy MM dd HH:mm")
        return outFormatter.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDateMy(tokenize: String?): String? {
        val inFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val date = LocalDate.parse(tokenize, inFormatter)
        val outFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")
        return outFormatter.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDateYY(tokenize: String?): String? {
        val inFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date = LocalDateTime.parse(tokenize, inFormatter)
        val outFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        return outFormatter.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDateYD(tokenize: String?): String? {
        val inFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date = LocalDateTime.parse(tokenize, inFormatter)
        val outFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")
        return outFormatter.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDateDD(tokenize: String?): String? {
        val inFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")
        val date = LocalDateTime.parse(tokenize, inFormatter)
        val outFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        return outFormatter.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDateDDay(tokenize: String?): String? {
        val inFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val date = LocalDateTime.parse(tokenize, inFormatter)
        val outFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return outFormatter.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDateDDayT(tokenize: String?): String? {
        val inFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date = LocalDateTime.parse(tokenize, inFormatter)
        val outFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")
        return outFormatter.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDateDDayReal(tokenize: String?): String? {
        val inFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date = LocalDateTime.parse(tokenize, inFormatter)
        val outFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")
        return outFormatter.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDateDDayMy(tokenize: String?): String? {
        val inFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")
        val date = LocalDateTime.parse(tokenize, inFormatter)
        val outFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return outFormatter.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDateTime(tokenize: String?, `in`: String?, out: String?): String? {
        //"yyyy-MM-dd HH:mm:ss"
        val inFormatter = DateTimeFormatter.ofPattern(`in`)
        val date = LocalDateTime.parse(tokenize, inFormatter)
        //"yyyyMMddHHmmss"
        val outFormatter = DateTimeFormatter.ofPattern(out)
        return outFormatter.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDateList(tokenize: String?): String? {
        val inFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val date = LocalDate.parse(tokenize, inFormatter)
        val outFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        return outFormatter.format(date)
    }

    fun getColSpaces(i: Int, i1: Int, append: String?): String? {
        val SpaceNumber = (384 - i) / i1
        val spaceString = StringBuilder()
        for (j in 0 until SpaceNumber) {
            spaceString.append(append)
        }
        return spaceString.toString()
    }
}