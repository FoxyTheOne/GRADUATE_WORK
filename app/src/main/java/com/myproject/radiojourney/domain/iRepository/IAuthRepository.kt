package com.myproject.radiojourney.domain.iRepository

/**
 * Repository. Domain layer.
 */
interface IAuthRepository {
    suspend fun isRememberLoginAndPasswordSelected(): Boolean
    suspend fun getEmail(): String?
    suspend fun getPassword(): String?

    suspend fun onLoginClicked(emailText: String, passwordText: String): Boolean

    // Каждый раз, когда мы кликаем, будет исполняться этот метод. Здесь мы сохраняем статус check box
    suspend fun setRememberLoginAndPasswordSelectedOrNot(isSelected: Boolean)

    suspend fun checkEmail(email: String): Boolean
    suspend fun checkPassword(password: String, confirmPassword: String): Boolean
    suspend fun registerNewUser(email: String, password: String)

    fun logout()
}