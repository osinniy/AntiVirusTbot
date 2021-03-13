
import app.Apis
import app.vt.AnalyzeResult
import app.vt.VTResponse
import app.vt.asVTError
import kotlinx.serialization.decodeFromString
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.jupiter.api.Test

class SerializationTests {
    private val json = Apis.kotlinxSerialization

    @Test
    fun testVTResponse() {
        json.decodeFromString<VTResponse<AnalyzeResult>>("""
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

    @Test
    fun testVTError() {
        ResponseBody.create(MediaType.get("application/json"), """
            { "error": { "message": "Quota exceeded", "code": "QuotaExceededError" } }
        """).asVTError().also { println(it) }
    }
}
