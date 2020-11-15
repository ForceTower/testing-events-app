package dev.forcetower.events.core.validators

interface Validator<T> {
    fun isValid(value: T): Boolean
}