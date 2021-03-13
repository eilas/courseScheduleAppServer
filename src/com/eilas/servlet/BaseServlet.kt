package com.eilas.servlet

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import javax.servlet.http.HttpServlet

open class BaseServlet : HttpServlet() {
    protected data class Result(val result: Status) {
        companion object {
            val OK = Result(Status.OK)
            val pwdError = Result(Status.pwdError)
            val noUser = Result(Status.noUser)
            val otherError = Result(Status.otherError)
        }

        enum class Status(i: Int) {
            OK(0), pwdError(1), noUser(2), otherError(3)
        }
    }

    protected val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
}