package com.example.android.architecture.blueprints.todoapp

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `testing run block`() {
        val x = kotlin.run {
            10
        }
        assertEquals(x,10)
    }

    @Test
    fun `test generic`() {
        1.event {

        }
    }
    private inline fun <T,R> T.event(block : T.()->R) : R {
        return block()
    }
}
