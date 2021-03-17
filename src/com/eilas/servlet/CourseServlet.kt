package com.eilas.servlet

import com.eilas.dao.impl.CourseDaoImpl
import com.eilas.entity.Course
import com.google.gson.JsonParser
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/course")
class CourseServlet : BaseServlet() {

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        response.characterEncoding = "utf-8"
        request.let {
            val action = it.getParameter("action")
            val courseDaoImpl = CourseDaoImpl()
            val jsonParser = JsonParser()
            when (action) {
                "search" -> {
                    val mode = it.getParameter("mode")
                    if (mode.equals("week")) {
//                        查询某周全部课程粗略信息
                        val id = jsonParser.parse(it.reader.readLine()).asJsonObject["id"].asString
                        courseDaoImpl.selectAllWeek(id, it.getParameter("week").toInt()).let {
                            response.writer.write(gson.toJson(it))
                        }
                    } else if (mode.equals("day")) {
//                        查询某天课程
                        val id = jsonParser.parse(it.reader.readLine()).asJsonObject["id"].asString
                        courseDaoImpl.selectAllDay(
                            id,
                            it.getParameter("week").toInt(),
                            Calendar.getInstance()
                                .apply { time = SimpleDateFormat("yyyy-MM-dd").parse(it.getParameter("time")) })
                            .let { response.writer.write(gson.toJson(it)) }
                    } else {
//                        查询单个课程
                        jsonParser.parse(it.reader.readLine()).asJsonObject.apply {
                            val userId = this["user"].asJsonObject["id"].asString
                            val courseId = this["courseId"].asInt
                            courseDaoImpl.select(courseId).let {
                                response.writer.write(gson.toJson(it))
                            }
                        }
                    }
                }
                "save" -> {
//                    添加课程
                    jsonParser.parse(request.reader.readLine()).asJsonObject.apply {
                        val user = this["user"].asJsonObject
                        val course = this["course"].asJsonObject

//                    通过name和location筛选课程，如果course里存在记录，则直接添加一条course_record
                        kotlin.runCatching {
                            courseDaoImpl.selectByNameOrAndLocation(
                                course["courseName"].asString,
                                course["location"].asString
                            )
                        }.onSuccess {
                            courseDaoImpl.saveRecord(it, user["id"].asString)
                        }.onFailure {
                            courseDaoImpl.apply {
                                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//                            执行顺序为先save后saveRecord
                                saveRecord(
                                    Course(
                                        id = null,
                                        name = course["courseName"].asString,
                                        info = course["info"].asString,
                                        location = course["location"].asString,
                                        strWeek = course["strWeek"].asInt,
                                        endWeek = course["strWeek"].asInt + course["lastWeek"].asInt - 1,
                                        strTime1 = simpleDateFormat.parse(course["courseStrTime1"].asString),
                                        endTime1 = simpleDateFormat.parse(course["courseEndTime1"].asString)
                                    ).also {
                                        it.id = save(it)
                                    }, user["id"].asString
                                )
                            }
                        }
                        response.writer.write(gson.toJson(Result.OK))
                    }

                }
                "drop" -> {
                    jsonParser.parse(request.reader.readLine()).asJsonObject.apply {
                        val user = this["user"].asJsonObject
                        val course = this["course"].asJsonObject

                        courseDaoImpl.delete(courseDaoImpl.select(course["id"].asInt), user["id"].asString)

                        response.writer.write(gson.toJson(Result.OK))
                    }
                }
                else -> {
                }
            }
        }

        response.writer.close()
    }
}