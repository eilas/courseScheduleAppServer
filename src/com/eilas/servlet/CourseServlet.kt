package com.eilas.servlet

import com.eilas.dao.impl.CourseDaoImpl
import com.google.gson.Gson
import com.google.gson.JsonParser
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/course")
class CourseServlet : HttpServlet() {
    protected val gson: Gson = Gson()

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        request.let {
            if (it.getParameter("search").toBoolean() && it.getParameter("all").toBoolean()) {
                val id = JsonParser().parse(it.reader.readLine()).asJsonObject["id"].asString
                CourseDaoImpl().selectAll(id, it.getParameter("week").toInt()).let {
                    response.writer.write(gson.toJson(it))
                }
            } else {

            }
        }

        response.writer.close()
    }
}