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

  test("sign(message: ByteArray, secretKey: ByteArray): ByteArray") { t ->
    val seed = Seed('h', 'e', 'l', 'l', 'o')
    val kp = keyPair(seed.buffer)

    t.throws({ sign(ByteArray(0), ByteArray(0)) }, Error::class)
    t.throws({ sign(ByteArray(0), kp.secretKey) }, Error::class)
    t.throws({ sign("".toUtf8(), kp.secretKey) }, Error::class)

    t.equal(
      toHexString(sign("hello".toUtf8(), kp.secretKey)),
      "2bec9e6f4f388ff390aa276365e6e62ba53d8ebbf1334c749ca32cbb47484835055a493743e191792bbd12da22957c5a72831142a7f0df502d0c733b4ceee20f"
    )

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
