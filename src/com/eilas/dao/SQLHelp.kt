package com.eilas.dao

import java.sql.Connection
import java.sql.DriverManager

object SQLHelp {
    lateinit var connection: Connection

    init {
        Class.forName("com.mysql.cj.jdbc.Driver")
        connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/course_schedule?characterEncoding=utf-8&serverTimezone=GMT",
            "root",
            "123456"
        )
    }
}