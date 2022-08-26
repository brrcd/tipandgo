package com.tip_n_go.tools

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.exception.ValidationErrors
import com.tip_n_go.data.incoming.*
import java.lang.reflect.Type

class BalanceDeserializer : JsonDeserializer<Balance> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Balance {
        val obj = json?.asJsonObject
        val balance = obj.getAmount("balance")
        val currency: Currency? = obj.getObject(context, "currency")
        val balanceLocalized = if (currency != null) {
            "$balance ${currency.sign}"
        } else {
            obj.getLocalizedCurrency("balance")
        }
        return Balance(
            balanceLocalized,
            balance,
            currency
        )
    }
}

class PayoutDeserializer : JsonDeserializer<Payout> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Payout {
        val obj = json?.asJsonObject
        val date = obj.getDate()
        val amount = obj.getAmount("amount")
        val currency: Currency? = obj.getObject(context, "currency")
        val amountLocalized = if (currency != null) {
            "$amount ${currency.sign}"
        } else {
            obj.getLocalizedCurrency("amount")
        }
        val userBankCard: BankCard? = obj.getObject(context, "userBankCard")
        return Payout(
            date,
            amount,
            amountLocalized,
            currency,
            userBankCard
        )
    }
}

class ReviewDeserializer : JsonDeserializer<Review> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Review {
        val obj = json?.asJsonObject
        val date = obj.getDate()
        val comments = obj.getString("comments")
        val rate = obj.getRating("rate")
        val metricRates: MetricRateList? = obj.getObject(context, "metricRates")
        val tip: TipBrief? = obj.getObject(context, "tip")
        return Review(
            date,
            comments,
            rate,
            metricRates,
            tip
        )
    }
}

class TipShortDeserializer : JsonDeserializer<TipBrief> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): TipBrief {
        val obj = json?.asJsonObject
        val hash = obj.getString("hash")
        val date = obj.getDate()
        val tipRate = obj.getTipRate()
        val amount = obj.getAmount("amount")
        val currency: Currency? = obj.getObject(context, "currency")
        val amountLocalized = if (currency != null) {
            "$amount ${currency.sign}"
        } else {
            obj.getLocalizedCurrency("amount")
        }
        return TipBrief(
            hash,
            date,
            tipRate,
            amount,
            amountLocalized,
            currency
        )
    }
}

class TipDeserializer : JsonDeserializer<Tip> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Tip {
        val obj = json?.asJsonObject
        val hash = obj.getString("hash")
        val date = obj.getDate()
        val tipRate: String? = obj.getTipRate()
        val amount = obj.getAmount("amount")
        val currency: Currency? = obj.getObject(context, "currency")
        val amountLocalized = if (currency != null) {
            "$amount ${currency.sign}"
        } else {
            obj.getLocalizedCurrency("amount")
        }
        val user: UserBrief? = obj.getObject(context, "user")
        val organization: Organization? = obj.getObject(context, "organization")
        val review: Review? = obj.getObject(context, "review")
        return Tip(
            hash,
            date,
            tipRate,
            amount,
            amountLocalized,
            currency,
            user,
            organization,
            review
        )
    }
}

class DistributedTipDeserializer : JsonDeserializer<DistributedTip> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): DistributedTip {
        val obj = json?.asJsonObject
        val date: String? = obj.getDate()
        val user: UserBrief? = obj.getObject(context, "user")
        val amount: Double? = obj.getAmount("amount")
        val currency: Currency? = obj.getObject(context, "currency")
        val amountLocalized: String? = if (currency != null) {
            "$amount ${currency.sign}"
        } else {
            obj.getLocalizedCurrency("amount")
        }
        val done: Boolean? = obj.getBoolean("done")
        val source: TipList? = obj.getObject(context, "tips")
        val destinations: TipDestinationList? = obj?.getObject(context, "destinations")
        return DistributedTip(
            date,
            user,
            amount,
            amountLocalized,
            currency,
            done,
            source,
            destinations
        )
    }
}

class TipDestinationDeserializer : JsonDeserializer<TipDestination> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): TipDestination {
        val obj = json?.asJsonObject
        val amount: Double? = obj.getAmount("amount")
        val currency: Currency? = obj.getObject(context, "currency")
        val amountLocalized: String? = if (currency != null) {
            "$amount ${currency.sign}"
        } else {
            obj.getLocalizedCurrency("amount")
        }
        val user: UserBrief? = obj.getObject(context, "user")
        return TipDestination(
            amount,
            amountLocalized,
            currency,
            user
        )
    }
}

class UserDeserializer : JsonDeserializer<User> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): User {
        val obj = json?.asJsonObject
        val hash = obj.getString("hash")
        val lastName = obj.getString("lastName")
        val firstName = obj.getString("firstName")
        val phone = obj.getString("phone")
        val email = obj.getString("email")
        val emailConfirmed = obj.getBoolean("emailConfirmed")
        val language = obj.getString("language")
        val avatar = obj.getString("avatar")
        val rating = obj.getRating("rating")
        val userRoles: RoleList? = obj.getObject(context, "userRoles")
        val userOrganizations: OrganizationBriefList? = obj.getObject(context, "userOrganizations")
        val currency: Currency? = obj.getObject(context, "currency")
        return User(
            hash, lastName, firstName, phone, email, emailConfirmed, language, avatar,
            rating, userRoles, userOrganizations, currency
        )
    }
}

class UserBriefDeserializer : JsonDeserializer<UserBrief> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): UserBrief {
        val obj = json?.asJsonObject
        val hash = obj.getString("hash")
        val lastName = obj.getString("lastName")
        val firstName = obj.getString("firstName")
        val phone = obj.getString("phone")
        val email = obj.getString("email")
        val emailConfirmed = obj.getBoolean("emailConfirmed")
        val language = obj.getString("language")
        val avatar = obj.getString("avatar")
        val rating = obj.getRating("rating")
        val position = obj.getString("position")
        return UserBrief(
            hash, lastName, firstName, phone, email, emailConfirmed, language, avatar,
            rating, position
        )
    }
}

class OrganizationUserDeserializer : JsonDeserializer<OrganizationUser> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): OrganizationUser {
        val obj = json?.asJsonObject
        val hash = obj.getString("hash")
        val lastName = obj.getString("lastName")
        val firstName = obj.getString("firstName")
        val phone = obj.getString("phone")
        val email = obj.getString("email")
        val emailConfirmed = obj.getBoolean("emailConfirmed")
        val language = obj.getString("language")
        val avatar = obj.getString("avatar")
        val rating = obj.getRating("rating")
        val position = obj.getString("position")
        val organization: Organization? = obj.getObject(context, "organization")
        val organizationRole: Role? = obj.getObject(context, "organizationRole")
        return OrganizationUser(
            hash,
            lastName,
            firstName,
            phone,
            email,
            emailConfirmed,
            language,
            avatar,
            rating,
            position,
            organization,
            organizationRole
        )
    }
}

class OrganizationDeserializer : JsonDeserializer<Organization> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Organization {
        val obj = json?.asJsonObject
        val brand: Brand? = obj.getObject(context, "brand")
        val name = obj.getString("name")
        val legalName = obj.getString("legalName")
        val phone = obj.getString("phone")
        val website = obj.getString("website")
        val email = obj.getString("email")
        val address = obj.getString("address")
        val hash = obj.getString("hash")
        val tipDistributionMethodId: Int? = if (obj?.get("tipDistributionMethodId").isNull()) {
            null
        } else {
            obj?.get("tipDistributionMethodId")?.asInt
        }
        val rating = obj.getRating("rating")
        val country: Country? = obj.getObject(context, "country")
        val currency: Currency? = obj.getObject(context, "currency")
        val organizationUsers: OrganizationUserList? = obj.getObject(context, "organizationUsers")
        return Organization(
            brand,
            name,
            legalName,
            phone,
            website,
            email,
            address,
            hash,
            tipDistributionMethodId,
            rating,
            country,
            currency,
            organizationUsers
        )
    }
}

class NotificationDeserializer : JsonDeserializer<Notification> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Notification {
        val obj = json?.asJsonObject
        val id: Int? = if (obj?.get("id").isNull()) {
            null
        } else {
            obj?.get("id")?.asInt
        }
        val date = obj.getDate()
        val text = obj.getString("text")
        val isRead = obj.getBoolean("isRead")
        return Notification(
            id ?: -1,
            date,
            text,
            isRead ?: false
        )
    }
}

class SeriesDeserializers : JsonDeserializer<Series> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Series {
        val obj = json?.asJsonObject
        val name = obj.getString("name")
        val data: DoubleList? =
            try {
                obj.getObject(context, "data")
            } catch (e: Exception) {
                null
            }
        val dataPie: DataPieList? =
            try {
                obj.getObject(context, "data")
            } catch (e: Exception) {
                null
            }
        val type = obj.getString("type")
        val color = obj.getString("color")
        return Series(
            name,
            data,
            dataPie,
            type,
            color
        )
    }
}

class ResponseErrorDeserializer : JsonDeserializer<ResponseError> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ResponseError {
        val obj = json?.asJsonObject
        val code: Int = obj?.get("id")?.asInt ?: 0
        val error: String = if (obj.getString("error").isNullOrEmpty()) {
            obj.getString("Error") ?: "error"
        } else {
            obj.getString("error") ?: "error"
        }
        val validationErrors: ValidationErrors? = obj.getObject(context, "validationErrors")
        return ResponseError(
            code = code,
            error = error,
            validationErrors = validationErrors
        )
    }
}

class ValidationErrorsDeserializer : JsonDeserializer<ValidationErrors>{

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ValidationErrors? {
        val obj = json?.asJsonObject
        val keys = obj?.entrySet()
        val validationErrors = if (!keys.isNullOrEmpty()) {
            val iterator = keys.iterator()
            val values = mutableListOf<String>()
            while (iterator.hasNext()) {
                values.add(iterator.next().value.asString)
            }
            ValidationErrors(
                additionalProp1 = values[0]
            )
        } else {
            null
        }
        return validationErrors
    }
}

private fun JsonElement?.isNull(): Boolean {
    return this?.isJsonNull == true
}

private fun JsonObject?.getLocalizedCurrency(key: String): String? {
    return if (this?.get(key).isNull()) {
        null
    } else {
        this?.get(key)?.asInt?.prepareCurrency()
    }
}

private fun JsonObject?.getDate(): String? {
    return if (this?.get("date").isNull()) {
        null
    } else {
//        val sharedPrefs = SharedTools.sharedPrefs()
//        val language = sharedPrefs[USER_LANGUAGE] ?: ""
//        if (language.isNotBlank()){
//            val locale = Locale(language)
//            this?.get("date")?.asString?.prepareDate(locale)
//        } else {
        this?.get("date")?.asString?.prepareDate()
//        }
    }
}

private fun JsonObject?.getString(key: String): String? {
    return if (this?.get(key).isNull()) {
        null
    } else {
        this?.get(key)?.asString
    }
}

private fun JsonObject?.getTipRate(): String? {
    return if (this?.get("tipRate").isNull()) {
        null
    } else {
        this?.get("tipRate")?.asInt?.prepareTipRate()
    }
}

private fun JsonObject?.getRating(key: String): Double? {
    return if (this?.get(key).isNull()) {
        null
    } else {
        this?.get(key)?.asInt?.prepareRate()
    }
}

private fun JsonObject?.getAmount(key: String): Double? {
    return if (this?.get(key).isNull()) {
        null
    } else {
        this?.get(key)?.asInt?.prepareAmount()
    }
}

private inline fun <reified T> JsonObject?.getObject(
    context: JsonDeserializationContext?,
    key: String
): T? {
    return if (this?.get(key).isNull()) {
        null
    } else {
        context?.deserialize(this?.get(key), T::class.java)
    }
}

private fun JsonObject?.getBoolean(key: String): Boolean? {
    return if (this?.get(key).isNull()) {
        null
    } else {
        this?.get(key)?.asBoolean
    }
}

// helper classes
private class TipDestinationList : ArrayList<TipDestination>()
private class TipList : ArrayList<Tip>()
private class MetricRateList : ArrayList<MetricRate>()
private class RoleList : ArrayList<Role>()
private class OrganizationBriefList : ArrayList<OrganizationBrief>()
private class OrganizationUserList : ArrayList<OrganizationUser>()
private class DataPieList : ArrayList<DataPie>()
private class StringList : ArrayList<String>()
private class DoubleList : ArrayList<Double>()
