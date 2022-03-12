package dev.forcetower.events.data.network

import com.google.gson.JsonDeserializer
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object GsonSerializers {
    @JvmStatic
    val LDT_DESERIALIZER: JsonDeserializer<LocalDateTime> = JsonDeserializer { json, _, _ ->
        val jsonPrimitive = json.asJsonPrimitive
        try {
            val long = jsonPrimitive.asLong
            LocalDateTime.ofInstant(Instant.ofEpochMilli(long), ZoneId.of("UTC"))
        } catch (e: Throwable) {
            null
        }
    }
}
