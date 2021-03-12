package app.server

import app.bot
import com.google.gson.JsonSyntaxException
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.content.file
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.sslConnector
import io.ktor.server.netty.Netty
import kotlin.concurrent.thread

fun setupKtor() {
    thread(name = "Ktor server") {
        embeddedServer(Netty, applicationEngineEnvironment {
            sslConnector(
                keystore,
                keyAlias = app.config.keystoreAlias,
                keyStorePassword = { "".toCharArray() },
                privateKeyPassword = { app.config.keystorePassword.toCharArray() }
            ) {
                port = Url(app.config.webhookUrl).port
                keyStorePath = keystoreFile.absoluteFile

                module {
                    routing {
                        get("/") {
                            call.respondText("OK")
                        }

                        post<String>("/${app.config.botToken}") {
                            try {
                                bot.processUpdate(it)
                                call.respond(HttpStatusCode.Accepted, "OK")
                            } catch (e: JsonSyntaxException) {
                                call.respond(HttpStatusCode.BadRequest, "Invalid json")
                            }
                        }

                        file("/reqbin-verify.txt")
                    }
                }
            }
        }).start(true)
    }
}
