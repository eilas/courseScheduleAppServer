package com.eilas.servlet

import com.eilas.dao.impl.UserDaoImpl
import com.eilas.entity.Student
import com.google.gson.JsonParser
import javax.servlet.annotation.WebServlet
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
                    response.writer.write(gson.toJson(Result.OK))
                else throw Exception(Result.pwdError.toString())
            }
        }.onFailure {
            if (it.message.equals(Result.pwdError.toString()))
                response.writer.write(gson.toJson(Result.pwdError))
            else
                response.writer.write(gson.toJson(Result.noUser))
        }
        response.writer.close()
    }

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        req.getRequestDispatcher("WEB-INF/view/test.jsp")?.forward(req, resp)
    }
}

