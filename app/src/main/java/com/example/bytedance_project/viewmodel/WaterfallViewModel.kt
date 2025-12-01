package com.example.bytedance_project.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bytedance_project.network.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.Request
import java.io.IOException

class WaterfallViewModel : ViewModel() {

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
                    // 获取响应体中的字符串 (JSON)
                    val responseData = response.body?.string()

                    if (responseData != null) {
                        Log.d("WaterfallRequest", "请求成功！服务器返回数据如下：")
                        Log.d("WaterfallRequest", responseData)
                    } else {
                        Log.w("WaterfallRequest", "请求成功，但返回体为空")
                    }
                } else {
                    Log.e("WaterfallRequest", "服务器返回错误: code=${response.code}")
                }

                // 记得关闭 response body 避免资源泄露
                response.close()

            } catch (e: IOException) {
                // 网络异常（断网、超时等）
                Log.e("WaterfallRequest", "网络请求异常: ${e.message}")
                e.printStackTrace()
            } catch (e: Exception) {
                // 其他未知异常
                Log.e("WaterfallRequest", "未知错误: ${e.message}")
            }
        }
    }
}