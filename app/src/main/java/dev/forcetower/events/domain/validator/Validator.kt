package dev.forcetower.events.domain.validator

interface Validator<T> {
    fun isValid(value: T): Boolean
}
