package com.lenguyenthanh.rxarrow

open class Animal(val name: String)

class Duck(name: String) : Animal(name) {
    fun quark(): String {
        return "quark"
    }
}

class Tiger(name: String) : Animal(name) {
    fun roar(): String {
        return "roar"
    }
}

sealed class Failure {
    object ABadFailure : Failure()
    data class FailureWithReason(val reason: String): Failure()
}
