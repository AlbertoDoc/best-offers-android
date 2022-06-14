package com.bestoffers.repositories.retrofit.jsonFactories

import com.google.gson.JsonObject

class AuthFactory {

    fun buildSignupJson(firstName: String, lastName: String, email: String, password: String,
                        confirmPassword: String): JsonObject {
        val json = JsonObject()

        json.addProperty("firstName", firstName)
        json.addProperty("lastName", lastName)
        json.addProperty("email", email)
        json.addProperty("password", password)
        json.addProperty("confirmPassword", confirmPassword)

        return json
    }

    fun buildLoginJson(email: String, password: String): JsonObject {
        val json = JsonObject()

        json.addProperty("email", email)
        json.addProperty("password", password)

        return json
    }
}