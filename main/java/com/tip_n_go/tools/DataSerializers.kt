package com.tip_n_go.tools

import com.google.gson.*
import com.tip_n_go.data.incoming.UpdateOrganizationSettings
import com.tip_n_go.data.incoming.UpdateSettings
import java.lang.reflect.Type

class UpdateSettingsSerializer : JsonSerializer<UpdateSettings> {

    override fun serialize(
        src: UpdateSettings?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val result = JsonObject()
        result.addProperty("key", src?.key)
        val array = JsonArray()
        src?.selectedOptionId?.forEach { array.add(it) }
        result.add("selectedOptionId", array)
        return result
    }
}

class UpdateOrganizationSettingsSerializer : JsonSerializer<UpdateOrganizationSettings> {

    override fun serialize(
        src: UpdateOrganizationSettings?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val result = JsonObject()
        val settings = context?.serialize(src?.settings, UpdateSettingsList::class.java)
        result.add("settings", settings)
        return result
    }
}

private class UpdateSettingsList : ArrayList<UpdateSettings>()
