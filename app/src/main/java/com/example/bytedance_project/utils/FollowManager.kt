package com.example.bytedance_project.utils

import android.content.Context
import android.content.SharedPreferences

object FollowManager {
    private const val PREF_NAME = "follow_status_pref"

    private fun getPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // 检查是否已关注 (根据 authorId)
    fun isFollowing(context: Context, authorId: String): Boolean {
        return getPref(context).getBoolean(authorId, false)
    }

    // 切换关注状态，并返回新的状态
    fun toggleFollow(context: Context, authorId: String): Boolean {
        val pref = getPref(context)
        val current = pref.getBoolean(authorId, false)
        val newStatus = !current
        pref.edit().putBoolean(authorId, newStatus).apply()
        return newStatus
    }
}