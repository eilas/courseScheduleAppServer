package com.eilas.dao

import org.apache.commons.pool2.PooledObject
import org.apache.commons.pool2.PooledObjectFactory
import org.apache.commons.pool2.impl.DefaultPooledObject
import org.apache.commons.pool2.impl.GenericObjectPool
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import java.sql.Connection
import java.sql.DriverManager

class SQLHelper {
    companion object {
        init {
            Class.forName("com.mysql.cj.jdbc.Driver")
        }
    }

    var connection: Connection = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/course_schedule?characterEncoding=utf-8&serverTimezone=GMT%2B8",
        "root",
        "123456"
    )
}

object SQLHelpers : PooledObjectFactory<SQLHelper> {
    private val genericObjectPool = GenericObjectPool<SQLHelper>(
        SQLHelpers,
        GenericObjectPoolConfig<SQLHelper>().apply {
            maxTotal = 16
        })


    fun getPool(): GenericObjectPool<SQLHelper> = genericObjectPool

    override fun makeObject(): PooledObject<SQLHelper> = DefaultPooledObject<SQLHelper>(SQLHelper())

    override fun destroyObject(p0: PooledObject<SQLHelper>?) {
        var sqlHelper = p0?.`object`
        sqlHelper = null
    }

    override fun validateObject(p0: PooledObject<SQLHelper>?): Boolean = true

    override fun activateObject(p0: PooledObject<SQLHelper>?) {

    }

    override fun passivateObject(p0: PooledObject<SQLHelper>?) {

    }

}

fun main(){
    println(SQLHelper().connection)
}