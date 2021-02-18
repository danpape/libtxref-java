package design.contract.example;

import design.contract.txref.LocationData;
import design.contract.txref.Txref;

public class TxrefDecodingExample {

    private static void decodeStandardTxref() {
        // decode a simple txref for a testnet transaction
        LocationData locationData = Txref.decode("txtest1:xjk0-uqay-zghl-p89");

        assert(locationData.getBlockHeight() == 466793);
        assert(locationData.getTransactionPosition() == 2205);
        assert(locationData.getTxoIndex() == 0); // txoIndex defaults to 0 for simple txrefs
    }

    private static void decodeExtendedTxref() {
        // decode an extended txref for a testnet transaction
        LocationData locationData = Txref.decode("txtest1:8jk0-uqay-zrqq-ldt6-va");

        assert(locationData.getBlockHeight() == 466793);
        assert(locationData.getTransactionPosition() == 2205);
        assert(locationData.getTxoIndex() == 3);
    }

    private static void decodeStandardTxrefUsingOriginalConstant() {
        // decode a simple txref for a testnet transaction, but this txref had been encoded with
        // the original bech32 internal constant
        LocationData locationData = Txref.decode("txtest1:xjk0-uqay-zat0-dz8");

        assert(locationData.getBlockHeight() == 466793);
        assert(locationData.getTransactionPosition() == 2205);
        assert(locationData.getTxoIndex() == 0); // txoIndex defaults to 0 for simple txrefs
    }

    public static void main(String[] args) {

        decodeStandardTxref();
        decodeExtendedTxref();
        decodeStandardTxrefUsingOriginalConstant();
    }

}
