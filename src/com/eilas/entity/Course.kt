package com.eilas.entity

import java.util.*

//年、月、日、时、分、秒、周几
data class Course(
    val id: String,
    val name: String,
    val info: String = "",
    val strWeek: Int = 0,
    val endWeek: Int = 0,
    val strTime1: Date,
    val endTime1: Date,
    val strTime2: Date? = null,
    val endTime2: Date? = null
)