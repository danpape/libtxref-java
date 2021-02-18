package design.contract.txref;

// In this "API" test file, we should only be referring to symbols in the immediate "Txref" interface.

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TxrefApiTest {

    @Test
    public void encode_mainnet() {
        assertEquals("tx1:rjk0-uqay-z9l7-m9m",
                Txref.encode( 466793, 2205));
        assertEquals("tx1:rjk0-uqay-z9l7-m9m",
                Txref.encode( 466793, 2205, 0));
        assertEquals("tx1:rjk0-uqay-z9l7-m9m",
                Txref.encode( 466793, 2205, 0, false));
        assertEquals("tx1:rjk0-uqay-z9l7-m9m",
                Txref.encode( 466793, 2205, 0, false, Txref.BECH32_HRP_MAIN));
    }

    @Test
    public void encode_testnet() {
        assertEquals("txtest1:xjk0-uqay-zghl-p89",
                Txref.encodeTestnet( 466793, 2205));
        assertEquals("txtest1:xjk0-uqay-zghl-p89",
                Txref.encodeTestnet( 466793, 2205, 0));
        assertEquals("txtest1:xjk0-uqay-zghl-p89",
                Txref.encodeTestnet( 466793, 2205, 0, false));
        assertEquals("txtest1:xjk0-uqay-zghl-p89",
                Txref.encodeTestnet( 466793, 2205, 0, false, Txref.BECH32_HRP_TEST));
    }


    @Test
    public void encode_extended_mainnet() {
        assertEquals("tx1:yjk0-uqay-zu4x-x22s-y6",
                Txref.encode( 466793, 2205, 0x1ABC));
        assertEquals("tx1:yjk0-uqay-zu4x-x22s-y6",
                Txref.encode( 466793, 2205, 0x1ABC, false));
        assertEquals("tx1:yjk0-uqay-zu4x-x22s-y6",
                Txref.encode( 466793, 2205, 0x1ABC, false, Txref.BECH32_HRP_MAIN));
    }

    @Test
    public void encode_extended_testnet() {
        assertEquals("txtest1:8jk0-uqay-zu4x-gj9m-8a",
                Txref.encodeTestnet( 466793, 2205, 0x1ABC));
        assertEquals("txtest1:8jk0-uqay-zu4x-gj9m-8a",
                Txref.encodeTestnet( 466793, 2205, 0x1ABC, false));
        assertEquals("txtest1:8jk0-uqay-zu4x-gj9m-8a",
                Txref.encodeTestnet( 466793, 2205, 0x1ABC, false, Txref.BECH32_HRP_TEST));
    }

    @Test
    public void decode_mainnet() {
        LocationData ld = Txref.decode("tx1:rjk0-uqay-z9l7-m9m");
        assertEquals(Txref.BECH32_HRP_MAIN, ld.getHrp());
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(466793, ld.getBlockHeight());
        assertEquals(2205, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());
    }

    @Test
    public void decode_testnet() {
        LocationData ld = Txref.decode("txtest1:xjk0-uqay-zghl-p89");
        assertEquals(Txref.BECH32_HRP_TEST, ld.getHrp());
        assertEquals(Txref.MAGIC_BTC_TEST, ld.getMagicCode());
        assertEquals(466793, ld.getBlockHeight());
        assertEquals(2205, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());
    }

    @Test
    public void decode_extended_mainnet() {
        LocationData ld = Txref.decode("tx1:yjk0-uqay-zu4x-x22s-y6");
        assertEquals(Txref.BECH32_HRP_MAIN, ld.getHrp());
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(466793, ld.getBlockHeight());
        assertEquals(2205, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());
    }

    @Test
    public void decode_extended_testnet() {
        LocationData ld = Txref.decode("txtest1:8jk0-uqay-zu4x-gj9m-8a");
        assertEquals(Txref.BECH32_HRP_TEST, ld.getHrp());
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(466793, ld.getBlockHeight());
        assertEquals(2205, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());
    }

    @Test
    public void classifyInputString_empty() {
        assertEquals(Txref.InputParam.unknown_param, Txref.classifyInputString(""));
    }

    @Test
    public void classifyInputString_random() {
        assertEquals(Txref.InputParam.unknown_param, Txref.classifyInputString("oihjediouhwisdubch"));
    }

    @Test
    public void classifyInputString_address() {
        assertEquals(Txref.InputParam.address_param, Txref.classifyInputString("17VZNX1SN5NtKa8UQFxwQbFeFc3iqRYhem"));
        assertEquals(Txref.InputParam.address_param, Txref.classifyInputString("3EktnHQD7RiAE6uzMj2ZifT9YgRrkSgzQX"));
        assertEquals(Txref.InputParam.address_param, Txref.classifyInputString("2MzQwSSnBHWHqSAqtTVQ6v47XtaisrJa1Vc"));
        assertEquals(Txref.InputParam.address_param, Txref.classifyInputString("mzgjzyj9i9JyU5zBQyNBZMkm2QNz2MQ3Se"));
        assertEquals(Txref.InputParam.address_param, Txref.classifyInputString("mxgj4vFNNWPdRb45tHoJoVqfahYkc3QYZ4"));
        assertEquals(Txref.InputParam.unknown_param, Txref.classifyInputString("mxgj4vFNNWPdRb45tHoJoVqfahYk8ec3QYZ4"));
    }

    @Test
    public void classifyInputString_bad_address() {
        // too long
        assertEquals(Txref.InputParam.unknown_param, Txref.classifyInputString("17VZNX1SN5NtKa8UQFxwQbFeFc3iqRYhemse"));
        assertEquals(Txref.InputParam.unknown_param, Txref.classifyInputString("3EktnHQD7RiAE6uzMj2ZifT9YgRrkSgzQXdd"));
        assertEquals(Txref.InputParam.unknown_param, Txref.classifyInputString("2MzQwSSnBHWHqSAqtTVQ6v47XtaisrJa1Vcd"));
        assertEquals(Txref.InputParam.unknown_param, Txref.classifyInputString("mzgjzyj9i9JyU5zBQyNBZMkm2QNz2MQ3Sedd"));
        assertEquals(Txref.InputParam.unknown_param, Txref.classifyInputString("mxgj4vFNNWPdRb45tHoJoVqfahYkc3QYZ4dd"));
        assertEquals(Txref.InputParam.unknown_param, Txref.classifyInputString("mxgj4vFNNWPdRb45tHoJoVqfahYk8ec3QYZ4"));
        // too short
        assertEquals(Txref.InputParam.unknown_param, Txref.classifyInputString("17VZNX1SN5NtKa8UQFxwQbFeF"));
        assertEquals(Txref.InputParam.unknown_param, Txref.classifyInputString("3EktnHQD7RiAE6uzMj2ZffT9Y"));
        assertEquals(Txref.InputParam.unknown_param, Txref.classifyInputString("2MzQwSSnBHWHqSAqtTVQ6v47X"));
    }

    @Test
    public void classifyInputString_anomolies() {
        // classifyInputString isn't perfect. Here are some examples where it is wrong

        // should be "unknown_param" since these are too-short bitcoin addresses, but they happen
        // to have the right number of characters after being cleaned of invalid characters
        assertEquals(Txref.InputParam.txref_param, Txref.classifyInputString("mzgjzyj9i9JyU5zBQyNBZMkm2"));
        assertEquals(Txref.InputParam.txref_param, Txref.classifyInputString("mxgj4vFNNWPdRb45tHoJoVqfa"));
    }

    @Test
    public void classifyInputString_txid() {
        assertEquals(Txref.InputParam.txid_param, Txref.classifyInputString("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"));
        assertEquals(Txref.InputParam.unknown_param, Txref.classifyInputString("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca4953991b7852b855"));
    }

    @Test
    public void classifyInputString_txref() {
        // mainnet
        assertEquals(Txref.InputParam.txref_param, Txref.classifyInputString("tx1rqqqqqqqqwtvvjr"));
        assertEquals(Txref.InputParam.txref_param, Txref.classifyInputString("rqqqqqqqqwtvvjr"));
        // testnet
        assertEquals(Txref.InputParam.txref_param, Txref.classifyInputString("txtest1xjk0uqayzghlp89"));
        assertEquals(Txref.InputParam.txref_param, Txref.classifyInputString("xjk0uqayzghlp89"));
    }

    @Test
    public void classifyInputString_txrefext() {
        // mainnet
        assertEquals(Txref.InputParam.txrefext_param, Txref.classifyInputString("tx1yqqqqqqqqqqqrvum0c"));
        assertEquals(Txref.InputParam.txrefext_param, Txref.classifyInputString("yqqqqqqqqqqqrvum0c"));
        // testnet
        assertEquals(Txref.InputParam.txrefext_param, Txref.classifyInputString("txtest18jk0uqayzu4xgj9m8a"));
        assertEquals(Txref.InputParam.txrefext_param, Txref.classifyInputString("8jk0uqayzu4xgj9m8a"));
    }

}
