package com.eilas.servlet

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/login")
class LoginServlet : HttpServlet() {
    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {


        val list = listOf("aaa", "bbbc")
        val maxLength = list.maxByOrNull({ it.length })
        println(list.map { word: String -> word[0].toUpperCase() + word.subSequence(1, word.length).toString() })

        list.let { list1 ->
            println(list)
            println(list1)
        }
        println("${list.lastIndex} is length!")

        resp?.writer?.write(maxLength.toString())

    }

}