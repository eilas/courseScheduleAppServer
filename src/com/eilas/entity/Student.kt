package com.eilas.entity

data class Student(override var id: String, override var name: String, override var sex: Sex, var pwd: String?) :
    User() {

}


fun main() {
    var student = Student("11111", "aaa", User.Sex.MALE, "123456")
    println(student)
    student.id = "222222"
}