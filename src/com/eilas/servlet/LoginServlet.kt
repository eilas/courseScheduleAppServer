package com.eilas.servlet

import com.eilas.dao.impl.UserDaoImpl
import com.eilas.entity.Student
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/login")
class LoginServlet : HttpServlet() {
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        UserDaoImpl().select(request.getParameter("id")).let {
            if (it is Student && request.getParameter("pwd").equals(it.pwd)) {
                response.writer.write("OK")
            }
        }
        response.writer.write("ERROR")
    }

}