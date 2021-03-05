package com.eilas.dao.impl

import com.eilas.dao.ICourseDao
import com.eilas.dao.SQLHelperPoolFactory
import com.eilas.entity.Course
import java.sql.Statement
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CourseDaoImpl : ICourseDao {
    private val objectPool = SQLHelperPoolFactory.getPool()

    override fun save(course: Course): Int {
        println("course:$course")

        val sqlHelper = objectPool.borrowObject()
        sqlHelper.connection.prepareStatement(
            "insert into course(name, info, location, str_week, end_week, odd_week_str_time1, odd_week_end_time1, odd_week_str_time2, odd_week_end_time2, even_week_str_time1, even_week_end_time1, even_week_str_time2, even_week_end_time2) values (?,?,?,?,?,?,?,?,?,?,?,?,?);",
            Statement.RETURN_GENERATED_KEYS
        ).apply {
            setString(1, course.name)
            setString(2, course.info)
            setString(3, course.location)
            setInt(4, course.strWeek)
            setInt(5, course.endWeek)
            setTimestamp(6, Timestamp(course.strTime1.time))
            setTimestamp(7, Timestamp(course.endTime1.time))
            setTimestamp(8, course.strTime2?.let { Timestamp(it.time) })
            setTimestamp(9, course.endTime2?.let { Timestamp(it.time) })
            setTimestamp(10, Timestamp(course.strTime1.time))
            setTimestamp(11, Timestamp(course.endTime1.time))
            setTimestamp(12, course.strTime2?.let { Timestamp(it.time) })
            setTimestamp(13, course.endTime2?.let { Timestamp(it.time) })
            executeUpdate()
            objectPool.returnObject(sqlHelper)
//            return自增主键
            return generatedKeys.let {
                if (it.next())
                    it.getInt(1)
                else
                    throw Exception("获取不了course的自增主键")
            }
        }

    }

    override fun saveRecord(course: Course, studentId: String) {
        println("course record:$course")

        objectPool.borrowObject().connection.createStatement()
            .executeUpdate("insert into course_record(course_id, student_id, teacher_id) values (${course.id},'$studentId',null);")
    }

    override fun update(course: Course) {
        TODO("Not yet implemented")
    }

    override fun select(courseId: String): Course {
        TODO("Not yet implemented")
    }

    override fun selectByNameOrAndLocation(vararg coursearg: String): Course {
        var sql =
            "select course_id,name,location,odd_week_str_time1,odd_week_end_time1," +
                    "odd_week_str_time2,odd_week_end_time2 from course where name="
        for (i in coursearg.indices) {
            when (i) {
                0 -> sql += coursearg[i]
                1 -> sql += "and location=$coursearg[i]"
            }
        }
        sql = "$sql;"

        return objectPool.borrowObject().let { sqlHelper ->
            sqlHelper.connection.createStatement().executeQuery(sql).let {
                it.first()
                objectPool.returnObject(sqlHelper)
                val dateFormat = SimpleDateFormat()
                Course(
                    id = it.getInt(1),
                    name = it.getString(2),
                    location = it.getString(3),
                    strTime1 = dateFormat.parse(it.getString(4)),
                    endTime1 = dateFormat.parse(it.getString(5)),
                    strTime2 = dateFormat.parse(it.getString(6)),
                    endTime2 = dateFormat.parse(it.getString(7))
                )
            }
        }
    }


    override fun selectAll(studentId: String, week: Int): ArrayList<Course> {

        return objectPool.borrowObject().let { sqlHelper ->
            sqlHelper.connection.createStatement()
                .executeQuery(("select course_id,name,location,odd_week_str_time1," +
                        "odd_week_end_time1," +
                        "odd_week_str_time2," +
                        "odd_week_end_time2 " +
                        "from course where course_id in" +
                        " (select course_id from course_record where student_id='$studentId') " +
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
                                id = it.getInt(1),
                                name = it.getString(2),
                                location = it.getString(3),
                                strTime1 = dateFormat.parse(it.getString(4)),
                                endTime1 = dateFormat.parse(it.getString(5)),
                                strTime2 = dateFormat.parse(it.getString(6)),
                                endTime2 = dateFormat.parse(it.getString(7))
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

fun main() {
    val courseDaoImpl = CourseDaoImpl()
    val time = Calendar.getInstance().time
    val course = Course(null, "aaa", "aaa", "aaa", 1, 10, time, time, time, time)
    println(courseDaoImpl.save(course))
}