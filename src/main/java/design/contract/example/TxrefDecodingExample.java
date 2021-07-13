package design.contract.example;

import design.contract.txref.DecodedResult;
import design.contract.txref.Txref;

public class TxrefDecodingExample {

    /**
     * Decode a simple txref for a mainnet transaction
     */
    private static void decodeStandardTxref() {
        DecodedResult decodedResult = Txref.decode("tx1:r52q-qqpq-qpty-cfg");

        assert(decodedResult.getBlockHeight() == 170);
        assert(decodedResult.getTransactionIndex() == 1);
        assert(decodedResult.getTxoIndex() == 0);
        assert(decodedResult.getEncoding() == DecodedResult.Encoding.BECH32M);
    }

    /**
     * Decode an extended txref for a mainnet transaction
     */
    private static void decodeExtendedTxref() {
        DecodedResult decodedResult = Txref.decode("tx1:y52q-qqpq-qpqq-4lkz-zc");

        assert(decodedResult.getBlockHeight() == 170);
        assert(decodedResult.getTransactionIndex() == 1);
        assert(decodedResult.getTxoIndex() == 1);
        assert(decodedResult.getEncoding() == DecodedResult.Encoding.BECH32M);
    }

    /**
     * Decode a simple txref--however, this txref has been encoded with the original bech32 internal
     * constant. The decoding will still succeed, but the library will also return a commentary string
     * that contains an explanatory message along with the txref that should be used instead.
     */
    private static void decodeStandardTxrefUsingOriginalConstant() {
        DecodedResult decodedResult = Txref.decode("tx1:r52q-qqpq-q5h5-5v2");

        assert(decodedResult.getBlockHeight() == 170);
        assert(decodedResult.getTransactionIndex() == 1);
        assert(decodedResult.getTxoIndex() == 0);
        assert(decodedResult.getEncoding() == DecodedResult.Encoding.BECH32);

        assert(decodedResult.getCommentary().contains("tx1:r52q-qqpq-qpty-cfg"));
    }

    public static void main(String[] args) {

        decodeStandardTxref();
        decodeExtendedTxref();
        decodeStandardTxrefUsingOriginalConstant();
    }

}
