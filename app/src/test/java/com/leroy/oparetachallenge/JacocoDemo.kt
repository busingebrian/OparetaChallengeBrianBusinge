package com.leroy.oparetachallenge

class JacocoDemo {
    fun isEven(number: Int): Boolean {
        return if(number % 2 == 0){
            println("Number is even")
            true
        } else {
            println("Number is odd")
            false
        }
    }

    fun printSomething() {
        println("foo")
    }
}

fun main() {
    val jacocoDemo = JacocoDemo()

    println(jacocoDemo.isEven(89))
    println(jacocoDemo.printSomething())
}