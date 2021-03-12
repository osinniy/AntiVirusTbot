package app.bot.dispatchers

import app.DAILY_QUOTA
import app.bot.*
import app.botScope
import app.db
import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.entities.ParseMode
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun CommandHandlerEnvironment.onStart() = botScope.launch {
    message.send(TEXT_START, ParseMode.MARKDOWN, disableWebPagePreview = true)
    db.dao.addUserIfNeeded(message.from!!)
}

fun CommandHandlerEnvironment.onHelp() = botScope.launch {
    message.send(TEXT_HELP, ParseMode.MARKDOWN)
}

@ExperimentalTime
fun CommandHandlerEnvironment.onQuota() = botScope.launch {
    val todayRequests = db.dao.getTodayRequestsFor(message.from!!)
    if (todayRequests != null) message.send(formatQuota(DAILY_QUOTA - todayRequests), ParseMode.MARKDOWN)
    else message.send(ERR_INTERNAL)
}
