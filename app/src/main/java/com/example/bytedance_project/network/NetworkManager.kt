package com.example.bytedance_project.network

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object NetworkManager {
    // 创建一个全局唯一的 OkHttpClient 实例
    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS) // 连接超时
            .readTimeout(10, TimeUnit.SECONDS)    // 读取超时
            .writeTimeout(10, TimeUnit.SECONDS)   // 写入超时
            .build()
    }
}