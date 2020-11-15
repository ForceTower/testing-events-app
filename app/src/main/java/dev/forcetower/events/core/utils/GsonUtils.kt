package dev.forcetower.events.core.utils

import com.google.gson.JsonDeserializer
import java.time.LocalDateTime

object GsonUtils {
    @JvmStatic
    val LDT_DESERIALIZER: JsonDeserializer<LocalDateTime> = JsonDeserializer { json, _, _ ->
        val jsonPrimitive = json.asJsonPrimitive
        try {
            val long = jsonPrimitive.asLong
            DateConverters.longToLocalDateTime(long)
        } catch (e: Throwable) {
            null
        }
    }
}
