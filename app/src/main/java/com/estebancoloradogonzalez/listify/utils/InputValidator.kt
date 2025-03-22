package com.estebancoloradogonzalez.listify.utils

object InputValidator {
    fun isValidName(name: String): Boolean {
        return name.isNotBlank() && name.matches(Regex(RegexExpressions.VALID_NAME_REGEX))
    }
}