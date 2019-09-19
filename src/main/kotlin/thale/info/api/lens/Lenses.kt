package thale.info.api.lens

import org.http4k.core.Body
import org.http4k.format.Gson.auto
import org.http4k.lens.BiDiBodyLens
import thale.info.api.model.*
import thale.info.exception.Problem
import thale.info.api.security.authentication.model.TokenExchangeResponse

/**
 * Holds all [BiDiBodyLens] for the apis
 */
object Lenses {
    val todoRequestLens: BiDiBodyLens<TodoRequest> = Body.auto<TodoRequest>().toLens()
    val todoCreateResponseLens: BiDiBodyLens<TodoCreateResponse> = Body.auto<TodoCreateResponse>().toLens()
    val todoResponseLens: BiDiBodyLens<TodoResponse> = Body.auto<TodoResponse>().toLens()
    val todoFindResponseLens: BiDiBodyLens<TodoFindResponse> = Body.auto<TodoFindResponse>().toLens()

    val userResponse: BiDiBodyLens<UserResponse> = Body.auto<UserResponse>().toLens()
    val rolesResponse: BiDiBodyLens<RolesResponse> = Body.auto<RolesResponse>().toLens()
    val addRolesRequest: BiDiBodyLens<AddRolesRequest> = Body.auto<AddRolesRequest>().toLens()

    val problem: BiDiBodyLens<Problem> = Body.auto<Problem>().toLens()

    val loginRequest: BiDiBodyLens<LoginRequest> = Body.auto<LoginRequest>().toLens()
    val tokenExchangeResponse: BiDiBodyLens<TokenExchangeResponse> = Body.auto<TokenExchangeResponse>().toLens()
}