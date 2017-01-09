package helper

import java.util.Base64
import java.nio.charset.StandardCharsets

object Base64Helper {
  
  def decodeAuthHeader(header: String): Map[String, String] = {
    val encodedStringWithoutBasic = header.substring(6)
    val decodedString = new String(Base64.getDecoder.decode(encodedStringWithoutBasic), StandardCharsets.UTF_8)
    val emailPassArray = decodedString.split(":")
    
    return Map("email" -> emailPassArray(0), "password" -> emailPassArray(1))
  }

}
