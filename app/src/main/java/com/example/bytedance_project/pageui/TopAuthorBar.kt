package com.example.bytedance_project.pageui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bytedance_project.model.Author
import com.example.bytedance_project.utils.FollowManager

@Composable
fun TopAuthorBar(
    author: Author?,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    // 获取当前关注状态 (如果 author 为 null，默认 false)
    val authorId = author?.userId ?: ""
    var isFollowing by remember { mutableStateOf(FollowManager.isFollowing(context, authorId)) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp) // 标准顶部栏高度
            .background(MaterialTheme.colorScheme.background) // 跟随主题背景
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. 返回按钮
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        // 2. 作者头像
        AsyncImage(
            model = author?.avatar,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))

        // 3. 作者昵称 (使用 weight 占据中间剩余空间)
        Text(
            text = author?.nickname ?: "未知用户",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )

        // 4. 关注按钮
        // 根据关注状态改变样式：
        // 未关注：红色/主色填充，显示"关注"
        // 已关注：灰色/次级背景，显示"已关注"
        Button(
            onClick = {
                if (authorId.isNotEmpty()) {
                    isFollowing = FollowManager.toggleFollow(context, authorId)
                }
            },
            modifier = Modifier
                .height(32.dp) // 按钮不要太高
                .width(72.dp), // 固定宽度防止文字变化导致抖动
            contentPadding = PaddingValues(0.dp), // 紧凑布局
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFollowing)
                    MaterialTheme.colorScheme.secondaryContainer // 已关注(灰/淡色)
                else
                    MaterialTheme.colorScheme.primary, // 未关注(醒目色)

                contentColor = if (isFollowing)
                    MaterialTheme.colorScheme.onSecondaryContainer
                else
                    MaterialTheme.colorScheme.onPrimary
            ),
            elevation = null // 去掉阴影更扁平
        ) {
            Text(
                text = if (isFollowing) "已关注" else "关注",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}