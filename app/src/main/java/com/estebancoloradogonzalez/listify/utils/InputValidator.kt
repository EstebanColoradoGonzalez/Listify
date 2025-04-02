package com.estebancoloradogonzalez.listify.utils

object InputValidator {
    fun isValidName(name: String): Boolean {
        return name.isNotBlank() && name.matches(Regex(RegexExpressions.VALID_NAME_REGEX))
    }

    fun isValidBudget(value: String): Boolean {
        return value.isNotBlank()
                && value.matches(Regex(RegexExpressions.VALID_MONEY_NUMBER_REGEX))
                && value.toDouble() > NumericConstants.LONG_ZERO
    }
}