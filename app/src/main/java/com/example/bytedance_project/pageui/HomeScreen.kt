package com.example.bytedance_project.pageui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import kotlin.random.Random
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bytedance_project.viewmodel.WaterfallViewModel
import androidx.compose.runtime.LaunchedEffect

data class WaterfallItem(
    val id: Int,
    val title: String,
    val height: Dp, // 关键：每个 Item 的高度不同
    val color: Color
)
@Composable
fun HomeScreen() {
    // 1. 定义 Tabs 数据
    val tabs = listOf("北京", "团购", "关注", "社区", "推荐", "搜索")

    // 2. 初始化 Pager
    // initialPage = 4 表示默认显示 "推荐" (下标从0开始)
    val pagerState = rememberPagerState(
        initialPage = 3,
        pageCount = { tabs.size }
    )
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()
                              .background(MaterialTheme.colorScheme.background) ) {

        // 3. 修改为 TabRow (固定宽度，均匀分布)
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            // 设置指示器颜色和位置
            indicator = {},
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                // 判断当前是否是“搜索”项
                val isSearchTab = (title == "搜索")
                // 判断是否被选中
                val isSelected = pagerState.currentPage == index
                val selectedColor = MaterialTheme.colorScheme.onBackground // 动态获取黑/白
                val unselectedColor = Color.Gray // 灰色在黑/白背景都通用
                Tab(
                    selected = isSelected,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    // --- 核心修改：根据是否是搜索页，分别设置 text 或 icon ---
                    // 如果是搜索，text传null，icon传组件
                    // 如果不是搜索，text传组件，icon传null
                    text = if (isSearchTab) null else {
                        {
                            Text(
                                text = title,
                                // 选中时加粗，未选中正常
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                // 稍微调整字体大小以适应一行6个
                                color = if (isSelected) selectedColor else unselectedColor,
                                fontSize = 13.sp
                            )
                        }
                    },
                    icon = if (isSearchTab) {
                        {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "搜索",
                                tint = if (isSelected) selectedColor else unselectedColor,
                                modifier = Modifier.size(24.dp) // 控制图标大小

                            )
                        }
                    } else null,

                    // 稍微减少 Tab 内部的上下间距，让布局紧凑一点
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        // 4. 内容区域
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
                               .background(MaterialTheme.colorScheme.background)
        ) { page ->
            when (page) {
                0 -> BeijingPage()
                1 -> TuangouPage()
                2 -> SubscribePage()
                3 -> CommunityPage()
                4 -> RecommendPage()
                5 -> SearchPage() // 这里对应搜索页的内容
                else -> Text("未知页面")
            }
        }
    }
}

// --- 占位页面 ---

@Composable
fun SearchPage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("搜索功能页面")
    }
}

@Composable
fun TuangouPage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("团购页面")
    }
}

@Composable
fun SubscribePage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("关注页面")
    }
}

@Composable
fun BeijingPage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("北京本地页面")
    }
}

@Composable
fun RecommendPage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("这里是【推荐】的内容列表")
    }
}
@Composable
fun CommunityPage(
    viewModel: WaterfallViewModel = viewModel()
) {
    // 2. 模拟生成 50 条数据，高度随机 (100dp 到 300dp 之间)
    // LaunchedEffect(Unit) 这里的代码只会在 Composable 第一次显示时运行一次
    LaunchedEffect(Unit) {
        viewModel.fetchFeed()
    }
    val items = remember {
        List(50) { index ->
            WaterfallItem(
                id = index,
                title = "帖子标题 $index \n这就双列瀑布流效果",
                height = Random.nextInt(150, 300).dp, // 随机高度
                color = Color(
                    Random.nextInt(256),
                    Random.nextInt(256),
                    Random.nextInt(256),
                    255
                )
            )
        }
    }

    // 3. 使用 LazyVerticalStaggeredGrid
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2), // 固定 2 列
        modifier = Modifier.fillMaxSize(),

        // 设置内容边距
        contentPadding = PaddingValues(8.dp),

        // 设置 Item 之间的垂直间距
        verticalItemSpacing = 8.dp,

        // 设置 Item 之间的水平间距
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            WaterfallItemCard(item)
        }
    }
}

// 4. 定义单个卡片的样式
@Composable
fun WaterfallItemCard(item: WaterfallItem) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            // 这里通常放图片，现在用彩色 Box 代替
            // 实际开发中请使用 Coil 加载网络图片: AsyncImage(...)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(item.height) // 使用数据中定义的高度
                    .background(item.color)
            )

            // 下面的文字区域
            Text(
                text = item.title,
                fontSize = 14.sp,
                modifier = Modifier.padding(8.dp),
                maxLines = 2,
                lineHeight = 18.sp
            )
        }
    }
}