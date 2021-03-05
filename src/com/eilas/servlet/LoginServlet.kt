package com.eilas.servlet

import com.eilas.dao.impl.UserDaoImpl
import com.eilas.entity.Student
import com.google.gson.Gson
import com.google.gson.JsonParser
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/login")
open class LoginServlet : BaseServlet() {

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        var jsonObject = JsonParser().parse(request.reader.readLine()).asJsonObject

        kotlin.runCatching {
            UserDaoImpl().select(jsonObject["id"].asString).let {
                if (it is Student && jsonObject["pwd"].asString.equals(it.pwd))
//                    println(Gson().toJson(mapOf("result" to "OK")))
                    response.writer.write(gson.toJson(Result(Result.Status.OK)))
                else throw Exception(Result.Status.pwdError.toString())
            }
        }.onFailure {
            if (it.message.equals(Result.Status.pwdError.toString()))
                response.writer.write(gson.toJson(Result(Result.Status.pwdError)))
            else
                response.writer.write(gson.toJson(Result(Result.Status.noUser)))
        }
        response.writer.close()
    }
}

