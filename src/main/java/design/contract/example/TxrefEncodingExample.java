package design.contract.example;

import design.contract.txref.Txref;

public class TxrefEncodingExample {

    /**
     * Create a txref for a mainnet transaction, with only a blockHeight and transactionPosition
     */
    private static void createStandardMainnetTxref() {
        int blockHeight = 170;
        int transactionPosition = 1;

        String txref = Txref.encode(blockHeight, transactionPosition);

        System.out.println(txref);
        // prints "tx1:r52q-qqpq-qpty-cfg"
    }

    /**
     * Create a txref for a testnet transaction, with only a blockHeight and transactionPosition
     */
    private static void createStandardTestnetTxref() {
        int blockHeight = 170;
        int transactionPosition = 1;

        String txref = Txref.encodeTestnet(blockHeight, transactionPosition);

        System.out.println(txref);
        // prints "txtest1:x52q-qqpq-qvr9-ztk"
    }

    /**
     * Create an extended txref for a mainnet transaction, with a blockHeight and
     * transactionPosition and a specific txoIndex of 3
     */
    private static void createExtendedMainnetTxref() {
        int blockHeight = 170;
        int transactionPosition = 1;
        int txoIndex = 1;

        String txref = Txref.encode(blockHeight, transactionPosition, txoIndex);

        System.out.println(txref);
        // prints "tx1:y52q-qqpq-qpqq-4lkz-zc"
    }

    /**
     * Create an extended txref for a testnet transaction, with a blockHeight and
     * transactionPosition and a specific txoIndex of 3
     */
    private static void createExtendedTestnetTxref() {
        int blockHeight = 170;
        int transactionPosition = 1;
        int txoIndex = 1;

        String txref = Txref.encodeTestnet(blockHeight, transactionPosition, txoIndex);

        System.out.println(txref);
        // prints "txtest1:852q-qqpq-qpqq-m8ef-pl"
    }

    public static void main(String[] args) {

        createStandardMainnetTxref();
        createStandardTestnetTxref();
        createExtendedMainnetTxref();
        createExtendedTestnetTxref();

    }

}
