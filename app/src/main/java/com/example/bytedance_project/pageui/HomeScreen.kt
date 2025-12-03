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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bytedance_project.viewmodel.WaterfallViewModel
import androidx.compose.runtime.LaunchedEffect
import com.example.bytedance_project.model.Post
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.example.bytedance_project.utils.LikeManager
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
@OptIn(ExperimentalMaterial3Api::class) // PullToRefresh 需要这个注解
@Composable
fun CommunityPage(
    viewModel: WaterfallViewModel = viewModel()
) {
    val uiState = viewModel.uiState
    val scrollState = rememberLazyStaggeredGridState()

    // --- 2. 上滑加载更多检测 ---
    // derivedStateOf 保证只在状态变化时计算，优化性能
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = scrollState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            if (totalItems == 0) return@derivedStateOf false

            // 获取可见的最后一项的索引
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            // 如果看到了倒数第2个item，且没在加载，且还有更多 -> 触发加载
            lastVisibleItemIndex >= totalItems - 2
        }
    }
    // LaunchedEffect(Unit) 这里的代码只会在 Composable 第一次显示时运行一次
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadMoreFeed()
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            // A. 首刷 Loading (页面中心转圈)
            uiState.isLoading && viewModel.postList.isEmpty() -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            // B. 首刷失败 (显示错误页)
            uiState.isError && viewModel.postList.isEmpty() -> {
                ErrorStatePage(
                    message = uiState.errorMessage,
                    onRetry = { viewModel.refreshFeed(isFirstLoad = true) }
                )
            }

            // C. 正常列表显示 (使用 PullToRefreshBox)
            else -> {
                // --- 核心修改：使用 PullToRefreshBox ---
                PullToRefreshBox(
                    isRefreshing = uiState.isRefreshing, // 直接绑定布尔值
                    onRefresh = {
                        viewModel.refreshFeed(isFirstLoad = false) // 绑定刷新事件
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 列表放在这里面
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        state = scrollState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalItemSpacing = 8.dp
                    ) {
                        items(viewModel.postList) { post ->
                            WaterfallItemCard(post)
                        }

                        // 底部 Loading 条 / 没有更多提示
                        if (viewModel.postList.isNotEmpty()) {
                            item(span = StaggeredGridItemSpan.FullLine) {
                                if (uiState.isLoadingMore) {
                                    LoadingFooter()
                                } else if (!uiState.hasMore) {
                                    NoMoreDataFooter()
                                } else {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
        // D. 加载更多失败的 Toast 提示 (可选)
        // 如果列表有数据，但是加载更多失败了，弹个 Snackbar 或者 Toast
        if (uiState.isError && viewModel.postList.isNotEmpty()) {
            // 这里简单用 Text 模拟 Toast
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "加载更多失败，请重试",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

// 4. 定义单个卡片的样式
@Composable
fun WaterfallItemCard(post: Post) {
    val context = LocalContext.current

    // --- 1. 处理点赞状态 (本地持久化) ---
    // 使用 remember 记录当前状态，初始值从 LikeManager 读取
    var isLiked by remember { mutableStateOf(LikeManager.isLiked(context, post.postId)) }
    // 模拟点赞数（因为接口没给）：如果本地点赞了就+1，否则显示一个随机基数
    val baseLikeCount = remember { (10..999).random() }
    val displayLikeCount = if (isLiked) baseLikeCount + 1 else baseLikeCount

    // --- 2. 处理图片比例 ---
    val firstClip = post.clips?.firstOrNull()
    val rawRatio = if (firstClip != null && firstClip.height > 0) {
        firstClip.width.toFloat() / firstClip.height.toFloat()
    } else {
        1f // 默认正方形
    }
    // 限制比例在 3:4 (0.75) 到 4:3 (1.33) 之间
    val aspectRatio = rawRatio.coerceIn(0.75f, 1.33f)

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            // --- 作品封面 ---
            val imageUrl = firstClip?.url
            if (imageUrl != null){
            AsyncImage(
                model = firstClip?.url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio) // 应用计算好的比例
                    .background(Color.LightGray) // 加载时的占位色
            )}
            else {
                // 没地址，显示一个带文字的色块
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.0f) // 默认正方形
                        .background(Color(0xFFEEEEEE)), // 浅灰色背景
                    contentAlignment = Alignment.Center
                ) {
                    Text("暂无图片", color = Color.Gray, fontSize = 12.sp)
                }
            }

            Column(modifier = Modifier.padding(8.dp)) {
                // --- 作品标题 (优先展示标题，没有则展示内容) ---
                val displayText = if (!post.title.isNullOrEmpty()) post.title else post.content ?: ""
                Text(
                    text = displayText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                // --- 底栏：头像昵称 + 点赞 ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 作者头像
                    AsyncImage(
                        model = post.author?.avatar,
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    // 作者昵称
                    Text(
                        text = post.author?.nickname ?: "用户",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f) // 占据剩余空间，把点赞挤到右边
                    )

                    // 点赞区域
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            // 点击切换状态并保存到本地
                            isLiked = LikeManager.toggleLike(context, post.postId)
                        }
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (isLiked) Color.Red else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = displayLikeCount.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}