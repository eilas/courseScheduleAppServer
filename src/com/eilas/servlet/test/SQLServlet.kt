package com.eilas.servlet.test

import com.eilas.dao.SQLHelper
import com.eilas.dao.SQLHelpers
import com.eilas.servlet.BaseServlet
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/SQLtest")
class SQLServlet : BaseServlet() {
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        if (req.getParameter("check")?.equals("mysql") == true) {
            kotlin.runCatching {
                val pool = SQLHelpers.getPool()
                val set = HashSet<SQLHelper>()
                var string: StringBuilder = StringBuilder()
                for (i in 0..15) {
                    val sqlHelper = pool.borrowObject()
                    string.append("$sqlHelper\n")
                    set.add(sqlHelper)
                }
                set.forEach { pool.returnObject(it) }
                resp.writer.write(string.toString())
            }.onFailure {
                val s: StringBuilder = StringBuilder("$it")
                it.stackTrace.forEach { s.append("\n   at $it") }
                resp.writer.write(s.toString())
            }
            resp.writer.close()
        }
    }
}