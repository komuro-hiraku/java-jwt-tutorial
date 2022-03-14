package dev.com4dc.kotlin.sample

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import dev.com4dc.kotlin.sample.exception.FailedCreateJwtException
import dev.com4dc.kotlin.sample.exception.FailedVerifyJwtException
import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Instant
import java.util.*


/**
 * java-jwt Helper Class
 */
class JwtHelper {

  companion object {
    val GENERATE_KEYS = genKeys()

    /**
     * KeyGenerator で RSAPublicKey と RSAPrivateKey を生成
     */
    fun genKeys(): Pair<RSAPublicKey, RSAPrivateKey> {
      val keyGenerator = KeyPairGenerator.getInstance("RSA")
      keyGenerator.initialize(1024)
      val pair = keyGenerator.genKeyPair()

      return Pair(pair.public as RSAPublicKey, pair.private as RSAPrivateKey)
    }

    fun loadKey(file_name: String): ByteArray {
      return Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(file_name).toURI()))
    }
  }

  /**
   * encode with RS256
   * @param issuer
   * @return Created JWT String
   */
  fun encodeRS256(issuer: String, claims:Map<String, Any>? = null): String {
    val publicKey = GENERATE_KEYS.first
    val privateKey = GENERATE_KEYS.second

    val now = Instant.now()

    try {
      val algorithm: Algorithm = Algorithm.RSA256(publicKey, privateKey)
      val builder = JWT.create()
        .withIssuer(issuer)
        .withIssuedAt(Date(now.toEpochMilli()))
        .withExpiresAt(Date(now.plusSeconds(600).toEpochMilli())) // 10 min

      claims?.let {
        it.forEach { (k, v) ->
          when(v) {
            is String -> builder.withClaim(k, v)
            is Int -> builder.withClaim(k, v)
            is Double -> builder.withClaim(k, v)
            is Date -> builder.withClaim(k, v)
            is Long -> builder.withClaim(k, v)
            is List<*> -> builder.withClaim(k, v)
//            is Map<*, *> -> builder.withClaim(k, v)
          }
        }
      }

      return builder.sign(algorithm)
    } catch (exception: JWTCreationException) {
      throw FailedCreateJwtException("Can't create JWT", exception)
    }
  }

  fun decodeRS256(token: String, issuer: String): DecodedJWT {

    val publicKey = GENERATE_KEYS.first
    val privateKey = GENERATE_KEYS.second

    try {
      val algorithm = Algorithm.RSA256(publicKey, privateKey)
      val verifier: JWTVerifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .withClaim("sample", "value1")
        .build()
      return verifier.verify(token)
    } catch (exception: JWTVerificationException) {
      throw FailedVerifyJwtException("can't verify token", exception)
    }
  }

}