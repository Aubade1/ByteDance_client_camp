package com.example.bytedance_project
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bytedance_project.pageui.HomeScreen


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.Friends,
        Screen.Camera,
        Screen.Messages,
        Screen.Me
    )

    Scaffold(
        bottomBar = {
            NavigationBar (
                containerColor = MaterialTheme.colorScheme.background,
                tonalElevation = 0.dp
            ){
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    // 1. 判断当前是否为相机按钮
                    val isCamera = screen == Screen.Camera

                    // 2. 判断当前是否被选中
                    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                    NavigationBarItem(
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onBackground,
                            selectedTextColor = MaterialTheme.colorScheme.onBackground,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        ),
                        icon = {
                            if (isCamera) {
                                // 创建一个容器来画边框
                                Box(
                                    contentAlignment = Alignment.Center, // 让里面的加号居中
                                    modifier = Modifier
                                        .size(48.dp) // 整体大小
                                        .border(
                                            width = 2.dp, // 边框厚度
                                            color = MaterialTheme.colorScheme.onSurface, // 边框颜色
                                            shape = RoundedCornerShape(8.dp) // 圆角大小 (8.dp是圆角矩形，50.dp就是圆形)
                                        )
                                ) {
                                    // 里面的加号
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.size(32.dp) // 加号要比外面的框小一点
                                    )
                                }
                            }
                            else {
                                // 情况B：如果是其他，只显示文字
                                // 技巧：把 Text 放在 icon 的位置，这样它会垂直居中
                                Text(
                                    text = screen.title,
                                    // 选中变粗体，没选中正常
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        },
                        // 因为我们在 icon 参数里处理了所有显示内容，这里 label 设为 null
                        label = null,
                        // --- 核心修改结束 ---

                        selected = isSelected,
                        // 甚至可以去掉相机的指示器背景（可选）
                        // colors = NavigationBarItemDefaults.colors(indicatorColor = if (isCamera) Color.Transparent else MaterialTheme.colorScheme.secondaryContainer),

                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // --- 修改点 B：必须为新加的页面添加 composable 目标，否则点击会崩溃 ---
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }       // 替换为你真实的 HomeScreen()
            composable(Screen.Friends.route) { FriendsScreen() }    // 替换为你真实的
            composable(Screen.Camera.route) { CameraScreen() }     // 替换为你真实的
            composable(Screen.Messages.route) { MessagesScreen() }   // 替换为你真实的
            composable(Screen.Me.route) { MeScreen()  }     // 替换为你真实的
        }
    }
}

@Composable
fun FriendsScreen() {
}

@Composable
fun CameraScreen() {
}

@Composable
fun MessagesScreen() {
}

@Composable
fun MeScreen() {
}
