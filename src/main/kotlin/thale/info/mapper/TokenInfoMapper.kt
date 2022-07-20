package thale.info.mapper

import thale.info.api.model.TokenInfoModel
import thale.info.auth.TokenInfo

fun TokenInfo.toTokenInfoModel(): TokenInfoModel =
    TokenInfoModel().idToken(idToken).refreshToken(refreshToken).expiresAt(expiresAt.toString())