import app.vt.AnalyzeResult
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

class SerializationTests {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Test
    fun testAnalyzeResult() {
        json.decodeFromString<AnalyzeResult>("""
{
    "data": {
        "attributes": {
            "date": 1614188446,
            "results": {
                "ADMINUSLabs": {
                    "category": "harmless",
                    "engine_name": "ADMINUSLabs",
                    "method": "blacklist",
                    "result": "clean"
                },
                "AICC (MONITORAPP)": {
                    "category": "harmless",
                    "engine_name": "AICC (MONITORAPP)",
                    "method": "blacklist",
                    "result": "clean"
                },
                "AegisLab WebGuard": {
                    "category": "harmless",
                    "engine_name": "AegisLab WebGuard",
                    "method": "blacklist",
                    "result": "clean"
                }
            },
            "stats": {
                "harmless": 77,
                "malicious": 0,
                "suspicious": 0,
                "timeout": 0,
                "undetected": 7
            },
            "status": "completed"
        },
        "id": "u-8ffd56a811cb8c011773cf0ec59b2c26e65c926675e55d9fb67ade3fafe8f399-1614188446",
        "type": "analysis"
    },
    "meta": {
        "url_info": {
            "id": "8ffd56a811cb8c011773cf0ec59b2c26e65c926675e55d9fb67ade3fafe8f399",
            "url": "https://hsto.org/getpro/habr/avatars/057/c5b/925/057c5b9257a2940fd7d24ee37a37f622.png"
        }
    }
}
        """).also { println(it) }
    }
}
