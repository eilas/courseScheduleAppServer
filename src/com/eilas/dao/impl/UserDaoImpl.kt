package com.eilas.dao.impl

import com.eilas.dao.IUserDao
import com.eilas.dao.SQLHelperPoolFactory
import com.eilas.entity.Student
import com.eilas.entity.User

class UserDaoImpl : IUserDao {
    val objectPool = SQLHelperPoolFactory.getPool()

    override fun save(user: User) {
        objectPool.borrowObject().apply {
            if (user is Student)
                connection.createStatement()
                    .executeUpdate("insert into student(student_id, name, sex, password) values ('${user.id}','${user.name}','${user.sex}','${user.pwd}');")
            else
                connection.createStatement()
                    .executeUpdate("insert into teacher(teacher_id, name, sex) values ('${user.id}','${user.name}','${user.sex}');")

            objectPool.returnObject(this)
        }


    }

    override fun update(user: User) {
        TODO("Not yet implemented")
    }

    override fun select(id: String): User =
        objectPool.borrowObject().let { sqlHelper ->
            sqlHelper.connection.createStatement().executeQuery("select * from student where student_id='$id';")
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

    override fun selectClassmate(useId: String, courseId: Int): List<User> {
        val sqlHelper = objectPool.borrowObject()
        val arrayList = ArrayList<User>()
//        在一周多节课的情况下sql语句有问题
        sqlHelper.connection.createStatement()
            .executeQuery("select student_id,name,sex from student where student_id !='$useId' and student_id in (select student_id from course_record where course_id=$courseId);")
            .let {
                while (it.next()) {
                    arrayList.add(Student(it.getString(1), it.getString(2), User.Sex.valueOf(it.getString(3)), null))
                }
            }
        objectPool.returnObject(sqlHelper)
        return arrayList
    }

    override fun delete(user: User) {
        objectPool.borrowObject().apply {
            connection.createStatement()
                .executeUpdate("delete from student where student_id='${user.id}';")

            objectPool.returnObject(this)
        }
    }
}

fun main() {
    var userDaoImpl = UserDaoImpl()
//    var student = Student("333", "aaaabbb", User.Sex.MALE, "123456")
//    userDaoImpl.save(student as User)

/*
    userDaoImpl.select("333").let { user ->
        println(user)
        userDaoImpl.delete(user)
    }
*/
    println(userDaoImpl.selectClassmate("222", 19))
}
