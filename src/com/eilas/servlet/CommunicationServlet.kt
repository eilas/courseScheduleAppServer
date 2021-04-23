package com.eilas.servlet

import com.eilas.dao.impl.CommentDaoImpl
import com.eilas.entity.CommentMessage
import com.eilas.entity.ReplyMessage
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/communication")
class CommunicationServlet : BaseServlet() {
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        response.characterEncoding = "utf-8"
        val action = request.getParameter("action")
        val method = request.getParameter("method")
        val jsonObject = JsonParser().parse(request.reader.readLine()).asJsonObject


        when (action) {
            "save" -> {
                val userId = jsonObject["userId"].asString
                val courseId = jsonObject["courseId"].asInt
                val message = jsonObject["message"]

                val messageId = saveMessage(message, courseId, userId, method)
                val toJson = gson.toJson(mapOf("messageId" to messageId))
                println(toJson)
                response.writer.write(toJson)
            }
            "search" -> {
                val userId = jsonObject["userId"].asString
                val courseId = jsonObject["courseId"].asInt

                val message1 = searchMessage(courseId, userId, method)
                response.writer.write(gson.toJson(message1))
            }
            "update" -> {
                val messageId = jsonObject["messageId"].asLong
                val userId = jsonObject["userId"].asString
                val courseId = jsonObject["courseId"].asInt

                updateMessage(messageId, courseId, userId, method)
                response.writer.write(gson.toJson(Result.OK))

            }
        }
        response.writer.close()
    }


    private fun saveMessage(message: JsonElement, courseId: Int, userId: String, method: String): Long {
        val commentDaoImpl = CommentDaoImpl()
        return when (method) {
            "comment" -> {
                commentDaoImpl.saveComment(gson.fromJson(message, CommentMessage::class.java), courseId, userId)
            }
            "reply" -> {
                commentDaoImpl.saveReply(gson.fromJson(message, ReplyMessage::class.java), courseId, userId)
            }
            else -> throw Exception()
        }
    }

    private fun searchMessage(courseId: Int, userId: String, method: String): Any {
        val commentDaoImpl = CommentDaoImpl()
        return when (method) {
            "comment" -> {
//                commentDaoImpl.selectComment()

            }
            "reply" -> {

            }
            "all" -> {
                commentDaoImpl.selectCommentModel(courseId)
            }
            else -> throw Exception()
        }
    }

    private fun updateMessage(messageId: Long, courseId: Int, userId: String, method: String) {
        val commentDaoImpl = CommentDaoImpl()
        when (method) {
            "like" -> {
                commentDaoImpl.like(messageId, courseId, userId)
            }
            else -> throw Exception()
        }
    }

}