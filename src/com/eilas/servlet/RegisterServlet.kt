package com.eilas.servlet

import com.eilas.dao.impl.UserDaoImpl
import com.eilas.entity.Student
import com.eilas.entity.User
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/register")
class RegisterServlet : HttpServlet() {
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        UserDaoImpl().save(
            Student(
                request.getParameter("id"),
                request.getParameter("name"),
                if (request.getParameter("sex").equals(User.Sex.MALE.toString(),true))
                    User.Sex.MALE
                else
                    User.Sex.FEMALE,
                request.getParameter("pwd")

            )
        )

        response.writer.write("OK")
    }

}