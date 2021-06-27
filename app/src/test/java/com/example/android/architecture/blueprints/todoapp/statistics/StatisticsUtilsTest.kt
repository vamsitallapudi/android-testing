package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.hamcrest.Matchers.`is`

class StatisticsUtilsTest {
    @Test
    fun `get active and completed stats with zero completion`() {
        val tasks = listOf(
            Task("Hello", "world", false)
        )
        val result = getActiveAndCompletedStats(tasks)
        assertThat(result.activeTasksPercent, `is`(100f))
        assertThat(result.completedTasksPercent, `is`(0f))
    }

    @Test
    fun getActiveAndCompletedStats_noActive_returnsZeroHundred() {
        val tasks = listOf(
            Task("H", "W", true)
        )
        val res = getActiveAndCompletedStats(tasks)
        assertThat(res.activeTasksPercent, `is`(0f))
        assertThat(res.completedTasksPercent, `is`(100f))
    }
    @Test
    fun getActiveAndCompletedStats_ThreeActiveTwoCompleted_returnsZeroHundred() {
        val tasks = listOf(
            Task("H", "W", true),
            Task("H", "W", true),
            Task("H", "W", false),
            Task("H", "W", false),
            Task("H", "W", false),
        )
        val res = getActiveAndCompletedStats(tasks)
        assertThat(res.activeTasksPercent, `is`(60f))
        assertThat(res.completedTasksPercent, `is`(40f))
    }

    @Test
    fun getActiveAndCompletedStats_emptyTasks_returnsZeroZero() {
        val result = getActiveAndCompletedStats(emptyList())
        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(0f))
    }
}