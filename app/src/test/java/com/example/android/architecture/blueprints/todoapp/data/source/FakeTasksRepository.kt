package com.example.android.architecture.blueprints.todoapp.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Result.Success
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.runBlocking

class FakeTasksRepository :  TasksRepository{
    var tasksMap :LinkedHashMap<String, Task> = LinkedHashMap()
    private val observableTasks = MutableLiveData<Result<List<Task>>>()
    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        return Success(tasksMap.values.toList())
    }

    override suspend fun refreshTasks() {
        observableTasks.value = getTasks()
    }

    override fun observeTasks(): LiveData<Result<List<Task>>> {
//        these functions can be run individually, hence calling
//        refreshtasks to load the list into observableTasks
        runBlocking {
            refreshTasks()
        }
        return observableTasks
    }

    override suspend fun refreshTask(taskId: String) {
        refreshTasks()
    }

    override suspend fun completeTask(task: Task) {
        val completedTask = task.copy(isCompleted = true)
        tasksMap[task.id] = completedTask
        refreshTasks()
    }

    fun addTasks(vararg tasks: Task) {
        for(task in tasks) {
            tasksMap[task.id] = task
        }
        runBlocking {
            refreshTasks()
        }
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun saveTask(task: Task) {
        TODO("Not yet implemented")
    }


    override suspend fun completeTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearCompletedTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }
}