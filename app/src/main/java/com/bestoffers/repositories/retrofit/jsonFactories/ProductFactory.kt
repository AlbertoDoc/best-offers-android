package com.bestoffers.repositories.retrofit.jsonFactories

import com.google.gson.JsonObject

class ProductFactory {

    fun buildGetByTextJson(text: String): JsonObject {
        val json = JsonObject()

        json.addProperty("search", text)

        return json
    }
}