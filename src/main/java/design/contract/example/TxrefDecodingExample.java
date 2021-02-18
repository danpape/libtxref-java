package design.contract.example;

import design.contract.txref.DecodedResult;
import design.contract.txref.Txref;

public class TxrefDecodingExample {

    private static void decodeStandardTxref() {
        // decode a simple txref for a testnet transaction
        DecodedResult decodedResult = Txref.decode("txtest1:xjk0-uqay-zghl-p89");

        assert(decodedResult.getBlockHeight() == 466793);
        assert(decodedResult.getTransactionPosition() == 2205);
        assert(decodedResult.getTxoIndex() == 0); // txoIndex defaults to 0 for simple txrefs
    }

    private static void decodeExtendedTxref() {
        // decode an extended txref for a testnet transaction
        DecodedResult decodedResult = Txref.decode("txtest1:8jk0-uqay-zrqq-ldt6-va");

        assert(decodedResult.getBlockHeight() == 466793);
        assert(decodedResult.getTransactionPosition() == 2205);
        assert(decodedResult.getTxoIndex() == 3);
    }

    private static void decodeStandardTxrefUsingOriginalConstant() {
        // decode a simple txref for a testnet transaction, but this txref had been encoded with
        // the original bech32 internal constant
        DecodedResult decodedResult = Txref.decode("txtest1:xjk0-uqay-zat0-dz8");

        assert(decodedResult.getBlockHeight() == 466793);
        assert(decodedResult.getTransactionPosition() == 2205);
        assert(decodedResult.getTxoIndex() == 0); // txoIndex defaults to 0 for simple txrefs
    }

    public static void main(String[] args) {

        decodeStandardTxref();
        decodeExtendedTxref();
        decodeStandardTxrefUsingOriginalConstant();
    }

}
