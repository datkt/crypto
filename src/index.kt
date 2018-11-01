package datkt.crypto

import kotlinx.cinterop.*

//import datkt.sodium.crypto_generichash_batch
import datkt.sodium.crypto_generichash
import datkt.sodium.crypto_sign_verify_detached
import datkt.sodium.crypto_sign_seed_keypair
import datkt.sodium.crypto_sign_detached
import datkt.sodium.crypto_sign_keypair
import datkt.sodium.randombytes_buf

import datkt.sodium.crypto_sign_PUBLICKEYBYTES
import datkt.sodium.crypto_sign_SECRETKEYBYTES
import datkt.sodium.crypto_sign_BYTES

/**
 * A simple data class container for a public and secret key pair
 */
data class KeyPair(val publicKey: ByteArray, val secretKey: ByteArray)

/**
 * Generates a ed25519 key pair suitable for creating signatures.
 */
fun keyPair(seed: ByteArray? = null): KeyPair {
  if (null != seed && seed.size < 32) {
    throw Error("Seed cannot be less than 32 bytes.")
  }

  val publicKey = ByteArray(crypto_sign_PUBLICKEYBYTES.toInt())
  val secretKey = ByteArray(crypto_sign_SECRETKEYBYTES.toInt())

  publicKey.usePinned { publicKeyPointer ->
    secretKey.usePinned { secretKeyPointer ->
      if (null != seed) {
        seed.usePinned { seedPointer ->
          crypto_sign_seed_keypair(
              publicKeyPointer.addressOf(0) as CValuesRef<UByteVar>,
              secretKeyPointer.addressOf(0) as CValuesRef<UByteVar>,
              seedPointer.addressOf(0) as CValuesRef<UByteVar>)
        }
      } else {
        crypto_sign_keypair(
          publicKeyPointer.addressOf(0) as CValuesRef<UByteVar>,
          secretKeyPointer.addressOf(0) as CValuesRef<UByteVar>)
      }
    }
  }

  return KeyPair(publicKey, secretKey)
}
