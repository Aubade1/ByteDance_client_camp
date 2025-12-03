package com.example.bytedance_project.viewmodel

data class WaterfallUiState(
    val isLoading: Boolean = false,      // 首屏加载中
    val isRefreshing: Boolean = false,   // 下拉刷新中
    val isLoadingMore: Boolean = false,  // 上滑加载更多中
    val isError: Boolean = false,        // 请求失败
    val errorMessage: String = "",       // 失败原因
    val hasMore: Boolean = true          // 是否还有更多数据
)
