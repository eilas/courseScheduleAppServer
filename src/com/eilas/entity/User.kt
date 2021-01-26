package com.eilas.entity

abstract class User {
    abstract var id: String
    abstract var name: String
    abstract var sex: Sex

    enum class Sex(i: Int) {
        MALE(0), FEMALE(1)
    }

//    abstract fun save()
}