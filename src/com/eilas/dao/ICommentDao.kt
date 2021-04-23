package com.eilas.dao

import com.eilas.entity.CommentMessage
import com.eilas.entity.DefaultCommentModel
import com.eilas.entity.ReplyMessage

interface ICommentDao {
    fun saveComment(comment: CommentMessage, courseId: Int, userId: String): Long
    fun saveReply(reply: ReplyMessage, courseId: Int, userId: String): Long
    fun selectCommentModel(courseId: Int): DefaultCommentModel
    fun selectComment(x: Any)
    fun like(messageId: Long, courseId: Int, userId: String)
}