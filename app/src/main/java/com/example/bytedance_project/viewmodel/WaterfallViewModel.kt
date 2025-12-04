package com.example.bytedance_project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    // UI 状态 (使用 Compose State)
    var uiState by mutableStateOf(WaterfallUiState())
        private set

    // 初始化
    init {
        refreshFeed(isFirstLoad = true)
    }
    fun refreshFeed(isFirstLoad: Boolean = false) {
        if (uiState.isLoading || uiState.isRefreshing) return

        uiState = uiState.copy(
            isLoading = isFirstLoad,
            isRefreshing = !isFirstLoad,
            isError = false
        )

        fetchData(isRefresh = true)
    }
    fun loadMoreFeed() {
        if (uiState.isLoadingMore || !uiState.hasMore || uiState.isRefreshing || uiState.isLoading) return

        uiState = uiState.copy(isLoadingMore = true)
        fetchData(isRefresh = false)
    }
    private fun fetchData(isRefresh: Boolean) {
        // 使用协程在 IO 线程执行网络请求，避免阻塞主线程（导致 App 卡死）
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("WaterfallRequest", "开始构建 OkHttp 请求...")
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
                        val gson = Gson()
                        val feedResponse = gson.fromJson(jsonString, FeedResponse::class.java)

                        if (feedResponse.statusCode == 0) {
                            viewModelScope.launch(Dispatchers.Main) {
                                if (isRefresh) {
                                    postList.clear()
                                }
                                // 过滤掉重复数据 (可选，防止服务器返回重复ID导致Crash)
                                val newPosts = feedResponse.postList.filter { newPost ->
                                    // 条件 1: 必须去重
                                    val isNotDuplicate = postList.none { it.postId == newPost.postId }

                                    // 条件 2: 必须包含图片 (clips 不为 null 且 不为空)
                                    val hasImages = !newPost.clips.isNullOrEmpty()

                                    // 同时满足才保留
                                    isNotDuplicate && hasImages
                                }
                                postList.addAll(newPosts)
                                uiState = uiState.copy(
                                    isLoading = false,
                                    isRefreshing = false,
                                    isLoadingMore = false,
                                    isError = false,
                                    hasMore = feedResponse.hasMore == 1 // 假设 1 代表有更多
                                )
                            }
                        }else {
                            handleError("服务器状态码异常: ${feedResponse.statusCode}")
                        }
                    } else{
                        handleError("网络请求失败 code: ${response.code}")
                    }
                }
            } catch (e: Exception) {
                handleError("发生错误: ${e.message}")
            }
        }
    }
    private fun handleError(msg: String) {
        viewModelScope.launch(Dispatchers.Main) {
            uiState = uiState.copy(
                isLoading = false,
                isRefreshing = false,
                isLoadingMore = false,
                isError = true, // 标记为失败
                errorMessage = msg
            )
        }
    }
}