package app.db

import app.logs.Logger
import app.runEveryDayAt
import app.scheduleOn
import com.github.kotlintelegrambot.entities.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement
import kotlin.time.ExperimentalTime

@ExperimentalTime
class DaoImpl(private val connection: Connection, private val dispatcher: CoroutineDispatcher) : Dao {

    init {
        scheduleOn(dispatcher) {
            runEveryDayAt(0, 0) {
                clearDailyValues()
            }
        }
    }


    override suspend fun createTableIfNeeded() = doSql<Unit> {
        executeUpdate("""
            CREATE TABLE IF NOT EXISTS users (
               id             INTEGER      PRIMARY KEY ON CONFLICT IGNORE
                                           NOT NULL,
               creation_time  INTEGER (13) NOT NULL,
               today_requests INTEGER (2)  DEFAULT (0)
                                           NOT NULL
            );
        """)
    }

    override suspend fun addUserIfNeeded(user: User) = doSql<Unit> {
        executeUpdate("""
            INSERT INTO users (id, creation_time)
            VALUES (${user.id}, '${System.currentTimeMillis()}');
        """)
    }

    override suspend fun incrementTodayRequestsFor(user: User) = doSql<Unit> {
        executeUpdate("UPDATE users SET today_requests = today_requests + 1 WHERE id = ${user.id}")
    }

    override suspend fun getTodayRequestsFor(user: User) = doSql {
        executeQuery("SELECT today_requests FROM users WHERE id = ${user.id}").getInt("today_requests")
    }

    override suspend fun clearDailyValues() = doSql<Unit> {
        executeUpdate("UPDATE users SET today_requests = 0")
    }


    private suspend fun <R> doSql(action: Statement.() -> R): R? = withContext(dispatcher) {
        try {
            connection.createStatement().use {
                try {
                    it.action()
                } catch (e: SQLException) {
                    e.printStackTrace()
                    Logger.log(e)
                    null
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            Logger.log(e)
            null
        }
    }

}
