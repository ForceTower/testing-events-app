package dev.forcetower.events.data.network

import com.google.gson.JsonDeserializer
import dev.forcetower.events.data.local.DateConverters
import java.time.LocalDateTime

object GsonSerializers {
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