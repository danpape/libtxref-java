package design.contract.example;

import design.contract.txref.LocationData;
import design.contract.txref.Txref;

public class TxrefDecodingExample {

    public static void main(String[] args) {

        {
            // decode a simple txref for a testnet transaction
            LocationData locationData = Txref.decode("txtest1:xjk0-uqay-zat0-dz8");

            assert(locationData.getBlockHeight() == 466793);
            assert(locationData.getTransactionPosition() == 2205);
            assert(locationData.getTxoIndex() == 0); // txoIndex defaults to 0 for simple txrefs
        }

        {
            // decode an extended txref for a testnet transaction
            LocationData locationData = Txref.decode("txtest1:8jk0-uqay-zrqq-23mk-fl");

            assert(locationData.getBlockHeight() == 466793);
            assert(locationData.getTransactionPosition() == 2205);
            assert(locationData.getTxoIndex() == 3);
        }

    }
}
