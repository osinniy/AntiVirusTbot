package app.bot.dispatchers

import app.DAILY_QUOTA
import app.MAX_FILE_SIZE_BYTES
import app.bot.*
import app.botScope
import app.db
import app.logs.Logger
import app.vt.VTException
import app.vt.analyzeUrl
import app.vt.getUrl
import com.github.kotlintelegrambot.dispatcher.handlers.media.MediaHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.files.Document
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun MediaHandlerEnvironment<Document>.onNewDocument() = botScope.launch {
    val todayRequests = db.dao.getTodayRequestsFor(message.from!!)
    when {
        todayRequests != null && todayRequests >= DAILY_QUOTA -> message.reply(ERR_QUOTA_EXCEEDED)
        todayRequests == null -> message.reply(ERR_INTERNAL)

        media.fileSize == null -> message.reply(ERR_DOWNLOAD_FILE)
        media.fileSize!! > MAX_FILE_SIZE_BYTES -> message.reply(ERR_FILE_TOO_LARGE)

        else -> media.getUrl()?.also { url ->
            db.dao.incrementTodayRequestsFor(message.from!!)

            val waitMessageId = message.reply(TEXT_FILE_QUEUED)?.messageId

            analyzeUrl(url)?.also { result ->
                result.first?.let {
                    message.reply(it.formatted(), ParseMode.MARKDOWN)
                }

                result.second?.let {
                    if (it is VTException) {
//                        must not happen in production
                        message.reply("Cannot proceed your request because of: ${it.message}")
                    } else {
                        message.reply(ERR_INTERNAL)
                    }
                    Logger.log(it, message.from?.id)
                }
            } ?: message.reply(ERR_TIMEOUT)

            waitMessageId?.let {
                bot.deleteMessage(ChatId.fromId(message.chat.id), it)
            }
        } ?: message.reply(ERR_DOWNLOAD_FILE)
    }
}
