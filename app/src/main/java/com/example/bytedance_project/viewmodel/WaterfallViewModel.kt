package com.example.bytedance_project.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bytedance_project.model.FeedResponse
import com.example.bytedance_project.model.Post
import com.example.bytedance_project.network.NetworkManager
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.Request
import java.io.IOException

class WaterfallViewModel : ViewModel() {
    val postList = mutableStateListOf<Post>()
    // 核心方法：去服务器拉取数据
    fun fetchFeed() {
        // 使用协程在 IO 线程执行网络请求，避免阻塞主线程（导致 App 卡死）
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("WaterfallRequest", "开始构建 OkHttp 请求...")

                // 1. 构建 URL 和参数
                // 目标地址: https://college-training-camp.bytedance.com/feed/
                // 参数: count=10, accept_video_clip=false
                val urlBuilder = HttpUrl.Builder()
                    .scheme("https")
                    .host("college-training-camp.bytedance.com")
                    .addPathSegment("feed")
                    .addPathSegment("") // 确保末尾有个 /，即 feed/
                    .addQueryParameter("count", "10")
                    .addQueryParameter("accept_video_clip", "false")
                    .build()

                // 2. 创建 Request 对象 (GET 请求)
                val request = Request.Builder()
                    .url(urlBuilder)
                    .get() // 显式声明是 GET 请求
                    .build()

                // 3. 执行请求 (同步 execute，因为我们已经在 Dispatchers.IO 协程里了)
                val response = NetworkManager.client.newCall(request).execute()

                // 4. 处理结果
                if (response.isSuccessful) {
                    val jsonString = response.body?.string()

                    if (jsonString != null) {
                        // --- 核心修改：解析 JSON ---
                        Log.d("WaterfallRequest", jsonString)
                        val gson = Gson()
                        val feedResponse = gson.fromJson(jsonString, FeedResponse::class.java)

                        if (feedResponse.statusCode == 0) {
                            // 切换回主线程更新 UI 数据
                            viewModelScope.launch(Dispatchers.Main) {
                                postList.clear() // 如果是刷新则清空，如果是加载更多则 addAll
                                postList.addAll(feedResponse.postList)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("WaterfallViewModel", "Error: ${e.message}")
            }
        }
    }
}