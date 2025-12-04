package com.example.bytedance_project.pageui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.example.bytedance_project.model.Post
// 1. 错误/空态页面
@Composable
fun ErrorStatePage(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "加载失败: $message", color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("点击重试")
        }
    }
}

// 2. 底部加载更多 Loading
@Composable
fun LoadingFooter() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            strokeWidth = 2.dp
        )
    }
}

// 3. 到底了提示
@Composable
fun NoMoreDataFooter() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("没有更多数据了", color = androidx.compose.ui.graphics.Color.Gray)
    }
}
@Composable
fun PostContent(post: Post) {
    val context = LocalContext.current
    val content = post.content ?: ""
    val hashtags = post.hashtags ?: emptyList()

    // 1. 构建带属性的字符串
    val annotatedString = remember(content, hashtags) {
        buildAnnotatedString {
            // 先添加全部文本
            append(content)

            // 遍历所有话题，添加高亮和点击事件
            hashtags.forEach { tag ->
                // 确保下标不越界，防止服务端数据错误导致 Crash
                if (tag.start >= 0 && tag.end <= content.length && tag.start < tag.end) {

                    // A. 添加高亮样式 (蓝色 + 加粗)
                    addStyle(
                        style = SpanStyle(
                            color = Color(0xFF2B5AE9), // 抖音蓝/话题蓝
                            fontWeight = FontWeight.Bold
                        ),
                        start = tag.start,
                        end = tag.end
                    )

                    // B. 添加点击标记 (Annotation)
                    // 我们把话题的内容作为 tag 存进去，方便点击时取出
                    val tagName = content.substring(tag.start, tag.end)
                    addStringAnnotation(
                        tag = "HASHTAG", // 标记类型
                        annotation = tagName, // 标记内容
                        start = tag.start,
                        end = tag.end
                    )
                }
            }
        }
    }

    // 2. 渲染可点击文本
    ClickableText(
        text = annotatedString,
        style = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onBackground, // 普通文字颜色
            fontSize = 16.sp,
            lineHeight = 24.sp // 增加行高，提升阅读体验
        ),
        // 要求：完整展示不截断
        overflow = TextOverflow.Clip,
        softWrap = true,
        modifier = Modifier.fillMaxWidth(),
        onClick = { offset ->
            // 3. 处理点击事件
            // 检查点击的位置是否有 "HASHTAG" 标记
            val annotations = annotatedString.getStringAnnotations(
                tag = "HASHTAG",
                start = offset,
                end = offset
            )

            // 如果找到了标记，说明点击的是话题
            annotations.firstOrNull()?.let { annotation ->
                // annotation.item 就是我们刚才存进去的话题内容 (例如 "#快来参与")
                Toast.makeText(context, "点击了话题: ${annotation.item}", Toast.LENGTH_SHORT).show()
                // 这里可以执行跳转到话题详情页的逻辑
            }
        }
    )
}