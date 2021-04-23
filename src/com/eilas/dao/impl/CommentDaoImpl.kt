package com.eilas.dao.impl

import com.eilas.dao.ICommentDao
import com.eilas.dao.SQLHelpers
import com.eilas.entity.CommentMessage
import com.eilas.entity.DefaultCommentModel
import com.eilas.entity.ReplyMessage
import java.sql.Statement
import java.sql.Timestamp
import java.sql.Types

class CommentDaoImpl : ICommentDao {
    private val objectPool = SQLHelpers.getPool()

    override fun saveComment(comment: CommentMessage, courseId: Int, userId: String): Long {
        val sqlHelper = objectPool.borrowObject()
        val statement = sqlHelper.connection.prepareStatement(
            "INSERT INTO comment(kid, pid, content, date, prize,course_id,student_id) VALUES (?,?,?,?,?,?,?);",
            Statement.RETURN_GENERATED_KEYS
        )
        statement.apply {
            setNull(1, Types.NULL)
            setLong(2, comment.pid)
            setString(3, comment.comment)
            setTimestamp(4, Timestamp(comment.date))
            setInt(5, comment.prizes)
            setInt(6, courseId)
            setString(7, userId)
        }.executeUpdate()

        objectPool.returnObject(sqlHelper)

        return statement.generatedKeys.takeIf { it.next() }!!.getLong(1)
    }


    override fun saveReply(reply: ReplyMessage, courseId: Int, userId: String): Long {
        val sqlHelper = objectPool.borrowObject()
        val statement = sqlHelper.connection.prepareStatement(
            "INSERT INTO comment(kid, pid, content, date, prize,course_id,student_id) VALUES (?,?,?,?,?,?,?);",
            Statement.RETURN_GENERATED_KEYS
        )
        statement.apply {
            setLong(1, reply.kid)
            setLong(2, reply.pid)
            setString(3, reply.reply)
            setTimestamp(4, Timestamp(reply.date))
            setInt(5, reply.prizes)
            setInt(6, courseId)
            setString(7, userId)
        }.executeUpdate()

        objectPool.returnObject(sqlHelper)

        return statement.generatedKeys.takeIf { it.next() }!!.getLong(1)

    }

    override fun selectCommentModel(courseId: Int): DefaultCommentModel {
        /**
         * 先查询kid为null的所有评论，然后再查询每条评论的回复
         */
        val sqlHelper = objectPool.borrowObject()
        val resultSet = sqlHelper.connection.createStatement()
            .executeQuery("SELECT * FROM comment WHERE course_id=$courseId AND kid IS NULL ORDER BY date DESC ;")
        val commentModel = DefaultCommentModel().apply { comments = ArrayList<CommentMessage>() }
        val userDaoImpl = UserDaoImpl()
        while (resultSet.next()) {
            val resultSet1 = sqlHelper.connection.createStatement()
                .executeQuery("SELECT * FROM comment WHERE kid=${resultSet.getLong(1)} ORDER BY date DESC ;")
            (commentModel.comments as ArrayList).add(CommentMessage().apply {
                id = resultSet.getLong(1)
                pid = resultSet.getLong(3)
                comment = resultSet.getString(4)
                date = resultSet.getTimestamp(5).time
                prizes = resultSet.getInt(6)
                posterName = userDaoImpl.select(resultSet.getString(8)).name
                replies = ArrayList<ReplyMessage>().apply {
                    while (resultSet1.next())
                        add(ReplyMessage().apply {
                            id = resultSet1.getLong(1)
                            kid = resultSet.getLong(2)
                            pid = resultSet1.getLong(3)
                            reply = resultSet1.getString(4)
                            date = resultSet1.getTimestamp(5).time
                            prizes = resultSet1.getInt(6)
                            replierName = userDaoImpl.select(resultSet1.getString(8)).name
                        })
                }
            })
        }

        objectPool.returnObject(sqlHelper)
        return commentModel
    }


    override fun selectComment(x: Any) {
        TODO("Not yet implemented")
    }

    override fun like(messageId: Long, courseId: Int, userId: String) {
        val sqlHelper = objectPool.borrowObject()
        sqlHelper.connection.createStatement().executeUpdate("UPDATE comment SET prize=prize+1 WHERE id=$messageId;")

        objectPool.returnObject(sqlHelper)
    }
}

