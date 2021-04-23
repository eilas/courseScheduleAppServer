package com.eilas.servlet.test

import com.eilas.dao.MQHelpers
import com.eilas.servlet.BaseServlet
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/MQtest")
class MQServlet : BaseServlet() {
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.characterEncoding = "utf-8"
        val parameter = req.getParameter("action")
        if (parameter.equals("prod")) {
            MQHelpers.productM(req.getParameter("message")).let {
                resp.writer.write(it)
            }
        } else if (parameter.equals("cons")) {
            MQHelpers.consumeM().let {
                resp.writer.write(it)
            }
        } else {
            resp.writer.write("这个请求没啥用~")
        }
        resp.writer.close()
    }
}