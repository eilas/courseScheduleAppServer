package com.eilas.dao.impl

import com.eilas.dao.ICourseDao
import com.eilas.dao.SQLHelpers
import com.eilas.entity.Course
import java.sql.Statement
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CourseDaoImpl : ICourseDao {
    private val objectPool = SQLHelpers.getPool()

    override fun save(course: Course): Int {
//        println("course:$course")

        val sqlHelper = objectPool.borrowObject()
        sqlHelper.connection.prepareStatement(
            "insert into course(name, info, location, str_week, end_week, odd_week_str_time1, odd_week_end_time1, odd_week_str_time2, odd_week_end_time2, even_week_str_time1, even_week_end_time1, even_week_str_time2, even_week_end_time2) values (?,?,?,?,?,?,?,?,?,?,?,?,?);",
            Statement.RETURN_GENERATED_KEYS
        ).apply {
            setString(1, course.name)
            setString(2, /*course.info*/null)
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
//        println("course record:$course")

        val sqlHelper = objectPool.borrowObject()
        sqlHelper.connection.createStatement()
            .executeUpdate("insert into course_record(course_id, student_id, teacher_id,additional_info) values (${course.id},'$studentId',null,'${course.info}');")
        objectPool.returnObject(sqlHelper)
    }

    override fun update(course: Course) {
        TODO("Not yet implemented")
    }

    override fun select(courseId: Int): Course {
        val sqlHelper = objectPool.borrowObject()
        return sqlHelper.connection.createStatement().executeQuery("select * from course where course_id=$courseId;")
            .let {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                objectPool.returnObject(sqlHelper)
                it.first()
                Course(
                    it.getInt(1),
                    it.getString(2),
                    it.getString(3),
                    it.getString(4),
                    it.getInt(5),
                    it.getInt(6),
                    dateFormat.parse(it.getString(7)),
                    dateFormat.parse(it.getString(8)),
                    it.getString(9)?.let { dateFormat.parse(it) },
                    it.getString(10)?.let { dateFormat.parse(it) }
                )
            }
    }

    override fun selectByNameOrAndLocation(vararg coursearg: String): Course {
        var sql =
            "select course_id,name,location,odd_week_str_time1,odd_week_end_time1," +
                    "odd_week_str_time2,odd_week_end_time2 from course where name="
        for (i in coursearg.indices) {
            when (i) {
                0 -> sql += "'${coursearg[i]}'"
                1 -> sql += "and location='${coursearg[i]}'"
            }
        }
        sql = "$sql;"

        return objectPool.borrowObject().let { sqlHelper ->
            sqlHelper.connection.createStatement().executeQuery(sql).let {
                it.first()
                objectPool.returnObject(sqlHelper)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                Course(
                    id = it.getInt(1),
                    name = it.getString(2),
                    location = it.getString(3),
                    strTime1 = dateFormat.parse(it.getString(4)),
                    endTime1 = dateFormat.parse(it.getString(5)),
                    strTime2 = it.getString(6)?.let { dateFormat.parse(it) },
                    endTime2 = it.getString(7)?.let { dateFormat.parse(it) }
                )
            }
        }
    }

    override fun selectAllDay(studentId: String, week: Int, day: Calendar): ArrayList<Course> {
        val courseList = selectAllWeek(
            studentId,
            week
        )
        courseList.removeIf { !(day[Calendar.DATE] == it.strTime1.date || day[Calendar.DATE] == it.strTime2?.date) }
        return courseList
    }


    override fun selectAllWeek(studentId: String, week: Int): ArrayList<Course> {

        return objectPool.borrowObject().let { sqlHelper ->
            sqlHelper.connection.createStatement()
                .executeQuery(("select course_id,name,location,str_week,odd_week_str_time1," +
                        "odd_week_end_time1," +
                        "odd_week_str_time2," +
                        "odd_week_end_time2 " +
                        "from course where course_id in" +
                        " (select course_id from course_record where student_id='$studentId') " +
                        "and $week between str_week and end_week order by odd_week_str_time1 asc;")
                    .apply {
                        if (week % 2 == 0)
                            this.replace("odd_week", "even_week")
                    }).let {
                    val courseList = ArrayList<Course>()
                    while (it.next()) {
                        val strWeek = it.getInt(4)
                        courseList.add(
                            Course(
                                id = it.getInt(1),
                                name = it.getString(2),
                                location = it.getString(3),
                                strTime1 = Calendar.getInstance().apply {
                                    time = Date(it.getTimestamp(5).time)
//                                    println("old course time="+this.time)
                                    add(Calendar.DATE, (week - strWeek) * 7)
//                                    println("new course time="+this.time)
                                }.time,
                                endTime1 = Calendar.getInstance().apply {
                                    time = Date(it.getTimestamp(6).time)
                                    add(Calendar.DATE, (week - strWeek) * 7)
                                }.time,
                                strTime2 = it.getTimestamp(7)?.let {
                                    Calendar.getInstance().apply {
                                        time = Date(it.time)
                                        add(Calendar.DATE, (week - strWeek) * 7)
                                    }.time
                                },
                                endTime2 = it.getTimestamp(8)?.let {
                                    Calendar.getInstance().apply {
                                        time = Date(it.time)
                                        add(Calendar.DATE, (week - strWeek) * 7)
                                    }.time
                                }
                            )
                        )
                    }

                    objectPool.returnObject(sqlHelper)
                    courseList
                }
        }
    }


    override fun delete(course: Course, studentId: String) {
        if (deleteRecord(course, studentId)) {
            val sqlHelper = objectPool.borrowObject()
            sqlHelper.connection.createStatement().executeUpdate("delete from course where course_id=${course.id};")
            objectPool.returnObject(sqlHelper)
        }
    }

    override fun deleteRecord(course: Course, studentId: String): Boolean {
        val sqlHelper = objectPool.borrowObject()
        sqlHelper.connection.createStatement().apply {
            executeUpdate("delete from course_record where course_id=${course.id} and student_id='$studentId';")
            executeQuery("select count(*) from course_record where course_id=${course.id};").let {
                objectPool.returnObject(sqlHelper)
                if (it.next()) {
                    return it.getInt(1) == 0
                }
            }
        }
        return false
    }
}

fun main() {
    val courseDaoImpl = CourseDaoImpl()
//    val time = Calendar.getInstance().time
//    val course = Course(null, "aaa", "aaa", "aaa", 1, 10, time, time, time, time)
//    println(courseDaoImpl.save(course))
//    courseDaoImpl.select(11)
    println(courseDaoImpl.selectByNameOrAndLocation("C语言", "b101"))
}