package com.eilas.servlet

import com.eilas.dao.impl.CourseDaoImpl
import com.eilas.entity.Course
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.text.SimpleDateFormat
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/course")
class CourseServlet : BaseServlet() {

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        response.characterEncoding = "utf-8"
        request.let {
            val search = it.getParameter("search").toBoolean()
            val all = it.getParameter("all").toBoolean()
            val courseDaoImpl = CourseDaoImpl()
            val jsonParser = JsonParser()

            if (search && all) {//查询某周全部课程粗略信息
                val id = jsonParser.parse(it.reader.readLine()).asJsonObject["id"].asString
                courseDaoImpl.selectAll(id, it.getParameter("week").toInt()).let {
                    response.writer.write(gson.toJson(it))
                }
            } else if (search) {//查询单个课程

            } else {//添加课程
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
                        response.writer.write(gson.toJson(Result.OK))
                    }
                }
            }
        }

        response.writer.close()
    }
}