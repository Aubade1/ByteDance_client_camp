package com.example.bytedance_project.pageui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.bytedance_project.model.Post

@Composable
fun DetailImageSlider(
    post: Post,
    pagerState: PagerState // 把 PagerState 传进来，因为后面做"进度条"要用它
) {
    val clips = post.clips ?: emptyList()

    // --- 1. 计算容器比例 (核心逻辑) ---
    val firstClip = clips.firstOrNull()
    val rawRatio = if (firstClip != null && firstClip.height > 0) {
        firstClip.width.toFloat() / firstClip.height.toFloat()
    } else {
        16f / 9f // 默认兜底比例
    }

    // 限制比例在 3:4 (0.75) 到 16:9 (1.77) 之间
    val targetRatio = rawRatio.coerceIn(0.75f, 16f / 9f)

    // --- 2. 横滑容器 ---
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(targetRatio) // 强制容器应用计算好的比例
            .background(Color.Black) // 图片未加载前显示黑色背景，更有质感
    ) { page ->
        val clip = clips.getOrNull(page)

        if (clip != null) {
            // --- 3. 支持状态管理的图片组件 ---
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(clip.url)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop, // 核心：非首图按照容器比例裁切，充满容器
                modifier = Modifier.fillMaxSize()
            ) {
                // 根据 Coil 的加载状态渲染不同的 UI
                val state = painter.state
                when (state) {
                    // A. 加载中
                    is AsyncImagePainter.State.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(32.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                    // B. 加载失败
                    is AsyncImagePainter.State.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.BrokenImage,
                                    contentDescription = "Error",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text("加载失败", color = Color.Gray)
                            }
                        }
                    }
                    // C. 加载成功 / 默认
                    else -> {
                        SubcomposeAsyncImageContent()
                    }
                }
            }
        }
    }
}