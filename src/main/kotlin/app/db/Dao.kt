package app.db

import com.github.kotlintelegrambot.entities.User

interface Dao {

    suspend fun createTableIfNeeded(): Unit?

    suspend fun addUserIfNeeded(user: User): Unit?

    suspend fun incrementTodayRequestsFor(user: User): Unit?

    suspend fun getTodayRequestsFor(user: User): Int?

    suspend fun clearDailyValues(): Unit?

}
