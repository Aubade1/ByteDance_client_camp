import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    // 1. 首页
    object Home : Screen("home", "首页", Icons.Filled.Home)

    // 2. 朋友 (这里用了 Face 图标代替，你可以换成 People)
    object Friends : Screen("friends", "朋友", Icons.Filled.Face)

    // 3. 相机 (+) (这里用了 AddCircle 代表加号)
    object Camera : Screen("camera", "相机", Icons.Filled.AddCircle)

    // 4. 消息
    object Messages : Screen("messages", "消息", Icons.Filled.Email)

    // 5. 我
    object Me : Screen("me", "我", Icons.Filled.Person)
    object Detail : Screen("detail/{postJson}", "详情", Icons.Filled.Home)

    // 辅助方法：生成带参数的路由地址
    fun createRoute(postJson: String): String {
        return "detail/$postJson"
    }
}