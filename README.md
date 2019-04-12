# libtxref-java

This is a Java library for converting between bitcoin transaction IDs (txid)
and "Bech32 Encoded Tx Position References" (txref). Txrefs are
described in [BIP 0136](https://github.com/veleslavs/bips/blob/txrev_v2/bip-0136.mediawiki).


## Getting and using libtxref-java

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


