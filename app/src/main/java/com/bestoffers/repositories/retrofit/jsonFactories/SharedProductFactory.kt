package com.bestoffers.repositories.retrofit.jsonFactories

import com.google.gson.JsonObject

class SharedProductFactory {

    fun buildPostJson(stablishmentName: String, name: String, price: Float): JsonObject {
        val json = JsonObject()

        json.addProperty("name", name)
        json.addProperty("price", price)
        json.addProperty("stablishmentName", stablishmentName)

        return json
    }
}