package com.eilas.servlet

import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/register")
class RegisterServlet : HttpServlet() {
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {

    }

}