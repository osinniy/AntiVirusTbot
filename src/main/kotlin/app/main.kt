package app

import app.bot.dispatchers.onHelp
import app.bot.dispatchers.onNewDocument
import app.bot.dispatchers.onQuota
import app.bot.dispatchers.onStart
import app.db.DB
import app.server.setupKtor
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.document
import com.github.kotlintelegrambot.entities.TelegramFile
import com.github.kotlintelegrambot.webhook
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.File
import kotlin.time.ExperimentalTime

lateinit var config: Config
lateinit var botScope: CoroutineScope
@ExperimentalTime
lateinit var db: DB
lateinit var bot: Bot

@ExperimentalTime
fun main(args: Array<String>) {
    config = Config(args.firstOrNull() ?: error("You must specify properties file name in first command line arg"))
    botScope = CoroutineScope(Dispatchers.IO)
    db = DB(File("db.db"))

    setupKtor()

    bot = bot {
        token = config.botToken

        webhook {
            url = config.webhookUrl
            certificate = TelegramFile.ByFile(File("cert.pem"))
        }

        dispatch {
            command("start") { onStart() }
            command("help") { onHelp() }
            command("quota") { onQuota() }

            document { onNewDocument() }
        }
    }
    bot.startPolling()
}
