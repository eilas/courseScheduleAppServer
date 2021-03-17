package com.eilas.dao

import com.eilas.entity.Course
import java.util.*
import kotlin.collections.ArrayList

interface ICourseDao {
    fun save(course: Course): Int
    fun saveRecord(course: Course, studentId: String)
    fun update(course: Course)
    fun select(courseId: Int): Course
    fun selectByNameOrAndLocation(vararg coursearg: String): Course
    fun selectAllDay(studentId: String,week: Int, day: Calendar): ArrayList<Course>
    fun selectAllWeek(studentId: String, week: Int): ArrayList<Course>
    fun delete(course: Course,studentId: String)
    fun deleteRecord(course: Course, studentId: String):Boolean
}