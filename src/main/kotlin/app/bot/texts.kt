package app.bot

import app.DAILY_QUOTA

private const val VT_ABOUT_LINK = "https://support.virustotal.com/hc/en-us/articles/115002126889-How-it-works"
private const val MAX_FILE_SIZE = "20MB"

val TEXT_START = """
    Welcome to *AntiVirusBot*!

    Each file sent to this chat will be checked for viruses on virustotal.com. And then you will get a result.

    Your limits is:
        - *1* file per minute
        - *$DAILY_QUOTA* files per day
        - *$MAX_FILE_SIZE* max file size

    Please do not submit any personal information: Bot or VirusTotal [are not responsible]($VT_ABOUT_LINK) for the contents of your submission.
""".trimIndent()

val TEXT_HELP = """
    To scan file for viruses send file to this chat
    To check your quota send command /quota
""".trimIndent()

const val TEXT_FILE_QUEUED = "Your file was queued for check. Please wait"

const val ERR_QUOTA_EXCEEDED = "You exceeded your daily quota. It will reset at midnight (UTC)"

const val ERR_FILE_TOO_LARGE = "This file is too big. Please send me a file smaller than *$MAX_FILE_SIZE*"

const val ERR_DOWNLOAD_FILE = "Cannot download file: internal error"

const val ERR_INTERNAL = "Internal error. Please try later"

const val ERR_TIMEOUT = "Timeout. Please try again"
