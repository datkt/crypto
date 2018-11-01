crypto
======

Various cryptographic function for datk.

## Installation

The `datkt.crypto` package an be installed with NPM.

```sh
$ npm install @datkt/crypto
```

## Prerequisites

* [Kotlin/Native](https://github.com/JetBrains/kotlin-native) and the
  `konanc` command line program.

## Usage

```sh
## Compile a program in 'main.kt' and link crypto.klib found in `node_modules/`
$ konanc -r node_modules/@datkt -l crypto/crypto main.kt
```

where `main.kt` might be

```kotlin
import datkt.crypto.*

fun main(args: Array<String>) {
  val kp = keyPair()
  // @TODO
}
```

## API

### `data class KeyPair(val publicKey: ByteArray, val secretKey: ByteArray)`

A simple data class container for a public and secret key pair

### `keyPair(seed: ByteArray? = null): KeyPair`

Generates a ed25519 key pair suitable for creating signatures.

```kotlin
val kp = keyPair("some random seed value".toUtf8())
```

or

```kotlin
val kp = keyPair()
```

## Tests

To run the tests, make sure the `crypto.klib` Kotlin library is built by
running the following command.

```sh
$ npm run build
```

When the library is built, run the following command to run the tests.

```sh
$ npm test
```

## See Also

* https://github.com/mafintosh/hypercore-crypto

## License

MIT
