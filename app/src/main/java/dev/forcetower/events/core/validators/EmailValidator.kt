package dev.forcetower.events.core.validators

import android.util.Patterns

// Patterns.EMAIL_ADDRESS is not available for testing : ^)
class EmailValidator : Validator<String> {
    override fun isValid(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }
}
