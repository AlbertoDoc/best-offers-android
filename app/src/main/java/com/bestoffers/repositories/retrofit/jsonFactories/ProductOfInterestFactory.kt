package com.bestoffers.repositories.retrofit.jsonFactories

import com.google.gson.JsonObject

class ProductOfInterestFactory {

    fun buildPostJson(productId: String, startPrice: Float, endPrice: Float,
                      activate: Boolean): JsonObject {
        val json = JsonObject()

        json.addProperty("productId", productId)
        json.addProperty("startPrice", startPrice)
        json.addProperty("endPrice", endPrice)
        json.addProperty("activate", activate)

        return json
    }
}