package com.flagsmith.internal

import com.flagsmith.entities.Identity
import com.flagsmith.entities.Trait
import com.flagsmith.entities.TraitWithIdentity
import com.flagsmith.entities.TraitsWithIdentity
import com.github.kittinunf.fuel.core.HeaderValues
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.util.FuelRouting
import com.google.gson.Gson

sealed class FlagsmithApi : FuelRouting {
    class GetIdentityFlagsAndTraits(val identity: String, val traits: List<Trait>? = null) : FlagsmithApi()
    object GetFlags : FlagsmithApi()
    class SetTrait(val trait: Trait, val identity: String) : FlagsmithApi()
    class SetTraits(vararg val trait: Trait, val identity: String) : FlagsmithApi()
    class PostAnalytics(val eventMap: Map<String, Int?>) : FlagsmithApi()

    companion object {
        var environmentKey: String? = null
        var baseUrl: String? = null
    }

    override val basePath: String
        get() {
            if (baseUrl == null) {
                throw IllegalStateException("baseUrl not set in FlagsmithApi, check Flagsmith SDK initialization")
            } else {
                return baseUrl!!
            }
        }
    override val body: String?
        get() = when (this) {
            is SetTrait -> Gson().toJson(
                TraitWithIdentity(
                    key = trait.key,
                    value = trait.value,
                    identity = Identity(identity)
                )
            )
            is SetTraits -> Gson().toJson(
                TraitsWithIdentity(
                    identifier = identity,
                    traits = trait.toList()
                )
            )
            is PostAnalytics -> Gson().toJson(eventMap)
            is GetIdentityFlagsAndTraits -> Gson().toJson(
                mutableMapOf<String, Any>("identifier" to identity).apply {
                    if (!traits.isNullOrEmpty()) {
                        this += "traits" to traits
                    }
                }
            )
            else -> null
        }

    override val bytes: ByteArray? = null

    override val headers: Map<String, HeaderValues>?
        get() = mutableMapOf<String, HeaderValues>(
            "X-Environment-Key" to listOf(environmentKey ?: "")
        ).apply {
            if (method == Method.POST) {
                this += "Content-Type" to listOf("application/json")
            }
        }

    override val method: Method
        get() = when (this) {
            is GetIdentityFlagsAndTraits -> Method.POST
            is GetFlags -> Method.GET
            is SetTraits,
            is SetTrait -> Method.POST
            is PostAnalytics -> Method.POST
        }

    override val params: Parameters?
        get() = when (this) {
            is GetIdentityFlagsAndTraits -> listOf("identifier" to this.identity)
            else -> null
        }

    override val path: String
        get() = when (this) {
            is SetTraits,
            is GetIdentityFlagsAndTraits -> "/identities/"
            is GetFlags -> "/flags/"
            is SetTrait -> "/traits/"
            is PostAnalytics -> "/analytics/flags/"
        }
}