package dev.forcetower.events.data.network

import dev.forcetower.events.data.model.Event
import dev.forcetower.events.data.model.dto.CheckInRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventService {
    @GET("events")
    suspend fun events(): List<Event>

    @GET("events/{id}")
    suspend fun event(@Path("id") id: String): Event

    @POST("checkin")
    suspend fun checkIn(@Body data: CheckInRequest): Any
}