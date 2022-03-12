package dev.forcetower.events.domain.validator

import android.util.Patterns

class AndroidEmailValidator : Validator<String> {
    override fun isValid(value: String) = Patterns.EMAIL_ADDRESS.matcher(value).matches()
}