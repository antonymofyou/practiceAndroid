package com.example.practiceandroid.utils

import java.util.concurrent.TimeUnit

val mTime = "00:02:12"
val parser = MyLocalTime.parse(mTime)
val milliseconds = parser.toMillis()

class MyLocalTime(val hour: Int, val minute: Int, val second: Int) {
    companion object {
        fun parse(time: String): MyLocalTime {
            val parts = time.split(":")
            val hour = parts[0].toInt()
            val minute = parts[1].toInt()
            val second = parts[2].toInt()
            return MyLocalTime(hour, minute, second)
        }
    }

    fun toMillis(): Long {
        return TimeUnit.HOURS.toMillis(hour.toLong()) +
                TimeUnit.MINUTES.toMillis(minute.toLong()) +
                TimeUnit.SECONDS.toMillis(second.toLong())
    }
}