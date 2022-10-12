package com.leroy.oparetachallenge

import org.junit.Assert.*
import org.junit.Test

class JacocoDemoTest{
    private val jacocoDemo = JacocoDemo()
    @Test
    fun testJacocoDemo(){
        assertTrue(jacocoDemo.isEven(10))
        assertFalse(jacocoDemo.isEven(7))
        assertTrue("foo", true)
        assertFalse("doo", false)
    }
}