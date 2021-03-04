package design.contract.example;

import design.contract.txref.DecodedResult;
import design.contract.txref.Txref;

public class TxrefDecodingExample {

    /**
     * Decode a simple txref for a testnet transaction
     */
    private static void decodeStandardTxref() {
        DecodedResult decodedResult = Txref.decode("txtest1:xjk0-uqay-zghl-p89");

        assert(decodedResult.getBlockHeight() == 466793);
        assert(decodedResult.getTransactionPosition() == 2205);
        assert(decodedResult.getTxoIndex() == 0);
        assert(decodedResult.getEncoding() == DecodedResult.Encoding.BECH32M);
    }

    /**
     * Decode an extended txref for a testnet transaction
     */
    private static void decodeExtendedTxref() {
        DecodedResult decodedResult = Txref.decode("txtest1:8jk0-uqay-zrqq-ldt6-va");

        assert(decodedResult.getBlockHeight() == 466793);
        assert(decodedResult.getTransactionPosition() == 2205);
        assert(decodedResult.getTxoIndex() == 3);
        assert(decodedResult.getEncoding() == DecodedResult.Encoding.BECH32M);
    }

    /**
     * Decode a simple txref--however, this txref has been encoded with the original bech32 internal
     * constant. The decoding will still succeed, but the library will also return a commentary string
     * that contains an explanatory message along with the txref that should be used instead.
     */
    private static void decodeStandardTxrefUsingOriginalConstant() {
        DecodedResult decodedResult = Txref.decode("txtest1:xjk0-uqay-zat0-dz8");

        assert(decodedResult.getBlockHeight() == 466793);
        assert(decodedResult.getTransactionPosition() == 2205);
        assert(decodedResult.getTxoIndex() == 0);
        assert(decodedResult.getEncoding() == DecodedResult.Encoding.BECH32);

        assert(decodedResult.getCommentary().contains("txtest1:xjk0-uqay-zghl-p89"));
    }

    public static void main(String[] args) {

        decodeStandardTxref();
        decodeExtendedTxref();
        decodeStandardTxrefUsingOriginalConstant();
    }

}
