package design.contract.txref;

// In this "API" test file, we should only be referring to symbols in the immediate "Txref" interface.

import org.junit.Test;

import static org.junit.Assert.*;

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
    public void encode_regtest() {
        assertEquals("txrt1:q7ll-llll-lps4-p3p",
                Txref.encodeRegtest( 0xFFFFFF, 0x7FFF));
        assertEquals("txrt1:q7ll-llll-lps4-p3p",
                Txref.encodeRegtest( 0xFFFFFF, 0x7FFF, 0));
        assertEquals("txrt1:q7ll-llll-lps4-p3p",
                Txref.encodeRegtest( 0xFFFFFF, 0x7FFF, 0, false));
        assertEquals("txrt1:q7ll-llll-lps4-p3p",
                Txref.encodeRegtest( 0xFFFFFF, 0x7FFF, 0, false, Txref.BECH32_HRP_REGTEST));
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
    public void encode_extended_regtest() {
        assertEquals("txrt1:p7ll-llll-lpqq-qa0d-vp",
                Txref.encodeRegtest( 0xFFFFFF, 0x7FFF, 1));
        assertEquals("txrt1:p7ll-llll-lpqq-qa0d-vp",
                Txref.encodeRegtest( 0xFFFFFF, 0x7FFF, 1, false));
        assertEquals("txrt1:p7ll-llll-lpqq-qa0d-vp",
                Txref.encodeRegtest( 0xFFFFFF, 0x7FFF, 1, false, Txref.BECH32_HRP_REGTEST));
    }

    @Test
    public void decode_mainnet() {
        DecodedResult decodedResult = Txref.decode("tx1:rjk0-uqay-z9l7-m9m");
        assertEquals(Txref.BECH32_HRP_MAIN, decodedResult.getHrp());
        assertEquals(Txref.MAGIC_BTC_MAIN, decodedResult.getMagicCode());
        assertEquals(466793, decodedResult.getBlockHeight());
        assertEquals(2205, decodedResult.getTransactionIndex());
        assertEquals(0, decodedResult.getTxoIndex());
        assertEquals(DecodedResult.Encoding.BECH32M, decodedResult.getEncoding());
        assertEquals("", decodedResult.getCommentary());
    }

    @Test
    public void decode_testnet() {
        DecodedResult decodedResult = Txref.decode("txtest1:xjk0-uqay-zghl-p89");
        assertEquals(Txref.BECH32_HRP_TEST, decodedResult.getHrp());
        assertEquals(Txref.MAGIC_BTC_TEST, decodedResult.getMagicCode());
        assertEquals(466793, decodedResult.getBlockHeight());
        assertEquals(2205, decodedResult.getTransactionIndex());
        assertEquals(0, decodedResult.getTxoIndex());
        assertEquals(DecodedResult.Encoding.BECH32M, decodedResult.getEncoding());
        assertEquals("", decodedResult.getCommentary());
    }

    @Test
    public void decode_regtest() {
        DecodedResult decodedResult = Txref.decode("txrt1:q7ll-llll-lps4-p3p");
        assertEquals(Txref.BECH32_HRP_REGTEST, decodedResult.getHrp());
        assertEquals(Txref.MAGIC_BTC_REGTEST, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionIndex());
        assertEquals(0, decodedResult.getTxoIndex());
        assertEquals(DecodedResult.Encoding.BECH32M, decodedResult.getEncoding());
        assertEquals("", decodedResult.getCommentary());
    }

    @Test
    public void decode_extended_mainnet() {
        DecodedResult decodedResult = Txref.decode("tx1:yjk0-uqay-zu4x-x22s-y6");
        assertEquals(Txref.BECH32_HRP_MAIN, decodedResult.getHrp());
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(466793, decodedResult.getBlockHeight());
        assertEquals(2205, decodedResult.getTransactionIndex());
        assertEquals(0x1ABC, decodedResult.getTxoIndex());
        assertEquals(DecodedResult.Encoding.BECH32M, decodedResult.getEncoding());
        assertEquals("", decodedResult.getCommentary());
    }

    @Test
    public void decode_extended_testnet() {
        DecodedResult decodedResult = Txref.decode("txtest1:8jk0-uqay-zu4x-gj9m-8a");
        assertEquals(Txref.BECH32_HRP_TEST, decodedResult.getHrp());
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(466793, decodedResult.getBlockHeight());
        assertEquals(2205, decodedResult.getTransactionIndex());
        assertEquals(0x1ABC, decodedResult.getTxoIndex());
        assertEquals(DecodedResult.Encoding.BECH32M, decodedResult.getEncoding());
        assertEquals("", decodedResult.getCommentary());
    }

    @Test
    public void decode_extended_regtest() {
        DecodedResult decodedResult = Txref.decode("txrt1:p7ll-llll-lpqq-qa0d-vp");
        assertEquals(Txref.BECH32_HRP_REGTEST, decodedResult.getHrp());
        assertEquals(Txref.MAGIC_BTC_REGTEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionIndex());
        assertEquals(1, decodedResult.getTxoIndex());
        assertEquals(DecodedResult.Encoding.BECH32M, decodedResult.getEncoding());
        assertEquals("", decodedResult.getCommentary());
    }

    @Test
    public void decode_extended_testnet_original_checksum() {
        DecodedResult decodedResult = Txref.decode("txtest1:8jk0-uqay-zu4x-aw4h-zl");
        assertEquals(Txref.BECH32_HRP_TEST, decodedResult.getHrp());
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(466793, decodedResult.getBlockHeight());
        assertEquals(2205, decodedResult.getTransactionIndex());
        assertEquals(0x1ABC, decodedResult.getTxoIndex());
        assertEquals(DecodedResult.Encoding.BECH32, decodedResult.getEncoding());
        assertNotEquals("", decodedResult.getCommentary());
        assertTrue(decodedResult.getCommentary().contains("txtest1:8jk0-uqay-zu4x-gj9m-8a"));
    }

    @Test
    public void classifyInputString_empty() {
        assertEquals(Txref.InputParam.UNKNOWN, Txref.classifyInputString(""));
    }

    @Test
    public void classifyInputString_random() {
        assertEquals(Txref.InputParam.UNKNOWN, Txref.classifyInputString("oihjediouhwisdubch"));
    }

    @Test
    public void classifyInputString_address() {
        assertEquals(Txref.InputParam.ADDRESS, Txref.classifyInputString("17VZNX1SN5NtKa8UQFxwQbFeFc3iqRYhem"));
        assertEquals(Txref.InputParam.ADDRESS, Txref.classifyInputString("3EktnHQD7RiAE6uzMj2ZifT9YgRrkSgzQX"));
        assertEquals(Txref.InputParam.ADDRESS, Txref.classifyInputString("2MzQwSSnBHWHqSAqtTVQ6v47XtaisrJa1Vc"));
        assertEquals(Txref.InputParam.ADDRESS, Txref.classifyInputString("mzgjzyj9i9JyU5zBQyNBZMkm2QNz2MQ3Se"));
        assertEquals(Txref.InputParam.ADDRESS, Txref.classifyInputString("mxgj4vFNNWPdRb45tHoJoVqfahYkc3QYZ4"));
        assertEquals(Txref.InputParam.UNKNOWN, Txref.classifyInputString("mxgj4vFNNWPdRb45tHoJoVqfahYk8ec3QYZ4"));
    }

    @Test
    public void classifyInputString_bad_address() {
        // too long
        assertEquals(Txref.InputParam.UNKNOWN, Txref.classifyInputString("17VZNX1SN5NtKa8UQFxwQbFeFc3iqRYhemse"));
        assertEquals(Txref.InputParam.UNKNOWN, Txref.classifyInputString("3EktnHQD7RiAE6uzMj2ZifT9YgRrkSgzQXdd"));
        assertEquals(Txref.InputParam.UNKNOWN, Txref.classifyInputString("2MzQwSSnBHWHqSAqtTVQ6v47XtaisrJa1Vcd"));
        assertEquals(Txref.InputParam.UNKNOWN, Txref.classifyInputString("mzgjzyj9i9JyU5zBQyNBZMkm2QNz2MQ3Sedd"));
        assertEquals(Txref.InputParam.UNKNOWN, Txref.classifyInputString("mxgj4vFNNWPdRb45tHoJoVqfahYkc3QYZ4dd"));
        assertEquals(Txref.InputParam.UNKNOWN, Txref.classifyInputString("mxgj4vFNNWPdRb45tHoJoVqfahYk8ec3QYZ4"));
        // too short
        assertEquals(Txref.InputParam.UNKNOWN, Txref.classifyInputString("17VZNX1SN5NtKa8UQFxwQbFeF"));
        assertEquals(Txref.InputParam.UNKNOWN, Txref.classifyInputString("3EktnHQD7RiAE6uzMj2ZffT9Y"));
        assertEquals(Txref.InputParam.UNKNOWN, Txref.classifyInputString("2MzQwSSnBHWHqSAqtTVQ6v47X"));
    }

    @Test
    public void classifyInputString_anomolies() {
        // classifyInputString isn't perfect. Here are some examples where it is wrong

        // should be "unknown_param" since these are too-short bitcoin addresses, but they happen
        // to have the right number of characters after being cleaned of invalid characters
        assertEquals(Txref.InputParam.TXREF, Txref.classifyInputString("mzgjzyj9i9JyU5zBQyNBZMkm2"));
        assertEquals(Txref.InputParam.TXREF, Txref.classifyInputString("mxgj4vFNNWPdRb45tHoJoVqfa"));
    }

    @Test
    public void classifyInputString_txid() {
        assertEquals(Txref.InputParam.TXID, Txref.classifyInputString("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"));
        assertEquals(Txref.InputParam.UNKNOWN, Txref.classifyInputString("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca4953991b7852b855"));
    }

    @Test
    public void classifyInputString_txref() {
        // mainnet
        assertEquals(Txref.InputParam.TXREF, Txref.classifyInputString("tx1rqqqqqqqqwtvvjr"));
        assertEquals(Txref.InputParam.TXREF, Txref.classifyInputString("rqqqqqqqqwtvvjr"));
        // testnet
        assertEquals(Txref.InputParam.TXREF, Txref.classifyInputString("txtest1xjk0uqayzghlp89"));
        assertEquals(Txref.InputParam.TXREF, Txref.classifyInputString("xjk0uqayzghlp89"));
        // regtest
        assertEquals(Txref.InputParam.TXREF, Txref.classifyInputString("txrt1q7lllllllps4p3p"));
        assertEquals(Txref.InputParam.TXREF, Txref.classifyInputString("q7lllllllps4p3p"));
    }

    @Test
    public void classifyInputString_txrefext() {
        // mainnet
        assertEquals(Txref.InputParam.TXREFEXT, Txref.classifyInputString("tx1yqqqqqqqqqqqrvum0c"));
        assertEquals(Txref.InputParam.TXREFEXT, Txref.classifyInputString("yqqqqqqqqqqqrvum0c"));
        // testnet
        assertEquals(Txref.InputParam.TXREFEXT, Txref.classifyInputString("txtest18jk0uqayzu4xgj9m8a"));
        assertEquals(Txref.InputParam.TXREFEXT, Txref.classifyInputString("8jk0uqayzu4xgj9m8a"));
        // regtest
        assertEquals(Txref.InputParam.TXREFEXT, Txref.classifyInputString("txrt1p7lllllllpqqqa0dvp"));
        assertEquals(Txref.InputParam.TXREFEXT, Txref.classifyInputString("p7lllllllpqqqa0dvp"));
    }

}
