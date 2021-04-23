package com.eilas.dao

import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

object MQHelpers {

    val client = MqttClient("tcp://139.224.33.253:1883", "producer_0", MemoryPersistence()).apply {
        connect(MqttConnectOptions().apply {
            isCleanSession = false
            userName = "guest"
            password = "guest".toCharArray()
        })
    }

    fun productM(message: String): String {
        kotlin.runCatching {
            client.apply {
                connect(MqttConnectOptions().apply {
                    isCleanSession = false
                    userName = "guest"
                    password = "guest".toCharArray()
                })
                publish("mqtt测试", MqttMessage().apply {
                    payload = message.toByteArray()
                    qos = 1
                })
                disconnect()
                close()
            }
        }.onFailure {
            it.printStackTrace()
            val s: StringBuilder = StringBuilder("$it")
            it.stackTrace.forEach { s.append("\n   at $it") }
            return s.toString()
        }
        return true.toString()
    }

    fun consumeM(): String {
        var message: String = ""
        kotlin.runCatching {
            client.apply {
                connect(MqttConnectOptions().apply {
                    isCleanSession = false
                    userName = "guest"
                    password = "guest".toCharArray()
                })
                setCallback(object : MqttCallback {
                    override fun connectionLost(p0: Throwable?) {

                    }

                    override fun messageArrived(p0: String?, p1: MqttMessage?) {
                        println("mqtt consume topic:$p0 message:${p1?.let { String(it.payload) }}")
                        message = p1?.payload.toString()
                        this@apply.apply {
                            disconnect()
                            close()
                        }
                    }

                    override fun deliveryComplete(p0: IMqttDeliveryToken?) {

                    }

                })
                subscribe("mqtt测试", 1)
            }
        }.onFailure {
            it.printStackTrace()
            val s: StringBuilder = StringBuilder("$it")
            it.stackTrace.forEach { s.append("\n   at $it") }
            return s.toString()
        }
        return message
    }

    /**
     * @param topic 消息主题，用于区分不同用户设备，格式notify/userId
     * @param message 消息内容，包含用户id、姓名、请求时间、课程名称的json字符串
     */
    fun publishNotifyMessage(topic: String, message: String) {
        kotlin.runCatching {
            client.publish(topic, MqttMessage().apply {
                payload = message.toByteArray()
                qos = 1
            })
        }.onFailure {
            it.printStackTrace()
        }
    }

    fun reConnect() {
        client.apply {
            disconnect()
            close()
            connect(MqttConnectOptions().apply {
                isCleanSession = false
                userName = "guest"
                password = "guest".toCharArray()
            })
        }
    }
}
