package com.eilas.servlet

import com.google.gson.Gson
import javax.servlet.http.HttpServlet

open class BaseServlet : HttpServlet() {
    protected data class Result(val result: Status) {
        enum class Status(i: Int) {
            OK(0), pwdError(1), noUser(2), otherError(3)
        }
    }

    protected val gson: Gson = Gson()
}