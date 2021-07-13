# libtxref-java

This is a Java library for converting between bitcoin transaction IDs (txid)
and "Bech32 Encoded Tx Position References" (txref). Txrefs are
described in [BIP 0136](https://github.com/bitcoin/bips/blob/master/bip-0136.mediawiki).

The only external dependencies libtxref-java has are [libbech32-java](https://github.com/dcdpr/libbech32-java) and `JUnit.`

## Getting libtxref-java

To use libtxref-java in your project, you can get
the package from Maven Central:

To use the package, you need the following Maven dependency:

```xml
<dependency>
  <groupId>design.contract</groupId>
  <artifactId>libtxref</artifactId>
  <version>1.1.0</version>
</dependency>
```

## Usage Examples

### Encoding Examples

See [the full code for the following examples](src/main/java/design/contract/example/TxrefEncodingExample.java).

#### Create a txref for a mainnet transaction, with only a blockHeight and transactionIndex:

```java
private static void createStandardMainnetTxref() {
    int blockHeight = 170;
    int transactionIndex = 1;

    String txref = Txref.encode(blockHeight, transactionIndex);

    System.out.println(txref);
    // prints "tx1:r52q-qqpq-qpty-cfg"
}
```

#### Create a txref for a testnet transaction, with only a blockHeight and transactionIndex:

```java
private static void createStandardTestnetTxref() {
    int blockHeight = 170;
    int transactionIndex = 1;

    String txref = Txref.encodeTestnet(blockHeight, transactionIndex);

    System.out.println(txref);
    // prints "txtest1:x52q-qqpq-qvr9-ztk"
}
```

#### Create an extended txref for a mainnet transaction, with a blockHeight and transactionIndex and a specific txoIndex:

```java
private static void createExtendedMainnetTxref() {
    int blockHeight = 170;
    int transactionIndex = 1;
    int txoIndex = 1;

    String txref = Txref.encode(blockHeight, transactionIndex, txoIndex);

    System.out.println(txref);
    // prints "tx1:y52q-qqpq-qpqq-4lkz-zc"
}
```

#### Create an extended txref for a testnet transaction, with a blockHeight and transactionIndex and a specific txoIndex:

```java
private static void createExtendedTestnetTxref() {
    int blockHeight = 170;
    int transactionIndex = 1;
    int txoIndex = 1;

    String txref = Txref.encodeTestnet(blockHeight, transactionIndex, txoIndex);

    System.out.println(txref);
    // prints "txtest1:852q-qqpq-qpqq-m8ef-pl"
}
```

### Decoding Examples

See [the full code for the following examples](src/main/java/design/contract/example/TxrefDecodingExample.java).

#### Decode a txref

```java
private static void decodeTxref() {
        DecodedResult decodedResult = Txref.decode("tx1:r52q-qqpq-qpty-cfg");

        assert(decodedResult.getBlockHeight() == 170);
        assert(decodedResult.getTransactionIndex() == 1);
        assert(decodedResult.getTxoIndex() == 0);
        assert(decodedResult.getEncoding() == DecodedResult.Encoding.BECH32M);
        }
```

#### Decode an "original" txref

This txref has been encoded with the original bech32 internal constant. The 
decoding will still succeed, but the library will also return a commentary 
string that contains an explanatory message along with the txref that should 
be used instead.

```java
    private static void decodeStandardTxrefUsingOriginalConstant() {
        DecodedResult decodedResult = Txref.decode("tx1:r52q-qqpq-q5h5-5v2");

        assert(decodedResult.getBlockHeight() == 170);
        assert(decodedResult.getTransactionIndex() == 1);
        assert(decodedResult.getTxoIndex() == 0);
        assert(decodedResult.getEncoding() == DecodedResult.Encoding.BECH32);

        System.out.println(decodedResult.getCommentary());
        // prints:
        // "The txref tx1:r52q-qqpq-q5h5-5v2 uses an old encoding scheme and should be updated to tx1:r52q-qqpq-qpty-cfg See https://github.com/dcdpr/libtxref-java#regarding-bech32-checksums for more information."
        }
```

## Building libtxref-java

To build libtxref-java, you will need:

* Java (8+)
* Maven

libtxref-java uses a standard maven build process:

```console
mvn install
```

You can also run just the tests without installing:

```console
mvn test
```

## Regarding bech32 checksums

The Bech32 data encoding format was first proposed by Pieter Wuille in early 2017 in
[BIP 0173](https://github.com/bitcoin/bips/blob/master/bip-0173.mediawiki). Later, in November 2019, Pieter published
some research that a constant used in the bech32 checksum algorithm (value = 1) may not be
optimal for the error detecting properties of bech32. In February 2021, Pieter published
[BIP 0350](https://github.com/bitcoin/bips/blob/master/bip-0350.mediawiki) reporting that "exhaustive analysis" showed the best possible constant value is
0x2bc830a3. This improved variant of Bech32 is called "Bech32m".

When decoding a txref, libtxref-java returns an enum value showing whether bech32m or bech32
was used to encode. If the original bech32 variant is detected, libtxref-java also returns a 
commentary string that can be shown to the user. This commentary will contain a new txref that represents
the same transaction data, but using the new bech32m variant. This can be seen in the examples above.

When encoding data, libtxref-java will only use the new constant value of 0x2bc830a3.

