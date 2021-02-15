package com.eilas.dao.impl

import com.eilas.dao.IUserDao
import com.eilas.dao.SQLHelperPoolFactory
import com.eilas.entity.Student
import com.eilas.entity.User
import com.eilas.servlet.LoginServlet
import java.sql.Connection
import java.sql.DriverManager

class UserDaoImpl : IUserDao {
    val objectPool = SQLHelperPoolFactory.getPool()

    override fun save(user: User) {
        objectPool.borrowObject().apply {
            if (user is Student)
                connection.createStatement()
                    .executeUpdate("insert into student(student_id, name, sex, password) values (${user.id},'${user.name}','${user.sex}','${user.pwd}');")
            else
                connection.createStatement()
                    .executeUpdate("insert into teacher(teacher_id, name, sex) values (${user.id},'${user.name}','${user.sex}');")

            objectPool.returnObject(this)
        }


    }

    override fun update(user: User) {
        TODO("Not yet implemented")
    }

    override fun select(id: String): User =
        objectPool.borrowObject().let { sqlHelper ->
            sqlHelper.connection.createStatement().executeQuery("select * from student where student_id=$id;")
                .let {
                    it.first()

                    objectPool.returnObject(sqlHelper)

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
        }

    override fun delete(user: User) {
        objectPool.borrowObject().apply {
            connection.createStatement()
                .executeUpdate("delete from student where student_id=${user.id}")

            objectPool.returnObject(this)
        }
    }
}

fun main() {
    var student = Student("333", "aaaabbb", User.Sex.MALE, "123456")
    var userDaoImpl = UserDaoImpl()
    userDaoImpl.save(student)
    userDaoImpl.select("333").let { user ->
        println(user)
        userDaoImpl.delete(user)
    }
}
