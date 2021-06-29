package com.example.android.architecture.blueprints.todoapp.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Result.Loading
import com.example.android.architecture.blueprints.todoapp.data.Result.Error
import com.example.android.architecture.blueprints.todoapp.data.Result.Success
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import kotlin.reflect.KClass

class FakeAndroidTasksRepository : TasksRepository {
    var tasksMap: LinkedHashMap<String, Task> = LinkedHashMap()
    private var shouldReturnError = false
    private val observableTasks = MutableLiveData<Result<List<Task>>>()
    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        if(shouldReturnError)
            return Error(Exception("Test Error"))
        return Success(tasksMap.values.toList())
    }

    override suspend fun refreshTasks() {
        observableTasks.value = getTasks()
    }

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
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
        for (task in tasks) {
            tasksMap[task.id] = task
        }
        runBlocking {
            refreshTasks()
        }
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        runBlocking { refreshTasks() }
        return observableTasks.map { tasks ->
            when (tasks) {
                is Loading -> Loading
                is Error -> Error(tasks.exception)
                is Success -> Success(tasks.data.firstOrNull {
                    it.id == taskId
                } ?: return@map Error(Exception("Value not found"))
                )
            }
        }
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
        if (shouldReturnError) {
            return Error(Exception("Testing Exception"))
        }
        tasksMap[taskId]?.let {
            return Success(it)
        }
        return Error(Exception("No task found"))
    }

    override suspend fun saveTask(task: Task) {
        tasksMap[task.id] = task
        refreshTasks()
    }


    override suspend fun completeTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(task: Task) {
        val activatedTask = task.copy(isCompleted = false)
        tasksMap[activatedTask.id] = activatedTask
        refreshTasks()
    }

    override suspend fun activateTask(taskId: String) {
        tasksMap[taskId]?.let {
            it.isCompleted = false
        }
        refreshTasks()
    }

    private fun <T:Any> doSomeThing(input: T, type : KClass<T>) {
        println("$input is the input with type: ${type.simpleName} ")
    }

    override suspend fun clearCompletedTasks() {
        tasksMap = tasksMap.filterValues { !it.isCompleted } as LinkedHashMap<String, Task>
    }

    override suspend fun deleteAllTasks() {
        tasksMap.clear()
        refreshTasks()
    }

    override suspend fun deleteTask(taskId: String) {
        tasksMap.remove(taskId)
        refreshTasks()
    }
}