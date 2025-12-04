package com.example.bytedance_project

import android.net.Uri
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bytedance_project.model.Post
import com.example.bytedance_project.pageui.DetailScreen
import com.google.gson.Gson

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val gson = remember { Gson() }

    NavHost(navController = navController, startDestination = "main_entry") {

        // 1. 主界面 (包含底部导航栏的那个大容器)
        composable("main_entry") {
            MainScreen(
                // 这里的 navigateToDetail 是为了让 MainScreen 里的点击事件能传出来
                navigateToDetail = { post ->
                    val json = gson.toJson(post)
                    val encodedJson = Uri.encode(json)
                    navController.navigate(Screen.Detail.createRoute(encodedJson))
                }
            )
        }

        // 2. 详情页 (全屏，覆盖在 MainScreen 之上)
        composable(
            route = Screen.Detail.route,
            // 这里还可以添加 enterTransition 来实现从右侧滑入的动画
            enterTransition = {
                slideInHorizontally{it}
            }
        ) { backStackEntry ->
            val postJson = backStackEntry.arguments?.getString("postJson")
            if (postJson != null) {
                val post = gson.fromJson(postJson, Post::class.java)
                DetailScreen(
                    post = post,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}