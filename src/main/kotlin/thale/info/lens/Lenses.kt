package thale.info.lens

import org.http4k.core.Body
import org.http4k.format.Gson.auto
import org.http4k.lens.BiDiBodyLens
import thale.info.api.model.*
import thale.info.exception.Problem

/**
 * Holds all [BiDiBodyLens] for the apis
 */
object Lenses {
    val cardRequestLens: BiDiBodyLens<CardRequest> = Body.auto<CardRequest>().toLens()
    val todoCreateResponseLens: BiDiBodyLens<CardCreateResponse> = Body.auto<CardCreateResponse>().toLens()
    val todoResponseLens: BiDiBodyLens<CardResponse> = Body.auto<CardResponse>().toLens()
    val todoFindResponseLens: BiDiBodyLens<CardFindResponse> = Body.auto<CardFindResponse>().toLens()

    val userResponse: BiDiBodyLens<UserResponse> = Body.auto<UserResponse>().toLens()
    val rolesResponse: BiDiBodyLens<RolesResponse> = Body.auto<RolesResponse>().toLens()
    val addRolesRequest: BiDiBodyLens<AddRolesRequest> = Body.auto<AddRolesRequest>().toLens()

    val problem: BiDiBodyLens<Problem> = Body.auto<Problem>().toLens()

    val loginRequest: BiDiBodyLens<LoginRequest> = Body.auto<LoginRequest>().toLens()
    val loginResponse: BiDiBodyLens<LoginResponse> = Body.auto<LoginResponse>().toLens()
}