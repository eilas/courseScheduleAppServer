package com.eilas.servlet

import com.eilas.dao.MQHelpers
import com.google.gson.JsonParser
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/notify")
class NotifyMessageServlet : BaseServlet() {
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        val jsonObject = JsonParser().parse(request.reader.readLine()).asJsonObject
        val map = mapOf<String, String>(
            "fromUserId" to jsonObject["fromUserId"].asString,
            "toUserId" to jsonObject["toUserId"].asString,
            "fromUserName" to jsonObject["fromUserName"].asString,
            "time" to jsonObject["time"].asString,
            "courseName" to jsonObject["courseName"].asString
        )
        MQHelpers.publishNotifyMessage("notify/${map["toUserId"]}", gson.toJson(map))
        response.writer.write(gson.toJson(Result.OK))
    }
}