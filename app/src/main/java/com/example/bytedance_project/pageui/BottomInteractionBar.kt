package com.example.bytedance_project.pageui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bytedance_project.model.Post
import com.example.bytedance_project.utils.LikeManager

@Composable
fun BottomInteractionBar(post: Post) {
    val context = LocalContext.current

    var isLiked by remember { mutableStateOf(LikeManager.isLiked(context, post.postId)) }
    val likeCount = if (isLiked) post.mockLikeCount + 1 else post.mockLikeCount
    val commentCount = 28

    // 底部容器：带上方分割线和背景
    Column {
        // 分割线
        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background) // 跟随主题
                .padding(horizontal = 12.dp, vertical = 8.dp), // 内部间距
            verticalAlignment = Alignment.CenterVertically
        ) {

            // --- 2. 快捷评论框 ---
            // 使用 Surface 模拟一个圆角输入框的样式
            Surface(
                modifier = Modifier
                    .weight(1f) // 占据左侧剩余空间
                    .height(36.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .clickable { /* 点击跳转到评论区 (暂不实现) */ },
                color = MaterialTheme.colorScheme.surfaceVariant // 浅灰色背景
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Comment, // 编辑图标或笔
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "评论一下", // 抖音经典提示语
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // --- 3. 图标交互区 (右侧) ---

            // A. 点赞 (支持交互)
            InteractionIcon(
                icon = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                text = if (isLiked) likeCount.toString() else "赞",
                tint = if (isLiked) Color.Red else MaterialTheme.colorScheme.onBackground,
                onClick = {
                    // 写入本地存储，并更新 UI
                    isLiked = LikeManager.toggleLike(context, post.postId)
                }
            )

            // B. 评论 (无交互)
            InteractionIcon(
                icon = Icons.AutoMirrored.Outlined.Comment,
                text = commentCount.toString(),
                onClick = {}
            )

            // C. 收藏 (无交互)
            InteractionIcon(
                icon = Icons.Outlined.BookmarkBorder, // 或者 Star
                text = "收藏",
                onClick = {}
            )

            // D. 分享 (支持系统分享交互)
            InteractionIcon(
                icon = Icons.Outlined.Share,
                text = "分享",
                onClick = {
                    sharePostSystem(context, post)
                }
            )
        }
    }
}

/**
 * 封装一个通用的图标按钮组件，保持代码整洁
 */
@Composable
fun InteractionIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    tint: Color = MaterialTheme.colorScheme.onBackground,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clickable(onClick = onClick) // 点击区域包含图标和文字
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(26.dp),
            tint = tint
        )
        // 图标下面的小字
        Text(
            text = text,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * 调用系统原生分享面板
 */
fun sharePostSystem(context: Context, post: Post) {
    val shareText = "快来看看这个作品：${post.title ?: post.content}\n链接：https://douyin.com/video/${post.postId}"

    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, "分享到")
    context.startActivity(shareIntent)
}