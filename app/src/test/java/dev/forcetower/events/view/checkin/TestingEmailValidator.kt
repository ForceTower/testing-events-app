package dev.forcetower.events.view.checkin

import dev.forcetower.events.core.validators.Validator
import java.util.regex.Pattern

class TestingEmailValidator : Validator<String> {
    override fun isValid(value: String): Boolean {
        return EMAIL_ADDRESS.matcher(value).matches()
    }

    companion object {
        private val EMAIL_ADDRESS = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
    }
}