package com.example.list_4pm2_2425.utils

import android.content.Context

object SessionManager {
    private const val PREF_NAME = "AppPrefs" // Используйте одно имя файла для SharedPreferences
    private const val KEY_IS_AUTHORIZED = "isAuthorized" // Один ключ для проверки авторизации

    // Метод для сохранения состояния авторизации
    fun saveUserState(context: Context, isAuthorized: Boolean) {
        val sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean(KEY_IS_AUTHORIZED, isAuthorized) // Используем единый ключ
        editor.apply()  // Сохраняем изменения
    }

    // Метод для проверки авторизации пользователя
    fun isUserAuthorized(context: Context): Boolean {
        val sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean(KEY_IS_AUTHORIZED, false) // Читаем по ключу isAuthorized
    }
}
