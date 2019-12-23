package com.myrobot.org.util

import com.myrobot.org.server.redis.RedisUtils

/**
 * @author Lixingxing
 */
object SpeechUtils {

    @Synchronized
    fun speech(text: String) {
        RedisUtils.publishTopic(
            "usr:voice:synthesis",
            JsonUtils.getJson("text" to text , "priority" to 0, "bullet" to false))
    }
}
