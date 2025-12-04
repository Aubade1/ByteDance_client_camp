package com.example.bytedance_project.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    /**
     * 将秒级时间戳格式化为抖音风格的时间字符串
     * @param timestampSeconds 服务器返回的秒级时间戳
     */
    fun formatDouyinDate(timestampSeconds: Long): String {
        val now = System.currentTimeMillis()
        val timeInMillis = timestampSeconds * 1000L // 转换为毫秒
        val date = Date(timeInMillis)

        // 准备日历对象
        val currentCalendar = Calendar.getInstance()
        val targetCalendar = Calendar.getInstance()
        targetCalendar.time = date

        // 获取年、天
        val currentYear = currentCalendar.get(Calendar.YEAR)
        val targetYear = targetCalendar.get(Calendar.YEAR)
        val currentDay = currentCalendar.get(Calendar.DAY_OF_YEAR)
        val targetDay = targetCalendar.get(Calendar.DAY_OF_YEAR)

        // 计算相差天数 (简单计算，忽略跨年时的复杂天数计算，针对近期时间足够)
        // 更严谨的做法是用 timeInMillis 相减除以一天的毫秒数
        val diffMillis = now - timeInMillis
        val diffDays = diffMillis / (24 * 60 * 60 * 1000)

        // 格式化器
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateFormat = SimpleDateFormat("MM-dd", Locale.getDefault())

        return when {
            // 1.如果是同一年
            currentYear == targetYear -> {
                when (currentDay - targetDay) {
                    0 -> {
                        // 今天：显示 HH:mm
                        timeFormat.format(date)
                    }
                    1 -> {
                        // 昨天：显示 昨天 HH:mm
                        "昨天 ${timeFormat.format(date)}"
                    }
                    else -> {
                        // 今天和昨天之外
                        if (diffDays < 7) {
                            // 7天内：显示 X天前
                            // fix: 假如 diffDays 计算出是 0 (比如相差20小时但是跨天了)，由上面的 case 1 处理了
                            // 这里处理至少是 2 天前的情况
                            "${diffDays}天前"
                        } else {
                            // 超过7天：显示 MM-dd
                            dateFormat.format(date)
                        }
                    }
                }
            }
            // 2. 跨年了 (肯定超过7天)
            else -> {
                // 可以显示 yyyy-MM-dd，或者题目要求的 MM-dd
                dateFormat.format(date)
            }
        }
    }
}