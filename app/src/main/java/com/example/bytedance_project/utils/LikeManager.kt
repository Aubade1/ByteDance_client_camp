package com.example.bytedance_project.utils

import android.content.Context
import android.content.SharedPreferences

object LikeManager {
    private const val PREF_NAME = "like_status_pref"

    private fun getPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // 检查是否点赞
    fun isLiked(context: Context, postId: String): Boolean {
        return getPref(context).getBoolean(postId, false)
    }

    // 切换点赞状态
    fun toggleLike(context: Context, postId: String): Boolean {
        val pref = getPref(context)
        val current = pref.getBoolean(postId, false)
        val newStatus = !current
        pref.edit().putBoolean(postId, newStatus).apply()
        return newStatus
    }
}