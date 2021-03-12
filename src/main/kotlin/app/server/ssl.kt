package app.server

import app.config
import java.io.File
import java.security.KeyStore

val keystoreFile = File("keystore.jks")

val keystore: KeyStore = KeyStore.getInstance("JKS").apply {
    load(keystoreFile.inputStream(), config.keystorePassword.toCharArray())
}
