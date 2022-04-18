package com.yeonkyu.kuringhouse.data.remote

import com.yeonkyu.kuringhouse.data.source.remote.ApiService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets

class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var service: ApiService

    @Before
    fun init() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        service = createApiService()
    }

    @After
    fun stopServer() {
        mockWebServer.shutdown()
    }

    private fun createApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private fun enqueueResponse(fileName: String) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream("api-response/$fileName")
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()

        mockResponse.setBody(source.readString(StandardCharsets.UTF_8))
        mockWebServer.enqueue(mockResponse)
    }

    @Test
    fun deleteRoomTest() = runBlocking {
        // given
        enqueueResponse("/RoomDeleteResponse.json")

        // when
        val response = service.deleteRoom("API_TOKEN_EXAMPLE")
        mockWebServer.takeRequest()

        // then
        val expectedRoomId = "ce8db108-149e-41db-b7db-dc253deb8b00"
        val expectedState = "deleted"

        assertEquals(true, response.isSuccessful)
        assertEquals(true, response.body() != null)
        assertEquals(true, response.body()?.room != null)
        assertEquals(expectedRoomId, response.body()?.room?.roomId)
        assertEquals(expectedState, response.body()?.room?.state)
    }
}