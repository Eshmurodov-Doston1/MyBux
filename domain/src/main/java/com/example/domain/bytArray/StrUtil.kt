package com.example.domain.bytArray

import kotlin.experimental.or

object StrUtil {
    fun toByteArray(hexString: String): ByteArray {
        val hexStringLength = hexString.length
        var byteArray: ByteArray? = null
        var count = 0
        var c: Char
        var i: Int

        // Count number of hex characters
        i = 0
        while (i < hexStringLength) {
            c = hexString[i]
            if (c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a' && c <= 'f') {
                count++
            }
            i++
        }
        byteArray = ByteArray((count + 1) / 2)
        var first = true
        var len = 0
        var value: Int
        i = 0
        while (i < hexStringLength) {
            c = hexString[i]
            value = if (c >= '0' && c <= '9') {
                c - '0'
            } else if (c >= 'A' && c <= 'F') {
                c - 'A' + 10
            } else if (c >= 'a' && c <= 'f') {
                c - 'a' + 10
            } else {
                -1
            }
            if (value >= 0) {
                if (first) {
                    byteArray[len] = (value shl 4).toByte()
                } else {
                    byteArray[len] = byteArray[len] or value.toByte()
                    len++
                }
                first = !first
            }
            i++
        }
        return byteArray
    }
}