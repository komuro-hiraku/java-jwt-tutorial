package dev.com4dc.kotlin.sample

import dev.com4dc.kotlin.sample.exception.FailedVerifyJwtException
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class JwtHelperTest {

  private val target = JwtHelper()

  @Test
  fun `指定した Issuer を含む JWT が作成できる`() {
    val token = target.encodeRS256("komuro-hiraku")

    println("created token = $token")
    assertNotNull(token)
  }

  @Test
  fun `作成した JWT を Verify できる`() {
    val token = target.encodeRS256("komuro-hiraku", mapOf("sample" to "value1"))

    val decodedJWT = target.decodeRS256(token, "komuro-hiraku")
    assertNotNull(decodedJWT)
  }

  @Test
  fun `Issuer が異なる JWT は Verify に失敗する`() {
    val token = target.encodeRS256("valid")

    val exception = assertThrows<FailedVerifyJwtException> {
      target.decodeRS256(token,"invalid")
    }

    assertNotNull(exception)
  }
}