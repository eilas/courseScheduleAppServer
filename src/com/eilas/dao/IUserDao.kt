package com.eilas.dao

import com.eilas.entity.User

interface IUserDao {
    fun save(user: User)
    fun update(user: User)
    fun select(id: String): User
    fun delete(user: User)
}