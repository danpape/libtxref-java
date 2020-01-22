package design.contract.example;

import design.contract.txref.Txref;

public class TxrefEncodingExample {

    private static void createStandardTxref() {
        int blockHeight = 466793;
        int transactionPosition = 2205;

        // create a simple txref for a testnet transaction, with only a
        // blockHeight and transactionPosition
        String txref = Txref.encodeTestnet( blockHeight, transactionPosition);

        System.out.println(txref);
        // prints "txtest1:xjk0-uqay-zz5s-jae"
    }

    private static void createExtendedTxref() {
        int blockHeight = 466793;
        int transactionPosition = 2205;
        int txoIndex = 3;

        // create an extended txref for a testnet transaction, with a
        // blockHeight and transactionPosition and a specific txoIndex of 3
        String txref = Txref.encodeTestnet( blockHeight, transactionPosition, txoIndex);

        System.out.println(txref);
        // prints "txtest1:8jk0-uqay-zrqq-4wyf-kp"
    }

    public static void main(String[] args) {

        createStandardTxref();
        createExtendedTxref();

    }

}
