package com.adiliqbal.rxbus

class Optional<T> {

    private var value: T? = null

    val isPresent: Boolean
        get() = value != null

    private constructor() {
        this.value = null
    }

    private constructor(value: T) {
        this.value = value
    }

    fun get(): T? {
        return value
    }

    companion object {

        fun <T> empty(): Optional<T> {
            return Optional()
        }

        fun <T> of(value: T): Optional<T> {
            return Optional(value)
        }
    }
}
