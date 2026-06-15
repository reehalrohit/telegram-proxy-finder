package com.duckpsycho.telegramproxyfinder.data.geo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

object GeoRepository {

    private val cache = mutableMapOf<String, Pair<String, String>>()

    suspend fun getCountry(ip: String): Pair<String, String> {

        cache[ip]?.let {
            return it
        }

        return withContext(Dispatchers.IO) {
            try {

                val response =
                    URL("https://ipwho.is/$ip")
                        .readText()

                val json = JSONObject(response)

                val country =
                    json.optString(
                        "country",
                        "Unknown"
                    )

                val countryCode =
                    json.optString(
                        "country_code",
                        "--"
                    )

                val result =
                    Pair(country, countryCode)

                cache[ip] = result

                result

            } catch (e: Exception) {

                Pair("Unknown", "--")
            }
        }
    }
}
