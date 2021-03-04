package design.contract.example;

import design.contract.txref.Txref;

public class TxrefEncodingExample {

    /**
     * Create a txref for a mainnet transaction, with only a blockHeight and transactionPosition
     */
    private static void createStandardMainnetTxref() {
        int blockHeight = 466793;
        int transactionPosition = 2205;

        String txref = Txref.encode(blockHeight, transactionPosition);

        System.out.println(txref);
        // prints "tx1:rjk0-uqay-z9l7-m9m"
    }

    /**
     * Create a txref for a testnet transaction, with only a blockHeight and transactionPosition
     */
    private static void createStandardTestnetTxref() {
        int blockHeight = 466793;
        int transactionPosition = 2205;

        String txref = Txref.encodeTestnet(blockHeight, transactionPosition);

        System.out.println(txref);
        // prints "txtest1:xjk0-uqay-zghl-p89"
    }

    /**
     * Create an extended txref for a mainnet transaction, with a blockHeight and
     * transactionPosition and a specific txoIndex of 3
     */
    private static void createExtendedMainnetTxref() {
        int blockHeight = 466793;
        int transactionPosition = 2205;
        int txoIndex = 3;

        String txref = Txref.encode(blockHeight, transactionPosition, txoIndex);

        System.out.println(txref);
        // prints "tx1:yjk0-uqay-zrqq-34y3-06"
    }

    /**
     * Create an extended txref for a testnet transaction, with a blockHeight and
     * transactionPosition and a specific txoIndex of 3
     */
    private static void createExtendedTestnetTxref() {
        int blockHeight = 466793;
        int transactionPosition = 2205;
        int txoIndex = 3;

        String txref = Txref.encodeTestnet(blockHeight, transactionPosition, txoIndex);

        System.out.println(txref);
        // prints "txtest1:8jk0-uqay-zrqq-ldt6-va"
    }

    public static void main(String[] args) {

        createStandardMainnetTxref();
        createStandardTestnetTxref();
        createExtendedMainnetTxref();
        createExtendedTestnetTxref();

    }

}
