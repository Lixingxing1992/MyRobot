package com.myrobot.org.base

import java.io.UnsupportedEncodingException

/**
 * @author Lixingxing
 */
class Test {

    fun getStringByte() {
        val parms = "1234"
        try {
            val bytes = parms.toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

    }
}
