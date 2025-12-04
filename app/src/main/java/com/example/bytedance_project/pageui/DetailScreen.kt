package com.example.bytedance_project.pageui

import com.example.bytedance_project.model.Post
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.bytedance_project.utils.DateUtils


@Composable
fun DetailScreen(
    post: Post, // 详情页必须接收一个 Post 数据对象
    onBack: () -> Unit // 必须接收一个返回的回调
) {
    // 使用 Scaffold 布局，把 TopAuthorBar 固定在顶部
    val pageCount = post.clips?.size ?: 0
    val pagerState = rememberPagerState(pageCount = { pageCount })

    // 整个页面内容可能很长，需要支持垂直滚动
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAuthorBar(
                author = post.author,
                onBackClick = onBack
            )
        },
        bottomBar = {
            BottomInteractionBar(post = post)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState) // 让整个详情页内容可以上下滑动
        ) {

            // --- 插入横滑容器 ---
            if (pageCount > 0) {
                DetailImageSlider(
                    post = post,
                    pagerState = pagerState
                )
            } else {
                // 如果没有图片，显示占位或直接不显示，防止Crash
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("暂无图片")
                }
            }
            DetailImageIndicator(
                pagerState = pagerState,
                pageCount = pageCount
            )

            // --- 3. 标题、正文、日期 (后续实现) ---
            // 为了看清进度条，先给个临时的间距
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // 3.1 标题
                if (!post.title.isNullOrEmpty()) {
                    Text(
                        text = post.title,
                        style = MaterialTheme.typography.titleLarge, // 大号字体
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // 3.2 正文 (带高亮话题)
                PostContent(post = post)

                // 3.3 发布日期 (后续实现)
                Spacer(modifier = Modifier.height(8.dp))
                // Text("发布于 2025-xx-xx", ...)
                Text(
                    // 调用工具类进行格式化
                    text = DateUtils.formatDouyinDate(post.createTime),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray, // 使用灰色，弱化显示
                    fontSize = 12.sp
                )
            }

            // 底部留白，防止被导航栏遮挡
            Spacer(modifier = Modifier.height(40.dp))

            Box(modifier = Modifier.height(500.dp))
        }
    }
}
