package com.eilas.dao

import com.eilas.entity.Course

interface ICourseDao {
    fun save(course: Course): Int
    fun saveRecord(course: Course, studentId: String)
    fun update(course: Course)
    fun select(courseId: String): Course
    fun selectByNameOrAndLocation(vararg coursearg: String): Course
    fun selectAll(studentId: String, week: Int): ArrayList<Course>
    fun delete(course: Course)
}