package app.vt

import app.bot
import app.config
import com.github.kotlintelegrambot.entities.files.Document
import com.github.kotlintelegrambot.network.bimap

fun Document.getUrl(): String? {
    return with(bot.getFile(fileId)) {
        bimap(
            mapResponse = { it?.result?.filePath?.let { path -> "https://api.telegram.org/file/bot${config.botToken}/$path" } },
            mapError = { println(it); null }
        )
    }
}
