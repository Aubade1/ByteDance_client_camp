package com.example.bytedance_project.pageui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DetailImageIndicator(
    pagerState: PagerState,
    pageCount: Int
) {
    // 规则 1: 单图不展示
    if (pageCount <= 1) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp) // 这里的 padding 决定了进度条距离屏幕边缘的距离
            .height(4.dp), // 进度条整体高度
        horizontalArrangement = Arrangement.spacedBy(4.dp) // 规则 2: 条状间隔 (分段效果)
    ) {
        repeat(pageCount) { index ->
            // 判断当前是否是活动页
            val isActive = pagerState.currentPage == index

            // 规则 3: 跟随图片切换 (使用动画过渡颜色，体验更丝滑)
            val color by animateColorAsState(
                targetValue = if (isActive)
                    MaterialTheme.colorScheme.primary // 选中色 (跟随主题色)
                else
                    Color.LightGray.copy(alpha = 0.5f), // 未选中色 (浅灰)
                label = "indicator_color"
            )

            Box(
                modifier = Modifier
                    .weight(1f) // 关键点：让所有条目平分宽度 (形成条状而非点状)
                    .fillMaxHeight()
                    .background(color, RoundedCornerShape(2.dp)) // 圆角
            )
        }
    }
}