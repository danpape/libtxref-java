# libtxref-java

This is a Java library for converting between bitcoin transaction IDs (txid)
and "Bech32 Encoded Tx Position References" (txref). Txrefs are
described in [BIP 0136](https://github.com/veleslavs/bips/blob/txrev_v2/bip-0136.mediawiki).


## Getting libtxref-java

To use libtxref-java in your project, you can get
the package from Maven Central:

To use the package, you need the following Maven dependency:

```xml
<dependency>
  <groupId>design.contract</groupId>
  <artifactId>libtxref</artifactId>
  <version>1.0.0</version>
</dependency>
```

or download the jar from the Maven repository.

The only external dependencies libtxref-java has are [libbech32-java](https://github.com/dcdpr/libbech32-java) and `JUint.`

## Usage Example

```java
import design.contract.txref.Txref;

public class EncodingExample {

    public static void main(String[] args) {

            int blockHeight = 466793;
            int transactionPosition = 2205;

            // create a simple txref for a testnet transaction
            String txref = Txref.encodeTestnet( blockHeight, transactionPosition);

            System.out.println(txref);
            // prints "txtest1:xjk0-uqay-zat0-dz8"

            int txoIndex = 3;

            // create an extended txref for a testnet transaction
            txref = Txref.encodeTestnet( blockHeight, transactionPosition, txoIndex);

            System.out.println(txref);
            // prints "txtest1:8jk0-uqay-zrqq-23mk-fl"
    }
}
```

## Building libtxref-java

To build libtxref-java, you will need:

* Java (8+)
* Maven

libbech32-java uses a standard maven build process:

```console
mvn install
```

You can also run just the tests without installing:

```console
mvn test
```


