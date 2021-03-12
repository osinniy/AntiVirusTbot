package app

import java.io.File
import java.util.Properties

class Config(propertiesFile: String) {
    private val properties = Properties().apply { load(File(propertiesFile).bufferedReader()) }

    val botToken = properties.getProperty("bot.token") ?: error("Property 'bot.token' not specified")
    val vtApiKey = properties.getProperty("vt.api.key") ?: error("Property 'vt.api.key' not specified")
    val webhookUrl = properties.getProperty("webhook.url") ?: error("Property 'webhook.url' not specified")
    val keystoreAlias = properties.getProperty("keystore.alias") ?: error("Property 'keystore.alias' not specified")
    val keystorePassword = properties.getProperty("keystore.password") ?: error("Property 'keystore.password' not specified")
    val gcloudProjectName: String? = properties.getProperty("gcloud.project.name") /* optional */
}
