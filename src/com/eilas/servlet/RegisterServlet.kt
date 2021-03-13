package com.eilas.servlet

import com.eilas.dao.impl.UserDaoImpl
import com.eilas.entity.Student
import com.eilas.entity.User
import com.google.gson.Gson
import com.google.gson.JsonParser
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/register")
class RegisterServlet : BaseServlet() {

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        var jsonObject = JsonParser().parse(request.reader.readLine()).asJsonObject

        kotlin.runCatching {
            UserDaoImpl().save(
                Student(
                    jsonObject.get("id").asString,
                    jsonObject.get("name").asString,
                    if (jsonObject.get("sex").asString.equals(User.Sex.MALE.toString(), true))
                        User.Sex.MALE
                    else
                        User.Sex.FEMALE,
                    jsonObject.get("pwd").asString
                )
            )
        }.onSuccess {
            response.writer.write(gson.toJson(Result.OK))
        }.onFailure {
            it.printStackTrace()
            response.writer.write(gson.toJson(Result.otherError))
        }
        response.writer.close()
    }

}