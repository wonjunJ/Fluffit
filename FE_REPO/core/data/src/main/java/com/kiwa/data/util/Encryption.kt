package com.kiwa.data.util

import com.kiwa.fluffit.data.BuildConfig
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

//암호화 알고리즘 정의 정해야함
private const val HMAC_ALGORITHM = "HmacSHA256"

@Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
fun calculateHmac(data: String): String {
    val secretKeySpec = SecretKeySpec(BuildConfig.SECRET_KEY.toByteArray(), HMAC_ALGORITHM)
    val mac = Mac.getInstance(HMAC_ALGORITHM)
    mac.init(secretKeySpec)
    val hmacBytes = mac.doFinal(data.toByteArray())
    return Base64.getEncoder().encodeToString(hmacBytes).toString()
}
