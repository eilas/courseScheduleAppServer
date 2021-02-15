package com.eilas.dao.impl

import com.eilas.dao.ICourseDao
import com.eilas.dao.SQLHelperPoolFactory
import com.eilas.entity.Course
import java.text.SimpleDateFormat

class CourseDaoImpl : ICourseDao {
    private val objectPool = SQLHelperPoolFactory.getPool()

    override fun save(course: Course) {
        TODO("Not yet implemented")
    }

    override fun update(course: Course) {
        TODO("Not yet implemented")
    }

    override fun select(courseId: String): Course {
        TODO("Not yet implemented")
    }

    override fun selectAll(studentId: String, week: Int): ArrayList<Course> {

        return objectPool.borrowObject().let { sqlHelper ->
            sqlHelper.connection.createStatement()
                .executeQuery(("select course_id,name,odd_week_str_time1," +
                        "odd_week_end_time1," +
                        "odd_week_str_time2," +
                        "odd_week_end_time2 " +
                        "from course where course_id in" +
                        " (select course_id from course_record where student_id=$studentId) " +
                        "and $week between str_week and end_week;")
                    .apply {
                        if (week % 2 == 0)
                            this.replace("odd_week", "even_week")
                    }).let {
                    val coureList = ArrayList<Course>()
                    val dateFormat = SimpleDateFormat()
                    while (it.next()) {
                        coureList.add(
                            Course(
                                id = it.getString(1),
                                name = it.getString(2),
                                strTime1 = dateFormat.parse(it.getString(3)),
                                endTime1 = dateFormat.parse(it.getString(4)),
                                strTime2 = dateFormat.parse(it.getString(5)),
                                endTime2 = dateFormat.parse(it.getString(5))
                            )
                        )
                    }

                    objectPool.returnObject(sqlHelper)
                    coureList
                }
        }
    }


    override fun delete(course: Course) {
        TODO("Not yet implemented")
    }
}