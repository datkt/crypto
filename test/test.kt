import datkt.crypto.*
import datkt.tape.*

class Seed {
  val buffer: ByteArray
  constructor(vararg bytes: Char) {
    this.buffer = ByteArray(32)
    for (i in 0 .. bytes.count() - 1) {
      this.buffer.set(i, bytes[i].toByte())
    }
  }
}

fun main(args: Array<String>) {

  test("keyPair(seed: ByteArray? = null): KeyPair") { t ->
    t.throws({ keyPair(ByteArray(30)) }, Error::class)
    run {
      val seed = Seed('h', 'e', 'l', 'l', 'o')
      val kp = keyPair(seed.buffer)

      t.ok(32 == kp.publicKey.size)
      t.ok(64 == kp.secretKey.size)

      t.equal(
        toHexString(kp.publicKey),
        "06bd950857006e5cf4610e6f9569881bf52c34aa21f57a03940c3d7ff2040849")

      t.equal(
        toHexString(kp.secretKey),
        "68656c6c6f00000000000000000000000000000000000000000000000000000006bd950857006e5cf4610e6f9569881bf52c34aa21f57a03940c3d7ff2040849")
    }

    run {
      val kp = keyPair()
      t.ok(32 == kp.publicKey.size)
      t.ok(64 == kp.secretKey.size)
    }

    t.end()
  }

  collect()
}

val table = "0123456789abcdef".toCharArray()

fun toHexString(bytes: ByteArray): String {
  var output = CharArray(2 * bytes.size)
  for (i in bytes.indices) {
    val j = (bytes[i].toInt() and 0xff).toInt()
    output[2 * i] = table[j ushr 4]
    output[1 + 2 * i] = table[j and 0x0f]
  }
  return String(output)
}

fun bufferFrom(vararg bytes: Number): ByteArray {
  return ByteArray(bytes.count()) { i -> bytes[i].toByte() }
}
