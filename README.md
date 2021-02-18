# libtxref-java

This is a Java library for converting between bitcoin transaction IDs (txid)
and "Bech32 Encoded Tx Position References" (txref). Txrefs are
described in [BIP 0136](https://github.com/veleslavs/bips/blob/txrev_v2/bip-0136.mediawiki).

The only external dependencies libtxref-java has are [libbech32-java](https://github.com/dcdpr/libbech32-java) and `JUnit.`

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
            // prints "txtest1:xjk0-uqay-zghl-p89"

            int txoIndex = 3;

            // create an extended txref for a testnet transaction
            txref = Txref.encodeTestnet( blockHeight, transactionPosition, txoIndex);

            System.out.println(txref);
            // prints "txtest1:8jk0-uqay-zrqq-4wyf-kp"
    }
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


