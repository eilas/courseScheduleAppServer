package com.eilas.entity

/**
 * 客户端评论模型，服务器主要用来反序列化
 */
data class CommentMessage(val any: Any? = null) : DefaultCommentModel.Comment()

data class ReplyMessage(val any: Any? = null) : DefaultCommentModel.Comment.Reply()

open class DefaultCommentModel {
    /**
     * n条母数据
     */
    var comments: List<Comment>? = null

    /**
     * 母数据（评论）模型
     *
     * 可自定义数据类型，但必须实现CommentEnable接口，如下：
     */
    open class Comment {
        /**
         * 数据id
         */
        var id: Long = 0
        /**
         * 评论者用户名
         */
        var posterName: String? = null

        /**
         * 评论内容
         */
        var comment: String? = null

        /**
         * 时间
         */
        var date: Long = 0

        /**
         * 点赞数
         */
        var prizes = 0

        /**
         * 楼层ID，如果是楼主，为0
         * 在Comment类，该属性是0
         */
        var pid: Long = 0

        /**
         * m条子数据
         */
        var replies: List<Reply>? = null

        /**
         * 子数据（回复）模型
         */
        open class Reply {
            /**
             * 数据id
             */
            var id: Long = 0

            /**
             * 子级id
             */
            var kid: Long = 0

            /**
             * 回复者用户名
             */
            var replierName: String? = null

            /**
             * 评论内容
             */
            var reply: String? = null

            /**
             * 时间
             */
            var date: Long = 0

            /**
             * 点赞数
             */
            var prizes = 0

            /**
             * 楼层ID，如果是回复楼主，为0
             * 如果回复非楼主，为被回复者的ID
             */
            var pid: Long = 0
        }
    }
}
