package app.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.time.ExperimentalTime

@ExperimentalTime
class DB(dbFile: File) {

    private val dbConnection = try {
        DriverManager.getConnection("jdbc:sqlite:${dbFile.absolutePath}")
    } catch (e: SQLException) {
        throw IllegalStateException("An error occurred while opening a database", e)
    }

    val dao: Dao = DaoImpl(dbConnection, Dispatchers.IO)

    init {
        GlobalScope.launch(Dispatchers.IO) {
            dao.createTableIfNeeded()
        }
    }

}
