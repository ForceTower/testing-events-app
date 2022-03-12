package dev.forcetower.events.tooling

interface MockFactory<T> {
    fun create(): T
    fun createList(size: Int = 10): List<T> {
        return (0..size).map { create() }
    }
}