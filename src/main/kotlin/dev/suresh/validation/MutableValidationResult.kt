package dev.suresh.validation


class MutableValidationResult(vararg errors: String) {
    private val errors: MutableList<String> = errors.toMutableList()

    fun addError(error: String): MutableValidationResult {
        errors.add(error)
        return this
    }

    fun getErrorsAsString(): String {
        return errors.joinToString(separator = ", ")
    }

    fun isValid(): Boolean{
        return errors.isEmpty()
    }

    fun addErrorIf(conditionResult: Boolean, errorMessage: String): MutableValidationResult {
        return if (conditionResult) addError(errorMessage) else this
    }
}