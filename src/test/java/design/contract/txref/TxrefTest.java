package design.contract.txref;

import design.contract.bech32.Bech32;
import design.contract.bech32.DecodedResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TxrefTest {

    @Test
    public void checkBlockHeightRange_forValuesInRange_wontThrow() {
        Txref.impl.checkBlockHeightRange(0);
        Txref.impl.checkBlockHeightRange(1);
        Txref.impl.checkBlockHeightRange(Txref.limits.MAX_BLOCK_HEIGHT);
    }

    @Test(expected = RuntimeException.class)
    public void checkBlockHeightRange_forValueBeforeRange_throws() {
        Txref.impl.checkBlockHeightRange(-1);
    }

    @Test(expected = RuntimeException.class)
    public void checkBlockHeightRange_forValueAfterRange_throws() {
        Txref.impl.checkBlockHeightRange(Txref.limits.MAX_BLOCK_HEIGHT + 1);
    }

    @Test
    public void checkTransactionPositionRange_forValuesInRange_wontThrow() {
        Txref.impl.checkTransactionPositionRange(0);
        Txref.impl.checkTransactionPositionRange(1);
        Txref.impl.checkTransactionPositionRange(Txref.limits.MAX_TRANSACTION_POSITION);
    }

    @Test(expected = RuntimeException.class)
    public void checkTransactionPositionRange_forValueBeforeRange_throws() {
        Txref.impl.checkTransactionPositionRange(-1);
    }

    @Test(expected = RuntimeException.class)
    public void checkTransactionPositionRange_forValueAfterRange_throws() {
        Txref.impl.checkTransactionPositionRange(Txref.limits.MAX_TRANSACTION_POSITION + 1);
    }

    @Test
    public void checkTxoIndexRange_forValuesInRange_wontThrow() {
        Txref.impl.checkTxoIndexRange(0);
        Txref.impl.checkTxoIndexRange(1);
        Txref.impl.checkTxoIndexRange(Txref.limits.MAX_TXO_INDEX);
    }

    @Test(expected = RuntimeException.class)
    public void checkTxoIndexRange_forValueBeforeRange_throws() {
        Txref.impl.checkTxoIndexRange(-1);
    }

    @Test(expected = RuntimeException.class)
    public void checkTxoIndexRange_forValueAfterRange_throws() {
        Txref.impl.checkTxoIndexRange(Txref.limits.MAX_TXO_INDEX + 1);
    }

    @Test
    public void checkMagicCodeRange_forValuesInRange_wontThrow() {
        Txref.impl.checkMagicCodeRange(0);
        Txref.impl.checkMagicCodeRange(1);
        Txref.impl.checkMagicCodeRange(Txref.limits.MAX_MAGIC_CODE);
    }

    @Test(expected = RuntimeException.class)
    public void checkMagicCodeRange_forValueBeforeRange_throws() {
        Txref.impl.checkMagicCodeRange(-1);
    }

    @Test(expected = RuntimeException.class)
    public void checkMagicCodeRange_forValueAfterRange_throws() {
        Txref.impl.checkMagicCodeRange(Txref.limits.MAX_MAGIC_CODE + 1);
    }

    @Test(expected = RuntimeException.class)
    public void addDashes_inputStringEmpty_throws() {
        Txref.impl.addGroupSeparators("", 0, 1);
    }

    @Test(expected = RuntimeException.class)
    public void addDashes_inputStringTooShort_throws() {
        Txref.impl.addGroupSeparators("0", 0, 1);
    }

    @Test
    public void addDashes_hrplenZero() {
        // hrplen is zero, then the "rest" of the input is of length two, so one hyphen should be inserted
        String result = Txref.impl.addGroupSeparators("00", 0, 1);
        assertEquals("0-0", result);
    }

    @Test
    public void addDashes_hrplenOne() {
        // hrplen is one, then the "rest" of the input is of length one, so zero hyphens should be inserted
        String result = Txref.impl.addGroupSeparators("00", 1, 1);
        assertEquals("00", result);
    }

    @Test
    public void addDashes_hrplenTwo() {
        // hrplen is two, then the "rest" of the input is of length zero, so zero hyphens should be inserted
        String result = Txref.impl.addGroupSeparators("00", 2, 1);
        assertEquals("00", result);
    }

    @Test(expected = RuntimeException.class)
    public void addDashes_hrplenThree_throws() {
        // hrplen is three, then the "rest" of the input is of length -1, so exception is thrown
        Txref.impl.addGroupSeparators("00", 3, 1);
    }

    @Test(expected = RuntimeException.class)
    public void addDashes_hrplenTooLong_throws() {
        Txref.impl.addGroupSeparators("00", Bech32.Limits.MAX_HRP_LENGTH + 1, 1);
    }

    @Test(expected = RuntimeException.class)
    public void addDashes_separatorOffsetZero_throws() {
        Txref.impl.addGroupSeparators("00", 1, 0);
    }

    @Test(expected = RuntimeException.class)
    public void addDashes_separatorOffsetNegative_throws() {
        Txref.impl.addGroupSeparators("00", 1, -1);
    }

    @Test
    public void addDashes_separatorOffsetTooLarge_returnsSameAsInput() {
        String result = Txref.impl.addGroupSeparators("00", 1, 10);
        assertEquals("00", result);
    }

    @Test
    public void addDashes_everyOtherCharacter() {
        String result = Txref.impl.addGroupSeparators("00", 0, 1);
        assertEquals("0-0", result);

        result = Txref.impl.addGroupSeparators("000", 0, 1);
        assertEquals("0-0-0", result);

        result = Txref.impl.addGroupSeparators("0000", 0, 1);
        assertEquals("0-0-0-0", result);

        result = Txref.impl.addGroupSeparators("00000", 0, 1);
        assertEquals("0-0-0-0-0", result);

        result = Txref.impl.addGroupSeparators("000000", 0, 1);
        assertEquals("0-0-0-0-0-0", result);
    }

    @Test
    public void addDashes_everyFewCharacters() {
        String result = Txref.impl.addGroupSeparators("0000000", 0, 1);
        assertEquals("0-0-0-0-0-0-0", result);

        result = Txref.impl.addGroupSeparators("0000000", 0, 2);
        assertEquals("00-00-00-0", result);

        result = Txref.impl.addGroupSeparators("0000000", 0, 3);
        assertEquals("000-000-0", result);

        result = Txref.impl.addGroupSeparators("0000000", 0, 4);
        assertEquals("0000-000", result);

    }

    @Test
    public void addDashes_everyFewCharacters_withHrps() {
        String result = Txref.impl.addGroupSeparators("A0000000", 1, 1);
        assertEquals("A0-0-0-0-0-0-0", result);

        result = Txref.impl.addGroupSeparators("AB0000000", 2, 2);
        assertEquals("AB00-00-00-0", result);

        result = Txref.impl.addGroupSeparators("ABCD0000000", 4, 4);
        assertEquals("ABCD0000-000", result);
    }

    @Test
    public void prettyPrint_mainnet() {
        String hrp = Txref.BECH32_HRP_MAIN;
        String plain = "tx1rqqqqqqqqmhuqhp";
        String pretty = Txref.impl.prettyPrint(plain, hrp.length());
        assertEquals("tx1:rqqq-qqqq-qmhu-qhp", pretty);
    }

    @Test
    public void prettyPrint_testnet() {
        String hrp = Txref.BECH32_HRP_TEST;
        String plain = "txtest1xjk0uqayzat0dz8";
        String pretty = Txref.impl.prettyPrint(plain, hrp.length());
        assertEquals("txtest1:xjk0-uqay-zat0-dz8", pretty);
    }

    @Test(expected = NullPointerException.class)
    public void extractMagicCode_withNullHrpAndDp_shouldThrow() {
        design.contract.bech32.DecodedResult decodedResult = null;
        Txref.impl.extractMagicCode(decodedResult.getDp());
    }

    @Test(expected = NullPointerException.class)
    public void extractMagicCode_withEmptyHrpAndDp_shouldThrow() {
        design.contract.bech32.DecodedResult decodedResult = new design.contract.bech32.DecodedResult();
        Txref.impl.extractMagicCode(decodedResult.getDp());
    }

    @Test
    public void extractMagicCode_mainnet() {
        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("tx1rqqqqqqqqwtvvjr");
        assertEquals(Txref.MAGIC_BTC_MAIN, Txref.impl.extractMagicCode(decodedResult.getDp()));
    }

    @Test
    public void extractMagicCode_testnet() {
        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("txtest1xjk0uqayzghlp89");
        assertEquals(Txref.MAGIC_BTC_TEST, Txref.impl.extractMagicCode(decodedResult.getDp()));
    }

    @Test(expected = NullPointerException.class)
    public void extractVersion_withNullHrpAndDp_shouldThrow() {
        design.contract.bech32.DecodedResult decodedResult = null;
        Txref.impl.extractVersion(decodedResult.getDp());
    }

    @Test(expected = NullPointerException.class)
    public void extractVersion_withEmptyHrpAndDp_shouldThrow() {
        design.contract.bech32.DecodedResult decodedResult = new design.contract.bech32.DecodedResult();
        Txref.impl.extractVersion(decodedResult.getDp());
    }

    @Test
    public void extractVersion_mainnet() {
        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("tx1rqqqqqqqqwtvvjr");
        assertEquals(0, Txref.impl.extractVersion(decodedResult.getDp()));
    }

    @Test
    public void extractVersion_testnet() {
        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("txtest1xjk0uqayzghlp89");
        assertEquals(0, Txref.impl.extractVersion(decodedResult.getDp()));
    }

    @Test(expected = NullPointerException.class)
    public void extractBlockHeight_withNullHrpAndDp_shouldThrow() {
        design.contract.bech32.DecodedResult decodedResult = null;
        Txref.impl.extractBlockHeight(decodedResult.getDp());
    }

    @Test(expected = NullPointerException.class)
    public void extractBlockHeight_withEmptyHrpAndDp_shouldThrow() {
        design.contract.bech32.DecodedResult decodedResult = new design.contract.bech32.DecodedResult();
        Txref.impl.extractBlockHeight(decodedResult.getDp());
    }

    @Test
    public void extractBlockHeight() {

        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("tx1rqqqqqqqqwtvvjr");
        int blockHeight = Txref.impl.extractBlockHeight(decodedResult.getDp());
        assertEquals(0, blockHeight);

        decodedResult = Bech32.decode("tx1rqqqqqlllj687n2");
        blockHeight = Txref.impl.extractBlockHeight(decodedResult.getDp());
        assertEquals(0, blockHeight);

        decodedResult = Bech32.decode("tx1r7llllqqqatsvx9");
        blockHeight = Txref.impl.extractBlockHeight(decodedResult.getDp());
        assertEquals(0xFFFFFF, blockHeight);

        decodedResult = Bech32.decode("tx1r7lllllllp6m78v");
        blockHeight = Txref.impl.extractBlockHeight(decodedResult.getDp());
        assertEquals(0xFFFFFF, blockHeight);

        decodedResult = Bech32.decode("tx1rjk0uqayz9l7m9m");
        blockHeight = Txref.impl.extractBlockHeight(decodedResult.getDp());
        assertEquals(466793, blockHeight);

        decodedResult = Bech32.decode("txtest1xjk0uqayzghlp89");
        blockHeight = Txref.impl.extractBlockHeight(decodedResult.getDp());
        assertEquals(466793, blockHeight);
    }

    @Test(expected = NullPointerException.class)
    public void extractTransactionPosition_withNullHrpAndDp_shouldThrow() {
        design.contract.bech32.DecodedResult decodedResult = null;
        Txref.impl.extractTransactionPosition(decodedResult.getDp());
    }

    @Test(expected = NullPointerException.class)
    public void extractTransactionPosition_withEmptyHrpAndDp_shouldThrow() {
        design.contract.bech32.DecodedResult decodedResult = new design.contract.bech32.DecodedResult();
        Txref.impl.extractTransactionPosition(decodedResult.getDp());
    }

    @Test
    public void extractTransactionPosition() {

        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("tx1rqqqqqqqqwtvvjr");
        int transactionPosition = Txref.impl.extractTransactionPosition(decodedResult.getDp());
        assertEquals(0, transactionPosition);

        decodedResult = Bech32.decode("tx1rqqqqqlllj687n2");
        transactionPosition = Txref.impl.extractTransactionPosition(decodedResult.getDp());
        assertEquals(0x7FFF, transactionPosition);

        decodedResult = Bech32.decode("tx1r7llllqqqatsvx9");
        transactionPosition = Txref.impl.extractTransactionPosition(decodedResult.getDp());
        assertEquals(0, transactionPosition);

        decodedResult = Bech32.decode("tx1r7lllllllp6m78v");
        transactionPosition = Txref.impl.extractTransactionPosition(decodedResult.getDp());
        assertEquals(0x7FFF, transactionPosition);

        decodedResult = Bech32.decode("tx1rjk0uqayz9l7m9m");
        transactionPosition = Txref.impl.extractTransactionPosition(decodedResult.getDp());
        assertEquals(2205, transactionPosition);

        decodedResult = Bech32.decode("txtest1xjk0uqayzghlp89");
        transactionPosition = Txref.impl.extractTransactionPosition(decodedResult.getDp());
        assertEquals(2205, transactionPosition);
    }

    @Test
    public void extractTxoIndex() {
        // these will all return 0 as none are extended txrefs

        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("tx1rqqqqqqqqwtvvjr");
        int txoIndex = Txref.impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(0, txoIndex);

        decodedResult = Bech32.decode("tx1rqqqqqlllj687n2");
        txoIndex = Txref.impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(0, txoIndex);

        decodedResult = Bech32.decode("tx1r7llllqqqatsvx9");
        txoIndex = Txref.impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(0, txoIndex);

        decodedResult = Bech32.decode("tx1r7lllllllp6m78v");
        txoIndex = Txref.impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(0, txoIndex);

        decodedResult = Bech32.decode("tx1rjk0uqayz9l7m9m");
        txoIndex = Txref.impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(0, txoIndex);

        decodedResult = Bech32.decode("txtest1xjk0uqayzghlp89");
        txoIndex = Txref.impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(0, txoIndex);
    }

    @Test
    public void addHrpIfNeeded_isNeeded() {
        assertEquals("tx1rqqqqqqqqwtvvjr", Txref.impl.addHrpIfNeeded("rqqqqqqqqwtvvjr"));
        assertEquals("txtest1xjk0uqayzghlp89", Txref.impl.addHrpIfNeeded("xjk0uqayzghlp89"));
    }

    @Test
    public void addHrpIfNeeded_isNotNeeded() {
        assertEquals("tx1rqqqqqqqqwtvvjr", Txref.impl.addHrpIfNeeded("tx1rqqqqqqqqwtvvjr"));
        assertEquals("txtest1xjk0uqayzghlp89", Txref.impl.addHrpIfNeeded("txtest1xjk0uqayzghlp89"));
    }

    @Test
    public void txrefEncode_mainnet() {
        assertEquals("tx1:rqqq-qqqq-qwtv-vjr",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0, 0));
        assertEquals("tx1:rqqq-qqll-lj68-7n2",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0, 0x7FFF));
        assertEquals("tx1:r7ll-llqq-qats-vx9",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0xFFFFFF, 0));
        assertEquals("tx1:r7ll-llll-lp6m-78v",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0xFFFFFF, 0x7FFF));
        assertEquals("tx1:rjk0-uqay-z9l7-m9m",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 466793, 2205));
    }

    @Test
    public void txrefEncode_testnet() {
        assertEquals("txtest1:xqqq-qqqq-qrrd-ksa",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0, 0));
        assertEquals("txtest1:xqqq-qqll-lljx-y35",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0, 0x7FFF));
        assertEquals("txtest1:x7ll-llqq-qsr3-kym",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0xFFFFFF, 0));
        assertEquals("txtest1:x7ll-llll-lvj6-y9j",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0xFFFFFF, 0x7FFF));
        assertEquals("txtest1:xjk0-uqay-zghl-p89",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 466793, 2205));
    }

    @Test
    public void txrefDecode_mainnet() {
        LocationData ld;

        ld = Txref.impl.txrefDecode("tx1:rqqq-qqqq-qwtv-vjr");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:rqqq-qqll-lj68-7n2");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:r7ll-llqq-qats-vx9");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:r7ll-llll-lp6m-78v");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:rjk0-uqay-z9l7-m9m");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(466793, ld.getBlockHeight());
        assertEquals(2205, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());
    }

    @Test
    public void txrefDecode_testnet() {
        LocationData ld;

        ld = Txref.impl.txrefDecode("txtest1:xqqq-qqqq-qrrd-ksa");
        assertEquals(Txref.MAGIC_BTC_TEST, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:xqqq-qqll-lljx-y35");
        assertEquals(Txref.MAGIC_BTC_TEST, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:x7ll-llqq-qsr3-kym");
        assertEquals(Txref.MAGIC_BTC_TEST, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:x7ll-llll-lvj6-y9j");
        assertEquals(Txref.MAGIC_BTC_TEST, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:xjk0-uqay-zghl-p89");
        assertEquals(Txref.MAGIC_BTC_TEST, ld.getMagicCode());
        assertEquals(466793, ld.getBlockHeight());
        assertEquals(2205, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());
    }


    // Extended Txrefs...

    @Test
    public void extractExtendedMagicCode_mainnet() {
        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("tx1yjk0uqayzu4xx22sy6");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, Txref.impl.extractMagicCode(decodedResult.getDp()));
    }

    @Test
    public void extractExtendedMagicCode_testnet() {
        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("txtest18jk0uqayzu4xgj9m8a");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, Txref.impl.extractMagicCode(decodedResult.getDp()));
    }

    @Test
    public void extractExtendedBlockHeight() {

        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("tx1yqqqqqqqqqqqrvum0c");
        int blockHeight = Txref.impl.extractBlockHeight(decodedResult.getDp());
        assertEquals(0, blockHeight);

        decodedResult = Bech32.decode("tx1y7llllqqqqqqggjgw6");
        blockHeight = Txref.impl.extractBlockHeight(decodedResult.getDp());
        assertEquals(0xFFFFFF, blockHeight);

        decodedResult = Bech32.decode("tx1yjk0uqayzu4xx22sy6");
        blockHeight = Txref.impl.extractBlockHeight(decodedResult.getDp());
        assertEquals(466793, blockHeight);

        decodedResult = Bech32.decode("txtest18jk0uqayzu4xgj9m8a");
        blockHeight = Txref.impl.extractBlockHeight(decodedResult.getDp());
        assertEquals(466793, blockHeight);
    }

    @Test
    public void extractExtendedTransactionPosition() {

        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("tx1yqqqqqqqqqqqrvum0c");
        int transactionPosition = Txref.impl.extractTransactionPosition(decodedResult.getDp());
        assertEquals(0, transactionPosition);

        decodedResult = Bech32.decode("tx1yqqqqqlllqqqen8x05");
        transactionPosition = Txref.impl.extractTransactionPosition(decodedResult.getDp());
        assertEquals(0x7FFF, transactionPosition);

        decodedResult = Bech32.decode("tx1yjk0uqayzu4xx22sy6");
        transactionPosition = Txref.impl.extractTransactionPosition(decodedResult.getDp());
        assertEquals(2205, transactionPosition);

        decodedResult = Bech32.decode("txtest18jk0uqayzu4xgj9m8a");
        transactionPosition = Txref.impl.extractTransactionPosition(decodedResult.getDp());
        assertEquals(2205, transactionPosition);
    }

    @Test
    public void extractExtendedTxoIndex() {
        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("tx1yqqqqqqqqqqqrvum0c");
        int txoIndex = Txref.impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(0, txoIndex);

        decodedResult = Bech32.decode("tx1yqqqqqqqqpqqpw4vkq");
        txoIndex = Txref.impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(1, txoIndex);

        decodedResult = Bech32.decode("tx1yqqqqqqqqu4xj4f2xe");
        txoIndex = Txref.impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(0x1ABC, txoIndex);

        decodedResult = Bech32.decode("tx1yjk0uqayzu4xx22sy6");
        txoIndex = Txref.impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(0x1ABC, txoIndex);

        decodedResult = Bech32.decode("txtest18jk0uqayzu4xgj9m8a");
        txoIndex = Txref.impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(0x1ABC, txoIndex);
    }

    @Test
    public void addHrpIfNeeded_isNeeded_extended() {
        assertEquals("tx1yjk0uqayzu4xnk6upc", Txref.impl.addHrpIfNeeded("yjk0uqayzu4xnk6upc"));
        assertEquals("txtest18jk0uqayzu4xaw4hzl", Txref.impl.addHrpIfNeeded("8jk0uqayzu4xaw4hzl"));
    }

    @Test
    public void addHrpIfNeeded_isNotNeeded_extended() {
        assertEquals("tx1yjk0uqayzu4xnk6upc", Txref.impl.addHrpIfNeeded("tx1yjk0uqayzu4xnk6upc"));
        assertEquals("txtest18jk0uqayzu4xaw4hzl", Txref.impl.addHrpIfNeeded("txtest18jk0uqayzu4xaw4hzl"));
    }

    @Test
    public void txrefEncode_extended_mainnet() {
        assertEquals("tx1:yqqq-qqqq-qqqq-rvum-0c",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0, 0));
        assertEquals("tx1:yqqq-qqll-lqqq-en8x-05",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0x7FFF, 0));
        assertEquals("tx1:y7ll-llqq-qqqq-ggjg-w6",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0, 0));
        assertEquals("tx1:y7ll-llll-lqqq-jhf4-wk",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x7FFF, 0));

        assertEquals("tx1:yqqq-qqqq-qpqq-pw4v-kq",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0, 1));
        assertEquals("tx1:yqqq-qqll-lpqq-m3w3-kv",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0x7FFF, 1));
        assertEquals("tx1:y7ll-llqq-qpqq-22ml-hz",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0, 1));
        assertEquals("tx1:y7ll-llll-lpqq-s4qz-hw",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x7FFF, 1));

        assertEquals("tx1:yqqq-qqqq-qu4x-j4f2-xe",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0, 0x1ABC));
        assertEquals("tx1:yqqq-qqll-lu4x-g2jh-x4",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0x7FFF, 0x1ABC));
        assertEquals("tx1:y7ll-llqq-qu4x-e38e-8m",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0, 0x1ABC));
        assertEquals("tx1:y7ll-llll-lu4x-rwuy-8h",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x7FFF, 0x1ABC));

        assertEquals("tx1:yjk0-uqay-zu4x-x22s-y6",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 466793, 2205, 0x1ABC));
    }

    @Test
    public void txrefEncode_extended_testnet() {
        assertEquals("txtest1:8qqq-qqqq-qqqq-d5ns-vl",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0, 0));
        assertEquals("txtest1:8qqq-qqll-lqqq-htgd-vn",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0x7FFF, 0));
        assertEquals("txtest1:87ll-llqq-qqqq-xsar-da",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0, 0));
        assertEquals("txtest1:87ll-llll-lqqq-u0x7-d3",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x7FFF, 0));

        assertEquals("txtest1:8qqq-qqqq-qpqq-0k68-48",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0, 1));
        assertEquals("txtest1:8qqq-qqll-lpqq-4fp6-4t",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0x7FFF, 1));
        assertEquals("txtest1:87ll-llqq-qpqq-yj55-59",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0, 1));
        assertEquals("txtest1:87ll-llll-lpqq-7d0f-5f",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x7FFF, 1));

        assertEquals("txtest1:8qqq-qqqq-qu4x-udxp-97",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0, 0x1ABC));
        assertEquals("txtest1:8qqq-qqll-lu4x-xjau-9j",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0x7FFF, 0x1ABC));
        assertEquals("txtest1:87ll-llqq-qu4x-hfgj-yu",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0, 0x1ABC));
        assertEquals("txtest1:87ll-llll-lu4x-dkn0-ys",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x7FFF, 0x1ABC));

        assertEquals("txtest1:8jk0-uqay-zu4x-gj9m-8a",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 466793, 2205, 0x1ABC));
    }

    @Test
    public void txrefDecode_extended_mainnet() {
        LocationData ld;

        ld = Txref.impl.txrefDecode("tx1:yqqq-qqqq-qqqq-rvum-0c");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("tx1:yqqq-qqll-lqqq-en8x-05");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("tx1:y7ll-llqq-qqqq-ggjg-w6");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("tx1:y7ll-llll-lqqq-jhf4-wk");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:yqqq-qqqq-qpqq-pw4v-kq");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("tx1:yqqq-qqll-lpqq-m3w3-kv");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("tx1:y7ll-llqq-qpqq-22ml-hz");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("tx1:y7ll-llll-lpqq-s4qz-hw");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:yqqq-qqqq-qu4x-j4f2-xe");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("tx1:yqqq-qqll-lu4x-g2jh-x4");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("tx1:y7ll-llqq-qu4x-e38e-8m");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("tx1:y7ll-llll-lu4x-rwuy-8h");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:yjk0-uqay-zu4x-x22s-y6");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(466793, ld.getBlockHeight());
        assertEquals(2205, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());
    }

    @Test
    public void txrefDecode_extended_testnet() {
        LocationData ld;

        ld = Txref.impl.txrefDecode("txtest1:8qqq-qqqq-qqqq-d5ns-vl");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("txtest1:8qqq-qqll-lqqq-htgd-vn");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("txtest1:87ll-llqq-qqqq-xsar-da");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("txtest1:87ll-llll-lqqq-u0x7-d3");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:8qqq-qqqq-qpqq-0k68-48");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("txtest1:8qqq-qqll-lpqq-4fp6-4t");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("txtest1:87ll-llqq-qpqq-yj55-59");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("txtest1:87ll-llll-lpqq-7d0f-5f");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:8qqq-qqqq-qu4x-udxp-97");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("txtest1:8qqq-qqll-lu4x-xjau-9j");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("txtest1:87ll-llqq-qu4x-hfgj-yu");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("txtest1:87ll-llll-lu4x-dkn0-ys");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:8jk0-uqay-zu4x-gj9m-8a");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(466793, ld.getBlockHeight());
        assertEquals(2205, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());

    }

    // //////////////// Examples from BIP-0136 /////////////////////

    // check that we correctly encode some sample txrefs from BIP-0136. These may duplicate
    // some tests above, but many of the examples in the BIP are present here for reference.

    @Test
    public void txrefEncode_bip_examples() {
        // Genesis Coinbase Transaction (Transaction #0 of Block #0):
        assertEquals("tx1:rqqq-qqqq-qwtv-vjr",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0, 0));

        // Transaction #2205 of Block #466793:
        assertEquals("tx1:rjk0-uqay-z9l7-m9m",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 466793, 2205));

        // The following list gives properly encoded Bitcoin mainnet TxRef's
        assertEquals("tx1:rqqq-qqqq-qwtv-vjr",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0, 0));
        assertEquals("tx1:rqqq-qqll-lj68-7n2",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0, 0x7FFF));
        assertEquals("tx1:r7ll-llqq-qats-vx9",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0xFFFFFF, 0x0));
        assertEquals("tx1:r7ll-llll-lp6m-78v",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0xFFFFFF, 0x7FFF));

        // The following list gives properly encoded Bitcoin testnet TxRef's
        assertEquals("txtest1:xqqq-qqqq-qrrd-ksa",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0, 0));
        assertEquals("txtest1:xqqq-qqll-lljx-y35",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0, 0x7FFF));
        assertEquals("txtest1:x7ll-llqq-qsr3-kym",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0xFFFFFF, 0x0));
        assertEquals("txtest1:x7ll-llll-lvj6-y9j",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0xFFFFFF, 0x7FFF));

        // The following list gives valid (though strangely formatted) Bitcoin TxRef's
        assertEquals("tx1:rjk0-uqay-z9l7-m9m",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0x71F69, 0x89D));

        // The following list gives properly encoded Bitcoin mainnet TxRef's with Outpoints
        assertEquals("tx1:yqqq-qqqq-qqqq-rvum-0c",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0, 0));
        assertEquals("tx1:yqqq-qqll-lqqq-en8x-05",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0x7FFF, 0));
        assertEquals("tx1:y7ll-llqq-qqqq-ggjg-w6",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x0, 0));
        assertEquals("tx1:y7ll-llll-lqqq-jhf4-wk",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x7FFF, 0));

        assertEquals("tx1:yqqq-qqqq-qpqq-pw4v-kq",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0, 1));
        assertEquals("tx1:yqqq-qqll-lpqq-m3w3-kv",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0x7FFF, 1));
        assertEquals("tx1:y7ll-llqq-qpqq-22ml-hz",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x0, 1));
        assertEquals("tx1:y7ll-llll-lpqq-s4qz-hw",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x7FFF, 1));

        assertEquals("tx1:yjk0-uqay-zrfq-akgy-w9",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0x71F69, 0x89D, 0x123));
        assertEquals("tx1:yjk0-uqay-zu4x-x22s-y6",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0x71F69, 0x89D, 0x1ABC));

        // The following list gives properly encoded Bitcoin testnet TxRef's with Outpoints
        assertEquals("txtest1:8qqq-qqqq-qqqq-d5ns-vl",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0, 0));
        assertEquals("txtest1:8qqq-qqll-lqqq-htgd-vn",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0x7FFF, 0));
        assertEquals("txtest1:87ll-llqq-qqqq-xsar-da",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x0, 0));
        assertEquals("txtest1:87ll-llll-lqqq-u0x7-d3",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x7FFF, 0));

        assertEquals("txtest1:8qqq-qqqq-qpqq-0k68-48",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0, 1));
        assertEquals("txtest1:8qqq-qqll-lpqq-4fp6-4t",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0x7FFF, 1));
        assertEquals("txtest1:87ll-llqq-qpqq-yj55-59",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x0, 1));
        assertEquals("txtest1:87ll-llll-lpqq-7d0f-5f",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x7FFF, 1));

        assertEquals("txtest1:8jk0-uqay-zrfq-nw80-dz",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0x71F69, 0x89D, 0x123));
        assertEquals("txtest1:8jk0-uqay-zu4x-gj9m-8a",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0x71F69, 0x89D, 0x1ABC));
    }

    @Test
    public void txrefDecode_bip_examples() {

        LocationData ld;

        // Genesis Coinbase Transaction (Transaction #0 of Block #0):
        ld = Txref.impl.txrefDecode("tx1:rqqq-qqqq-qwtv-vjr");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());


        // Transaction #2205 of Block #466793:
        ld = Txref.impl.txrefDecode("tx1:rjk0-uqay-z9l7-m9m");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(466793, ld.getBlockHeight());
        assertEquals(2205, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());


        // The following list gives properly encoded Bitcoin mainnet TxRef's
        ld = Txref.impl.txrefDecode("tx1:rqqq-qqqq-qwtv-vjr");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:rqqq-qqll-lj68-7n2");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:r7ll-llqq-qats-vx9");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:r7ll-llll-lp6m-78v");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());


        // The following list gives properly encoded Bitcoin testnet TxRef's
        ld = Txref.impl.txrefDecode("txtest1:xqqq-qqqq-qrrd-ksa");
        assertEquals(Txref.MAGIC_BTC_TEST, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:xqqq-qqll-lljx-y35");
        assertEquals(Txref.MAGIC_BTC_TEST, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:x7ll-llqq-qsr3-kym");
        assertEquals(Txref.MAGIC_BTC_TEST, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:x7ll-llll-lvj6-y9j");
        assertEquals(Txref.MAGIC_BTC_TEST, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());


        // The following list gives valid (though strangely formatted) Bitcoin TxRef's
        ld = Txref.impl.txrefDecode("tx1:rjk0-uqay-z9l7-m9m");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(0x71F69, ld.getBlockHeight());
        assertEquals(0x89D, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());


        // The following list gives properly encoded Bitcoin mainnet TxRef's with Outpoints
        ld = Txref.impl.txrefDecode("tx1:yqqq-qqqq-qqqq-rvum-0c");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:yqqq-qqll-lqqq-en8x-05");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:y7ll-llqq-qqqq-ggjg-w6");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:y7ll-llll-lqqq-jhf4-wk");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());


        ld = Txref.impl.txrefDecode("tx1:yqqq-qqqq-qpqq-pw4v-kq");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:yqqq-qqll-lpqq-m3w3-kv");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:y7ll-llqq-qpqq-22ml-hz");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:y7ll-llll-lpqq-s4qz-hw");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());


        ld = Txref.impl.txrefDecode("tx1:yjk0-uqay-zrfq-akgy-w9");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0x71F69, ld.getBlockHeight());
        assertEquals(0x89D, ld.getTransactionPosition());
        assertEquals(0x123, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:yjk0-uqay-zu4x-x22s-y6");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0x71F69, ld.getBlockHeight());
        assertEquals(0x89D, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());


        // The following list gives properly encoded Bitcoin testnet TxRef's with Outpoints
        ld = Txref.impl.txrefDecode("txtest1:8qqq-qqqq-qqqq-d5ns-vl");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:8qqq-qqll-lqqq-htgd-vn");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:87ll-llqq-qqqq-xsar-da");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:87ll-llll-lqqq-u0x7-d3");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());


        ld = Txref.impl.txrefDecode("txtest1:8qqq-qqqq-qpqq-0k68-48");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:8qqq-qqll-lpqq-4fp6-4t");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:87ll-llqq-qpqq-yj55-59");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:87ll-llll-lpqq-7d0f-5f");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());


        ld = Txref.impl.txrefDecode("txtest1:8jk0-uqay-zrfq-nw80-dz");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0x71F69, ld.getBlockHeight());
        assertEquals(0x89D, ld.getTransactionPosition());
        assertEquals(0x123, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:8jk0-uqay-zu4x-gj9m-8a");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0x71F69, ld.getBlockHeight());
        assertEquals(0x89D, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());
    }

    @Test
    public void txrefDecode_checkEncoding() {

        LocationData ld;

        ld = Txref.impl.txrefDecode("txtest1:8jk0-uqay-zu4x-gj9m-8a");
        assertEquals(LocationData.Encoding.BECH32M, ld.getEncoding());

        ld = Txref.impl.txrefDecode("txtest1:8jk0-uqay-zu4x-aw4h-zl");
        assertEquals(LocationData.Encoding.BECH32, ld.getEncoding());

    }

}
