package dev.suresh.validation


class RequestValidationResult(errors: List<String> = emptyList()) {
    private val errors: List<String> = errors.toList()
    val isValid: Boolean = errors.isEmpty()

    fun addError(error: String): RequestValidationResult {
        return RequestValidationResult( errors + error)
    }

    fun getErrorsAsString(): String {
        return errors.joinToString(separator = ", ")
    }

    fun addErrorIf(conditionResult: Boolean, errorMessage: String): RequestValidationResult {
        return if (conditionResult) addError(errorMessage) else this
    }
}