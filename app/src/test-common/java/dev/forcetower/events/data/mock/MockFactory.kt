package dev.forcetower.events.data.mock

interface MockFactory<T> {
    fun create(): T
    fun createList(size: Int = 10): List<T> {
        return (1..size).map { create() }
    }
}
