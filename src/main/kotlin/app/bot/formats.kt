package app.bot

import app.DAILY_QUOTA
import app.vt.AnalyzeResultAttributesStats

fun AnalyzeResultAttributesStats.formatted(): String {
    return """
        *Results:*

        _Detected:_ ${malicious + suspicious}
        _Undetected:_ ${harmless + undetected}
        _Not finished:_ $timeout
    """.trimIndent()
}

fun formatQuota(todayRequestsLeft: Int): String {
    return """
        Your today checks left: *${todayRequestsLeft.coerceAtMost(DAILY_QUOTA)}*
        Quotas updates at midnight (UTC)
    """.trimIndent()
}
