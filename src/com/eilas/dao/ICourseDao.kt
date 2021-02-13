package com.eilas.dao

import com.eilas.entity.Course
import com.eilas.entity.Student

interface ICourseDao {
    fun save(course: Course)
    fun update(course: Course)
    fun select(courseId: String): Course
    fun selectAll(studentId: String, week: Int): ArrayList<Course>
    fun delete(course: Course)
}