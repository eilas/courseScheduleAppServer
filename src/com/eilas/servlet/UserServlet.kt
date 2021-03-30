package com.eilas.servlet

import com.eilas.dao.impl.UserDaoImpl
import com.google.gson.JsonParser
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/user")
class UserServlet : BaseServlet() {
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        response.characterEncoding = "utf-8"
        val userDaoImpl = UserDaoImpl()
        val jsonParser = JsonParser()
        when (request.getParameter("action")) {
            "search" -> {
                jsonParser.parse(request.reader.readLine()).asJsonObject.apply {
                    userDaoImpl.selectClassmate(this["user"].asJsonObject["id"].asString, this["courseId"].asInt).let {
                        response.writer.write(gson.toJson(it))
                    }
                }
            }
        }
        response.writer.close()
    }
}