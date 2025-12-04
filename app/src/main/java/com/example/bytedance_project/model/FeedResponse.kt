package com.example.bytedance_project.model

import com.google.gson.annotations.SerializedName

data class FeedResponse(
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("has_more") val hasMore: Int,
    @SerializedName("post_list") val postList: List<Post> = emptyList()
)

data class Post(
    @SerializedName("post_id") val postId: String,
    @SerializedName("title") val title: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("create_time") val createTime: Long,
    @SerializedName("author") val author: Author?,
    @SerializedName("clips") val clips: List<Clip>?,
    @SerializedName("hashtag") val hashtags: List<Hashtag>?,
    val mockLikeCount: Int = (10..1000).random()
)

data class Author(
    @SerializedName("user_id") val userId: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("avatar") val avatar: String
)

data class Clip(
    @SerializedName("type") val type: Int, // 0:图片, 1:视频
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("url") val url: String
)
data class Hashtag(
    @SerializedName("start") val start: Int, // 起始下标
    @SerializedName("end") val end: Int      // 结束下标
)