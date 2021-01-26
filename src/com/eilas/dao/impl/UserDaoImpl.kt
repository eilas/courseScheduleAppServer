package com.eilas.dao.impl

import com.eilas.dao.IUserDao
import com.eilas.entity.Student
import com.eilas.entity.User
import java.sql.Connection
import java.sql.DriverManager

class UserDaoImpl : IUserDao {

    companion object {
        lateinit var connection: Connection
    }

    init {
        Class.forName("com.mysql.cj.jdbc.Driver")
        connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/course_schedule?characterEncoding=utf-8&serverTimezone=GMT",
            "root",
            "123456"
        )
    }

    override fun save(user: User) {
        if (user is Student)
            connection.createStatement()
                .executeUpdate("insert into student(student_id, name, sex, password) values (${user.id},'${user.name}','${user.sex}','${user.pwd}');")
        else
            connection.createStatement()
                .executeUpdate("insert into teacher(teacher_id, name, sex) values (${user.id},'${user.name}','${user.sex}');")


    }

    override fun update(user: User) {
        TODO("Not yet implemented")
    }

    override fun select(id: String): User =
        connection.createStatement().executeQuery("select * from student where student_id=$id;")
            .let {
                it.first()
                Student(
                    it.getString("student_id"),
                    it.getString("name"),
                    if (it.getString("sex").equals(User.Sex.MALE.toString(), true))
                        User.Sex.MALE
                    else
                        User.Sex.FEMALE,
                    it.getString("password")
                )
            }

    override fun delete(user: User) {
        connection.createStatement()
            .executeUpdate("delete from student where student_id=${user.id}")
    }
}

fun main() {
    var student = Student("222", "aaaabbb", User.Sex.MALE, "123456")
    var userDaoImpl = UserDaoImpl()
    userDaoImpl.save(student)
    userDaoImpl.select("222").let { user -> userDaoImpl.delete(user) }

}