package design.contract.txref;

import design.contract.bech32.Bech32;
import org.junit.Test;

import static org.junit.Assert.*;

public class TxrefTest {

    @Test
    public void checkBlockHeightRange_forValuesInRange_wontThrow() {
        Txref.Impl.checkBlockHeightRange(0);
        Txref.Impl.checkBlockHeightRange(1);
        Txref.Impl.checkBlockHeightRange(Txref.Limits.MAX_BLOCK_HEIGHT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkBlockHeightRange_forValueBeforeRange_throws() {
        Txref.Impl.checkBlockHeightRange(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkBlockHeightRange_forValueAfterRange_throws() {
        Txref.Impl.checkBlockHeightRange(Txref.Limits.MAX_BLOCK_HEIGHT + 1);
    }

    @Test
    public void checkTransactionPositionRange_forValuesInRange_wontThrow() {
        Txref.Impl.checkTransactionPositionRange(0);
        Txref.Impl.checkTransactionPositionRange(1);
        Txref.Impl.checkTransactionPositionRange(Txref.Limits.MAX_TRANSACTION_POSITION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkTransactionPositionRange_forValueBeforeRange_throws() {
        Txref.Impl.checkTransactionPositionRange(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkTransactionPositionRange_forValueAfterRange_throws() {
        Txref.Impl.checkTransactionPositionRange(Txref.Limits.MAX_TRANSACTION_POSITION + 1);
    }

    @Test
    public void checkTxoIndexRange_forValuesInRange_wontThrow() {
        Txref.Impl.checkTxoIndexRange(0);
        Txref.Impl.checkTxoIndexRange(1);
        Txref.Impl.checkTxoIndexRange(Txref.Limits.MAX_TXO_INDEX);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkTxoIndexRange_forValueBeforeRange_throws() {
        Txref.Impl.checkTxoIndexRange(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkTxoIndexRange_forValueAfterRange_throws() {
        Txref.Impl.checkTxoIndexRange(Txref.Limits.MAX_TXO_INDEX + 1);
    }

    @Test
    public void checkMagicCodeRange_forValuesInRange_wontThrow() {
        Txref.Impl.checkMagicCodeRange(0);
        Txref.Impl.checkMagicCodeRange(1);
        Txref.Impl.checkMagicCodeRange(Txref.Limits.MAX_MAGIC_CODE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkMagicCodeRange_forValueBeforeRange_throws() {
        Txref.Impl.checkMagicCodeRange(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkMagicCodeRange_forValueAfterRange_throws() {
        Txref.Impl.checkMagicCodeRange(Txref.Limits.MAX_MAGIC_CODE + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDashes_inputStringEmpty_throws() {
        Txref.Impl.addGroupSeparators("", 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDashes_inputStringTooShort_throws() {
        Txref.Impl.addGroupSeparators("0", 0, 1);
    }

    @Test
    public void addDashes_hrplenZero() {
        // hrplen is zero, then the "rest" of the input is of length two, so one hyphen should be inserted
        String result = Txref.Impl.addGroupSeparators("00", 0, 1);
        assertEquals("0-0", result);
    }

    @Test
    public void addDashes_hrplenOne() {
        // hrplen is one, then the "rest" of the input is of length one, so zero hyphens should be inserted
        String result = Txref.Impl.addGroupSeparators("00", 1, 1);
        assertEquals("00", result);
    }

    @Test
    public void addDashes_hrplenTwo() {
        // hrplen is two, then the "rest" of the input is of length zero, so zero hyphens should be inserted
        String result = Txref.Impl.addGroupSeparators("00", 2, 1);
        assertEquals("00", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDashes_hrplenThree_throws() {
        // hrplen is three, then the "rest" of the input is of length -1, so exception is thrown
        Txref.Impl.addGroupSeparators("00", 3, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDashes_hrplenTooLong_throws() {
        Txref.Impl.addGroupSeparators("00", Bech32.Limits.MAX_HRP_LENGTH + 1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDashes_separatorOffsetZero_throws() {
        Txref.Impl.addGroupSeparators("00", 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDashes_separatorOffsetNegative_throws() {
        Txref.Impl.addGroupSeparators("00", 1, -1);
    }

    @Test
    public void addDashes_separatorOffsetTooLarge_returnsSameAsInput() {
        String result = Txref.Impl.addGroupSeparators("00", 1, 10);
        assertEquals("00", result);
    }

    @Test
    public void addDashes_everyOtherCharacter() {
        String result = Txref.Impl.addGroupSeparators("00", 0, 1);
        assertEquals("0-0", result);

        result = Txref.Impl.addGroupSeparators("000", 0, 1);
        assertEquals("0-0-0", result);

        result = Txref.Impl.addGroupSeparators("0000", 0, 1);
        assertEquals("0-0-0-0", result);

        result = Txref.Impl.addGroupSeparators("00000", 0, 1);
        assertEquals("0-0-0-0-0", result);

        result = Txref.Impl.addGroupSeparators("000000", 0, 1);
        assertEquals("0-0-0-0-0-0", result);
    }

    @Test
    public void addDashes_everyFewCharacters() {
        String result = Txref.Impl.addGroupSeparators("0000000", 0, 1);
        assertEquals("0-0-0-0-0-0-0", result);

        result = Txref.Impl.addGroupSeparators("0000000", 0, 2);
        assertEquals("00-00-00-0", result);

        result = Txref.Impl.addGroupSeparators("0000000", 0, 3);
        assertEquals("000-000-0", result);

        result = Txref.Impl.addGroupSeparators("0000000", 0, 4);
        assertEquals("0000-000", result);

    }

    @Test
    public void addDashes_everyFewCharacters_withHrps() {
        String result = Txref.Impl.addGroupSeparators("A0000000", 1, 1);
        assertEquals("A0-0-0-0-0-0-0", result);

        result = Txref.Impl.addGroupSeparators("AB0000000", 2, 2);
        assertEquals("AB00-00-00-0", result);

        result = Txref.Impl.addGroupSeparators("ABCD0000000", 4, 4);
        assertEquals("ABCD0000-000", result);
    }

    @Test
    public void prettyPrint_mainnet() {
        String hrp = Txref.BECH32_HRP_MAIN;
        String plain = "tx1rqqqqqqqqwtvvjr";
        String pretty = Txref.Impl.prettyPrint(plain, hrp.length());
        assertEquals("tx1:rqqq-qqqq-qwtv-vjr", pretty);
    }

    @Test
    public void prettyPrint_testnet() {
        String hrp = Txref.BECH32_HRP_TEST;
        String plain = "txtest1xjk0uqayzghlp89";
        String pretty = Txref.Impl.prettyPrint(plain, hrp.length());
        assertEquals("txtest1:xjk0-uqay-zghl-p89", pretty);
    }

    @Test(expected = NullPointerException.class)
    public void extractMagicCode_withNullData_shouldThrow() {
        Txref.Impl.extractMagicCode(null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void extractMagicCode_withEmptyData_shouldThrow() {
        char[] dp = new char[0];
        Txref.Impl.extractMagicCode(dp);
    }

    @Test
    public void extractMagicCode_mainnet() {
        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("tx1rqqqqqqqqwtvvjr");
        assertEquals(Txref.MAGIC_BTC_MAIN, Txref.Impl.extractMagicCode(decodedResult.getDp()));
    }

    @Test
    public void extractMagicCode_testnet() {
        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("txtest1xjk0uqayzghlp89");
        assertEquals(Txref.MAGIC_BTC_TEST, Txref.Impl.extractMagicCode(decodedResult.getDp()));
    }

    @Test(expected = NullPointerException.class)
    public void extractVersion_withNullDp_shouldThrow() {
        Txref.Impl.extractVersion(null);
    }

    @Test(expected = NullPointerException.class)
    public void extractVersion_withEmptyDp_shouldThrow() {
        design.contract.bech32.DecodedResult decodedResult = new design.contract.bech32.DecodedResult();
        Txref.Impl.extractVersion(decodedResult.getDp());
    }

    @Test
    public void extractVersion_mainnet() {
        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("tx1rqqqqqqqqwtvvjr");
        assertEquals(0, Txref.Impl.extractVersion(decodedResult.getDp()));
    }

    @Test
    public void extractVersion_testnet() {
        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("txtest1xjk0uqayzghlp89");
        assertEquals(0, Txref.Impl.extractVersion(decodedResult.getDp()));
    }

    @Test(expected = NullPointerException.class)
    public void extractBlockHeight_withNullData_shouldThrow() {
        Txref.Impl.extractBlockHeight(null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void extractBlockHeight_withEmptyData_shouldThrow() {
        char[] dp = new char[0];
        Txref.Impl.extractBlockHeight(dp);
    }

    @Test
    public void extractBlockHeight() {

        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("tx1rqqqqqqqqwtvvjr");
        int blockHeight = Txref.Impl.extractBlockHeight(decodedResult.getDp());
        assertEquals(0, blockHeight);

        decodedResult = Bech32.decode("tx1rqqqqqlllj687n2");
        blockHeight = Txref.Impl.extractBlockHeight(decodedResult.getDp());
        assertEquals(0, blockHeight);

        decodedResult = Bech32.decode("tx1r7llllqqqatsvx9");
        blockHeight = Txref.Impl.extractBlockHeight(decodedResult.getDp());
        assertEquals(0xFFFFFF, blockHeight);

        decodedResult = Bech32.decode("tx1r7lllllllp6m78v");
        blockHeight = Txref.Impl.extractBlockHeight(decodedResult.getDp());
        assertEquals(0xFFFFFF, blockHeight);

        decodedResult = Bech32.decode("tx1rjk0uqayz9l7m9m");
        blockHeight = Txref.Impl.extractBlockHeight(decodedResult.getDp());
        assertEquals(466793, blockHeight);

        decodedResult = Bech32.decode("txtest1xjk0uqayzghlp89");
        blockHeight = Txref.Impl.extractBlockHeight(decodedResult.getDp());
        assertEquals(466793, blockHeight);
    }

    @Test(expected = NullPointerException.class)
    public void extractTransactionPosition_withNullData_shouldThrow() {
        Txref.Impl.extractTransactionPosition(null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void extractTransactionPosition_withEmptyData_shouldThrow() {
        char[] dp = new char[0];
        Txref.Impl.extractTransactionPosition(dp);
    }

    @Test
    public void extractTransactionPosition() {

        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("tx1rqqqqqqqqwtvvjr");
        int transactionPosition = Txref.Impl.extractTransactionPosition(decodedResult.getDp());
        assertEquals(0, transactionPosition);

        decodedResult = Bech32.decode("tx1rqqqqqlllj687n2");
        transactionPosition = Txref.Impl.extractTransactionPosition(decodedResult.getDp());
        assertEquals(0x7FFF, transactionPosition);

        decodedResult = Bech32.decode("tx1r7llllqqqatsvx9");
        transactionPosition = Txref.Impl.extractTransactionPosition(decodedResult.getDp());
        assertEquals(0, transactionPosition);

        decodedResult = Bech32.decode("tx1r7lllllllp6m78v");
        transactionPosition = Txref.Impl.extractTransactionPosition(decodedResult.getDp());
        assertEquals(0x7FFF, transactionPosition);

        decodedResult = Bech32.decode("tx1rjk0uqayz9l7m9m");
        transactionPosition = Txref.Impl.extractTransactionPosition(decodedResult.getDp());
        assertEquals(2205, transactionPosition);

        decodedResult = Bech32.decode("txtest1xjk0uqayzghlp89");
        transactionPosition = Txref.Impl.extractTransactionPosition(decodedResult.getDp());
        assertEquals(2205, transactionPosition);
    }

    @Test(expected = NullPointerException.class)
    public void extractTxoIndex_withNullData_shouldThrow() {
        Txref.Impl.extractTxoIndex(null);
    }

    @Test
    public void extractTxoIndex_withEmptyData_shouldReturnZero() {
        char[] dp = new char[0];
        assertEquals(0, Txref.Impl.extractTxoIndex(dp));
    }

    @Test
    public void extractTxoIndex() {
        // these will all return 0 as none are extended txrefs

        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("tx1rqqqqqqqqwtvvjr");
        int txoIndex = Txref.Impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(0, txoIndex);

        decodedResult = Bech32.decode("tx1rqqqqqlllj687n2");
        txoIndex = Txref.Impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(0, txoIndex);

        decodedResult = Bech32.decode("tx1r7llllqqqatsvx9");
        txoIndex = Txref.Impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(0, txoIndex);

        decodedResult = Bech32.decode("tx1r7lllllllp6m78v");
        txoIndex = Txref.Impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(0, txoIndex);

        decodedResult = Bech32.decode("tx1rjk0uqayz9l7m9m");
        txoIndex = Txref.Impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(0, txoIndex);

        decodedResult = Bech32.decode("txtest1xjk0uqayzghlp89");
        txoIndex = Txref.Impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(0, txoIndex);
    }

    @Test
    public void addHrpIfNeeded_isNeeded() {
        assertEquals("tx1rqqqqqqqqwtvvjr", Txref.Impl.addHrpIfNeeded("rqqqqqqqqwtvvjr"));
        assertEquals("txtest1xjk0uqayzghlp89", Txref.Impl.addHrpIfNeeded("xjk0uqayzghlp89"));
        assertEquals("txrt1q7lllllllps4p3p", Txref.Impl.addHrpIfNeeded("q7lllllllps4p3p"));
    }

    @Test
    public void addHrpIfNeeded_isNotNeeded() {
        assertEquals("tx1rqqqqqqqqwtvvjr", Txref.Impl.addHrpIfNeeded("tx1rqqqqqqqqwtvvjr"));
        assertEquals("txtest1xjk0uqayzghlp89", Txref.Impl.addHrpIfNeeded("txtest1xjk0uqayzghlp89"));
        assertEquals("txrt1q7lllllllps4p3p", Txref.Impl.addHrpIfNeeded("txrt1q7lllllllps4p3p"));
    }

    @Test
    public void txrefEncode_mainnet() {
        assertEquals("tx1:rqqq-qqqq-qwtv-vjr",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0, 0));
        assertEquals("tx1:rqqq-qqll-lj68-7n2",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0, 0x7FFF));
        assertEquals("tx1:r7ll-llqq-qats-vx9",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0xFFFFFF, 0));
        assertEquals("tx1:r7ll-llll-lp6m-78v",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0xFFFFFF, 0x7FFF));
        assertEquals("tx1:rjk0-uqay-z9l7-m9m",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 466793, 2205));
    }

    @Test
    public void txrefEncode_testnet() {
        assertEquals("txtest1:xqqq-qqqq-qrrd-ksa",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0, 0));
        assertEquals("txtest1:xqqq-qqll-lljx-y35",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0, 0x7FFF));
        assertEquals("txtest1:x7ll-llqq-qsr3-kym",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0xFFFFFF, 0));
        assertEquals("txtest1:x7ll-llll-lvj6-y9j",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0xFFFFFF, 0x7FFF));
        assertEquals("txtest1:xjk0-uqay-zghl-p89",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 466793, 2205));
    }

    @Test
    public void txrefEncode_regtest() {
        assertEquals("txrt1:qqqq-qqqq-qwpz-nyw",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_REGTEST, Txref.MAGIC_BTC_REGTEST, 0, 0));
        assertEquals("txrt1:qqqq-qqll-ljsf-p98",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_REGTEST, Txref.MAGIC_BTC_REGTEST, 0, 0x7FFF));
        assertEquals("txrt1:q7ll-llqq-qap7-nsg",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_REGTEST, Txref.MAGIC_BTC_REGTEST, 0xFFFFFF, 0));
        assertEquals("txrt1:q7ll-llll-lps4-p3p",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_REGTEST, Txref.MAGIC_BTC_REGTEST, 0xFFFFFF, 0x7FFF));
        assertEquals("txrt1:qjk0-uqay-z94s-ynk",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_REGTEST, Txref.MAGIC_BTC_REGTEST, 466793, 2205));
    }

    @Test
    public void txrefDecode_mainnet() {
        DecodedResult decodedResult;

        decodedResult = Txref.Impl.txrefDecode("tx1:rqqq-qqqq-qwtv-vjr");
        assertEquals(Txref.MAGIC_BTC_MAIN, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("tx1:rqqq-qqll-lj68-7n2");
        assertEquals(Txref.MAGIC_BTC_MAIN, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("tx1:r7ll-llqq-qats-vx9");
        assertEquals(Txref.MAGIC_BTC_MAIN, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("tx1:r7ll-llll-lp6m-78v");
        assertEquals(Txref.MAGIC_BTC_MAIN, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("tx1:rjk0-uqay-z9l7-m9m");
        assertEquals(Txref.MAGIC_BTC_MAIN, decodedResult.getMagicCode());
        assertEquals(466793, decodedResult.getBlockHeight());
        assertEquals(2205, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());
    }

    @Test
    public void txrefDecode_testnet() {
        DecodedResult decodedResult;

        decodedResult = Txref.Impl.txrefDecode("txtest1:xqqq-qqqq-qrrd-ksa");
        assertEquals(Txref.MAGIC_BTC_TEST, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("txtest1:xqqq-qqll-lljx-y35");
        assertEquals(Txref.MAGIC_BTC_TEST, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("txtest1:x7ll-llqq-qsr3-kym");
        assertEquals(Txref.MAGIC_BTC_TEST, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("txtest1:x7ll-llll-lvj6-y9j");
        assertEquals(Txref.MAGIC_BTC_TEST, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("txtest1:xjk0-uqay-zghl-p89");
        assertEquals(Txref.MAGIC_BTC_TEST, decodedResult.getMagicCode());
        assertEquals(466793, decodedResult.getBlockHeight());
        assertEquals(2205, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());
    }

    @Test
    public void txrefDecode_regtest() {
        DecodedResult decodedResult;

        decodedResult = Txref.Impl.txrefDecode("txrt1:qqqq-qqqq-qwpz-nyw");
        assertEquals(Txref.MAGIC_BTC_REGTEST, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("txrt1:qqqq-qqll-ljsf-p98");
        assertEquals(Txref.MAGIC_BTC_REGTEST, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("txrt1:q7ll-llqq-qap7-nsg");
        assertEquals(Txref.MAGIC_BTC_REGTEST, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("txrt1:q7ll-llll-lps4-p3p");
        assertEquals(Txref.MAGIC_BTC_REGTEST, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());
    }


    // Extended Txrefs...

    @Test
    public void extractExtendedMagicCode_mainnet() {
        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("tx1yjk0uqayzu4xx22sy6");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, Txref.Impl.extractMagicCode(decodedResult.getDp()));
    }

    @Test
    public void extractExtendedMagicCode_testnet() {
        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("txtest18jk0uqayzu4xgj9m8a");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, Txref.Impl.extractMagicCode(decodedResult.getDp()));
    }

    @Test
    public void extractExtendedBlockHeight() {

        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("tx1yqqqqqqqqqqqrvum0c");
        int blockHeight = Txref.Impl.extractBlockHeight(decodedResult.getDp());
        assertEquals(0, blockHeight);

        decodedResult = Bech32.decode("tx1y7llllqqqqqqggjgw6");
        blockHeight = Txref.Impl.extractBlockHeight(decodedResult.getDp());
        assertEquals(0xFFFFFF, blockHeight);

        decodedResult = Bech32.decode("tx1yjk0uqayzu4xx22sy6");
        blockHeight = Txref.Impl.extractBlockHeight(decodedResult.getDp());
        assertEquals(466793, blockHeight);

        decodedResult = Bech32.decode("txtest18jk0uqayzu4xgj9m8a");
        blockHeight = Txref.Impl.extractBlockHeight(decodedResult.getDp());
        assertEquals(466793, blockHeight);
    }

    @Test
    public void extractExtendedTransactionPosition() {

        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("tx1yqqqqqqqqqqqrvum0c");
        int transactionPosition = Txref.Impl.extractTransactionPosition(decodedResult.getDp());
        assertEquals(0, transactionPosition);

        decodedResult = Bech32.decode("tx1yqqqqqlllqqqen8x05");
        transactionPosition = Txref.Impl.extractTransactionPosition(decodedResult.getDp());
        assertEquals(0x7FFF, transactionPosition);

        decodedResult = Bech32.decode("tx1yjk0uqayzu4xx22sy6");
        transactionPosition = Txref.Impl.extractTransactionPosition(decodedResult.getDp());
        assertEquals(2205, transactionPosition);

        decodedResult = Bech32.decode("txtest18jk0uqayzu4xgj9m8a");
        transactionPosition = Txref.Impl.extractTransactionPosition(decodedResult.getDp());
        assertEquals(2205, transactionPosition);
    }

    @Test
    public void extractExtendedTxoIndex() {
        design.contract.bech32.DecodedResult decodedResult = Bech32.decode("tx1yqqqqqqqqqqqrvum0c");
        int txoIndex = Txref.Impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(0, txoIndex);

        decodedResult = Bech32.decode("tx1yqqqqqqqqpqqpw4vkq");
        txoIndex = Txref.Impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(1, txoIndex);

        decodedResult = Bech32.decode("tx1yqqqqqqqqu4xj4f2xe");
        txoIndex = Txref.Impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(0x1ABC, txoIndex);

        decodedResult = Bech32.decode("tx1yjk0uqayzu4xx22sy6");
        txoIndex = Txref.Impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(0x1ABC, txoIndex);

        decodedResult = Bech32.decode("txtest18jk0uqayzu4xgj9m8a");
        txoIndex = Txref.Impl.extractTxoIndex(decodedResult.getDp());
        assertEquals(0x1ABC, txoIndex);
    }

    @Test
    public void addHrpIfNeeded_isNeeded_extended() {
        assertEquals("tx1yjk0uqayzu4xnk6upc", Txref.Impl.addHrpIfNeeded("yjk0uqayzu4xnk6upc"));
        assertEquals("txtest18jk0uqayzu4xaw4hzl", Txref.Impl.addHrpIfNeeded("8jk0uqayzu4xaw4hzl"));
        assertEquals("txrt1p7lllllllpqqqa0dvp", Txref.Impl.addHrpIfNeeded("p7lllllllpqqqa0dvp"));
    }

    @Test
    public void addHrpIfNeeded_isNotNeeded_extended() {
        assertEquals("tx1yjk0uqayzu4xnk6upc", Txref.Impl.addHrpIfNeeded("tx1yjk0uqayzu4xnk6upc"));
        assertEquals("txtest18jk0uqayzu4xaw4hzl", Txref.Impl.addHrpIfNeeded("txtest18jk0uqayzu4xaw4hzl"));
        assertEquals("txrt1p7lllllllpqqqa0dvp", Txref.Impl.addHrpIfNeeded("txrt1p7lllllllpqqqa0dvp"));
    }

    @Test
    public void txrefEncode_extended_mainnet() {
        assertEquals("tx1:yqqq-qqqq-qqqq-rvum-0c",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0, 0));
        assertEquals("tx1:yqqq-qqll-lqqq-en8x-05",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0x7FFF, 0));
        assertEquals("tx1:y7ll-llqq-qqqq-ggjg-w6",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0, 0));
        assertEquals("tx1:y7ll-llll-lqqq-jhf4-wk",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x7FFF, 0));

        assertEquals("tx1:yqqq-qqqq-qpqq-pw4v-kq",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0, 1));
        assertEquals("tx1:yqqq-qqll-lpqq-m3w3-kv",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0x7FFF, 1));
        assertEquals("tx1:y7ll-llqq-qpqq-22ml-hz",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0, 1));
        assertEquals("tx1:y7ll-llll-lpqq-s4qz-hw",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x7FFF, 1));

        assertEquals("tx1:yqqq-qqqq-qu4x-j4f2-xe",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0, 0x1ABC));
        assertEquals("tx1:yqqq-qqll-lu4x-g2jh-x4",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0x7FFF, 0x1ABC));
        assertEquals("tx1:y7ll-llqq-qu4x-e38e-8m",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0, 0x1ABC));
        assertEquals("tx1:y7ll-llll-lu4x-rwuy-8h",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x7FFF, 0x1ABC));

        assertEquals("tx1:yjk0-uqay-zu4x-x22s-y6",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 466793, 2205, 0x1ABC));
    }

    @Test
    public void txrefEncode_extended_testnet() {
        assertEquals("txtest1:8qqq-qqqq-qqqq-d5ns-vl",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0, 0));
        assertEquals("txtest1:8qqq-qqll-lqqq-htgd-vn",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0x7FFF, 0));
        assertEquals("txtest1:87ll-llqq-qqqq-xsar-da",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0, 0));
        assertEquals("txtest1:87ll-llll-lqqq-u0x7-d3",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x7FFF, 0));

        assertEquals("txtest1:8qqq-qqqq-qpqq-0k68-48",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0, 1));
        assertEquals("txtest1:8qqq-qqll-lpqq-4fp6-4t",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0x7FFF, 1));
        assertEquals("txtest1:87ll-llqq-qpqq-yj55-59",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0, 1));
        assertEquals("txtest1:87ll-llll-lpqq-7d0f-5f",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x7FFF, 1));

        assertEquals("txtest1:8qqq-qqqq-qu4x-udxp-97",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0, 0x1ABC));
        assertEquals("txtest1:8qqq-qqll-lu4x-xjau-9j",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0x7FFF, 0x1ABC));
        assertEquals("txtest1:87ll-llqq-qu4x-hfgj-yu",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0, 0x1ABC));
        assertEquals("txtest1:87ll-llll-lu4x-dkn0-ys",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x7FFF, 0x1ABC));

        assertEquals("txtest1:8jk0-uqay-zu4x-gj9m-8a",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 466793, 2205, 0x1ABC));
    }

    @Test
    public void txrefEncode_extended_regtest() {
        assertEquals("txrt1:pqqq-qqqq-qpqq-3x6r-d0",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_REGTEST, Txref.MAGIC_BTC_REGTEST_EXTENDED, 0, 0, 1));
        assertEquals("txrt1:pqqq-qqll-lpqq-tep7-dr",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_REGTEST, Txref.MAGIC_BTC_REGTEST_EXTENDED, 0, 0x7FFF, 1));
        assertEquals("txrt1:p7ll-llqq-qpqq-6z5s-vd",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_REGTEST, Txref.MAGIC_BTC_REGTEST_EXTENDED, 0xFFFFFF, 0, 1));
        assertEquals("txrt1:p7ll-llll-lpqq-qa0d-vp",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_REGTEST, Txref.MAGIC_BTC_REGTEST_EXTENDED, 0xFFFFFF, 0x7FFF, 1));
    }

    @Test
    public void txrefDecode_extended_mainnet() {
        DecodedResult decodedResult;

        decodedResult = Txref.Impl.txrefDecode("tx1:yqqq-qqqq-qqqq-rvum-0c");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("tx1:yqqq-qqll-lqqq-en8x-05");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("tx1:y7ll-llqq-qqqq-ggjg-w6");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("tx1:y7ll-llll-lqqq-jhf4-wk");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("tx1:yqqq-qqqq-qpqq-pw4v-kq");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("tx1:yqqq-qqll-lpqq-m3w3-kv");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("tx1:y7ll-llqq-qpqq-22ml-hz");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("tx1:y7ll-llll-lpqq-s4qz-hw");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("tx1:yqqq-qqqq-qu4x-j4f2-xe");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0x1ABC, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("tx1:yqqq-qqll-lu4x-g2jh-x4");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0x1ABC, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("tx1:y7ll-llqq-qu4x-e38e-8m");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0x1ABC, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("tx1:y7ll-llll-lu4x-rwuy-8h");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0x1ABC, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("tx1:yjk0-uqay-zu4x-x22s-y6");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(466793, decodedResult.getBlockHeight());
        assertEquals(2205, decodedResult.getTransactionPosition());
        assertEquals(0x1ABC, decodedResult.getTxoIndex());
    }

    @Test
    public void txrefDecode_extended_testnet() {
        DecodedResult decodedResult;

        decodedResult = Txref.Impl.txrefDecode("txtest1:8qqq-qqqq-qqqq-d5ns-vl");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("txtest1:8qqq-qqll-lqqq-htgd-vn");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("txtest1:87ll-llqq-qqqq-xsar-da");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("txtest1:87ll-llll-lqqq-u0x7-d3");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("txtest1:8qqq-qqqq-qpqq-0k68-48");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("txtest1:8qqq-qqll-lpqq-4fp6-4t");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("txtest1:87ll-llqq-qpqq-yj55-59");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("txtest1:87ll-llll-lpqq-7d0f-5f");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("txtest1:8qqq-qqqq-qu4x-udxp-97");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0x1ABC, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("txtest1:8qqq-qqll-lu4x-xjau-9j");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0x1ABC, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("txtest1:87ll-llqq-qu4x-hfgj-yu");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0x1ABC, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("txtest1:87ll-llll-lu4x-dkn0-ys");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0x1ABC, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("txtest1:8jk0-uqay-zu4x-gj9m-8a");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(466793, decodedResult.getBlockHeight());
        assertEquals(2205, decodedResult.getTransactionPosition());
        assertEquals(0x1ABC, decodedResult.getTxoIndex());

    }

    @Test
    public void txrefDecode_extended_regtest() {
        DecodedResult decodedResult;

        decodedResult = Txref.Impl.txrefDecode("txrt1:pqqq-qqqq-qpqq-3x6r-d0");
        assertEquals(Txref.MAGIC_BTC_REGTEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("txrt1:pqqq-qqll-lpqq-tep7-dr");
        assertEquals(Txref.MAGIC_BTC_REGTEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("txrt1:p7ll-llqq-qpqq-6z5s-vd");
        assertEquals(Txref.MAGIC_BTC_REGTEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());
        decodedResult = Txref.Impl.txrefDecode("txrt1:p7ll-llll-lpqq-qa0d-vp");
        assertEquals(Txref.MAGIC_BTC_REGTEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());
    }

    // //////////////// Examples from BIP-0136 /////////////////////

    // check that we correctly encode some sample txrefs from BIP-0136. These may duplicate
    // some tests above, but many of the examples in the BIP are present here for reference.

    @Test
    public void txrefEncode_bip_examples() {
        // Genesis Coinbase Transaction (Transaction #0 of Block #0):
        assertEquals("tx1:rqqq-qqqq-qwtv-vjr",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0, 0));

        // Transaction #1 of Block #170:
        assertEquals("tx1:r52q-qqpq-qpty-cfg",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 170, 1));

        // Transaction #1234 of Block #456789, with outpoint 1:
        assertEquals("tx1:y29u-mqjx-ppqq-sfp2-tt",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 456789, 1234, 1));

        // The following list gives properly encoded mainnet TxRef's
        assertEquals("tx1:rqqq-qqqq-qwtv-vjr",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0, 0));
        assertEquals("tx1:rqqq-qqll-lj68-7n2",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0, 0x7FFF));
        assertEquals("tx1:r7ll-llqq-qats-vx9",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0xFFFFFF, 0x0));
        assertEquals("tx1:r7ll-llll-lp6m-78v",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0xFFFFFF, 0x7FFF));

        // The following list gives properly encoded testnet TxRef's
        assertEquals("txtest1:xqqq-qqqq-qrrd-ksa",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0, 0));
        assertEquals("txtest1:xqqq-qqll-lljx-y35",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0, 0x7FFF));
        assertEquals("txtest1:x7ll-llqq-qsr3-kym",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0xFFFFFF, 0x0));
        assertEquals("txtest1:x7ll-llll-lvj6-y9j",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0xFFFFFF, 0x7FFF));

        // The following list gives valid (sometimes strangely formatted) TxRef's
        assertEquals("tx1:r29u-mqjx-putt-3p0",
                Txref.Impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 456789, 1234));

        // The following list gives properly encoded mainnet TxRef's with Outpoints
        assertEquals("tx1:yqqq-qqqq-qqqq-rvum-0c",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0, 0));
        assertEquals("tx1:yqqq-qqll-lqqq-en8x-05",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0x7FFF, 0));
        assertEquals("tx1:y7ll-llqq-qqqq-ggjg-w6",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x0, 0));
        assertEquals("tx1:y7ll-llll-lqqq-jhf4-wk",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x7FFF, 0));

        assertEquals("tx1:yqqq-qqqq-qpqq-pw4v-kq",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0, 1));
        assertEquals("tx1:yqqq-qqll-lpqq-m3w3-kv",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0x7FFF, 1));
        assertEquals("tx1:y7ll-llqq-qpqq-22ml-hz",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x0, 1));
        assertEquals("tx1:y7ll-llll-lpqq-s4qz-hw",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x7FFF, 1));

        assertEquals("tx1:y29u-mqjx-ppqq-sfp2-tt",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 456789, 1234, 1));

        // The following list gives properly encoded testnet TxRef's with Outpoints
        assertEquals("txtest1:8qqq-qqqq-qqqq-d5ns-vl",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0, 0));
        assertEquals("txtest1:8qqq-qqll-lqqq-htgd-vn",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0x7FFF, 0));
        assertEquals("txtest1:87ll-llqq-qqqq-xsar-da",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x0, 0));
        assertEquals("txtest1:87ll-llll-lqqq-u0x7-d3",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x7FFF, 0));

        assertEquals("txtest1:8qqq-qqqq-qpqq-0k68-48",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0, 1));
        assertEquals("txtest1:8qqq-qqll-lpqq-4fp6-4t",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0x7FFF, 1));
        assertEquals("txtest1:87ll-llqq-qpqq-yj55-59",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x0, 1));
        assertEquals("txtest1:87ll-llll-lpqq-7d0f-5f",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x7FFF, 1));

        assertEquals("txtest1:829u-mqjx-ppqq-73wp-gv",
                Txref.Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 456789, 1234, 1));
    }

    @Test
    public void txrefDecode_bip_examples() {

        DecodedResult decodedResult;

        // Genesis Coinbase Transaction (Transaction #0 of Block #0):
        decodedResult = Txref.Impl.txrefDecode("tx1:rqqq-qqqq-qwtv-vjr");
        assertEquals(Txref.MAGIC_BTC_MAIN, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        // Transaction #1 of Block #170:
        decodedResult = Txref.Impl.txrefDecode("tx1:r52q‑qqpq‑qpty‑cfg");
        assertEquals(Txref.MAGIC_BTC_MAIN, decodedResult.getMagicCode());
        assertEquals(170, decodedResult.getBlockHeight());
        assertEquals(1, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        // Transaction #1234 of Block #456789, with outpoint 1:
        decodedResult = Txref.Impl.txrefDecode("tx1:y29u‑mqjx‑ppqq‑sfp2‑tt");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(456789, decodedResult.getBlockHeight());
        assertEquals(1234, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());


        // The following list gives properly encoded mainnet TxRef's
        decodedResult = Txref.Impl.txrefDecode("tx1:rqqq-qqqq-qwtv-vjr");
        assertEquals(Txref.MAGIC_BTC_MAIN, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("tx1:rqqq-qqll-lj68-7n2");
        assertEquals(Txref.MAGIC_BTC_MAIN, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("tx1:r7ll-llqq-qats-vx9");
        assertEquals(Txref.MAGIC_BTC_MAIN, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("tx1:r7ll-llll-lp6m-78v");
        assertEquals(Txref.MAGIC_BTC_MAIN, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());


        // The following list gives properly encoded testnet TxRef's
        decodedResult = Txref.Impl.txrefDecode("txtest1:xqqq-qqqq-qrrd-ksa");
        assertEquals(Txref.MAGIC_BTC_TEST, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("txtest1:xqqq-qqll-lljx-y35");
        assertEquals(Txref.MAGIC_BTC_TEST, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("txtest1:x7ll-llqq-qsr3-kym");
        assertEquals(Txref.MAGIC_BTC_TEST, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("txtest1:x7ll-llll-lvj6-y9j");
        assertEquals(Txref.MAGIC_BTC_TEST, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());


        // The following list gives valid (sometimes strangely formatted) TxRef's
        decodedResult = Txref.Impl.txrefDecode("tx1:r29u-mqjx-putt-3p0");
        assertEquals(Txref.MAGIC_BTC_MAIN, decodedResult.getMagicCode());
        assertEquals(456789, decodedResult.getBlockHeight());
        assertEquals(1234, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("TX1R29UMQJXPUTT3P0");
        assertEquals(Txref.MAGIC_BTC_MAIN, decodedResult.getMagicCode());
        assertEquals(456789, decodedResult.getBlockHeight());
        assertEquals(1234, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("TX1R29umqJX--PUTT----3P0");
        assertEquals(Txref.MAGIC_BTC_MAIN, decodedResult.getMagicCode());
        assertEquals(456789, decodedResult.getBlockHeight());
        assertEquals(1234, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("tx1 r29u mqjx putt 3p0");
        assertEquals(Txref.MAGIC_BTC_MAIN, decodedResult.getMagicCode());
        assertEquals(456789, decodedResult.getBlockHeight());
        assertEquals(1234, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("tx1!r29u/mqj*x-putt^^3p0");
        assertEquals(Txref.MAGIC_BTC_MAIN, decodedResult.getMagicCode());
        assertEquals(456789, decodedResult.getBlockHeight());
        assertEquals(1234, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());


        // The following list gives properly encoded mainnet TxRef's with Outpoints
        decodedResult = Txref.Impl.txrefDecode("tx1:yqqq-qqqq-qqqq-rvum-0c");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("tx1:yqqq-qqll-lqqq-en8x-05");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("tx1:y7ll-llqq-qqqq-ggjg-w6");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("tx1:y7ll-llll-lqqq-jhf4-wk");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());


        decodedResult = Txref.Impl.txrefDecode("tx1:yqqq-qqqq-qpqq-pw4v-kq");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("tx1:yqqq-qqll-lpqq-m3w3-kv");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("tx1:y7ll-llqq-qpqq-22ml-hz");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("tx1:y7ll-llll-lpqq-s4qz-hw");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());


        decodedResult = Txref.Impl.txrefDecode("tx1:y29u-mqjx-ppqq-sfp2-tt");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, decodedResult.getMagicCode());
        assertEquals(456789, decodedResult.getBlockHeight());
        assertEquals(1234, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());


        // The following list gives properly encoded testnet TxRef's with Outpoints
        decodedResult = Txref.Impl.txrefDecode("txtest1:8qqq-qqqq-qqqq-d5ns-vl");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("txtest1:8qqq-qqll-lqqq-htgd-vn");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("txtest1:87ll-llqq-qqqq-xsar-da");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("txtest1:87ll-llll-lqqq-u0x7-d3");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(0, decodedResult.getTxoIndex());


        decodedResult = Txref.Impl.txrefDecode("txtest1:8qqq-qqqq-qpqq-0k68-48");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("txtest1:8qqq-qqll-lpqq-4fp6-4t");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("txtest1:87ll-llqq-qpqq-yj55-59");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());

        decodedResult = Txref.Impl.txrefDecode("txtest1:87ll-llll-lpqq-7d0f-5f");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(0xFFFFFF, decodedResult.getBlockHeight());
        assertEquals(0x7FFF, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());


        decodedResult = Txref.Impl.txrefDecode("txtest1:829u-mqjx-ppqq-73wp-gv");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, decodedResult.getMagicCode());
        assertEquals(456789, decodedResult.getBlockHeight());
        assertEquals(1234, decodedResult.getTransactionPosition());
        assertEquals(1, decodedResult.getTxoIndex());
    }

    @Test
    public void txrefDecode_checkEncoding() {

        DecodedResult decodedResult;

        decodedResult = Txref.Impl.txrefDecode("txtest1:8jk0-uqay-zu4x-gj9m-8a");
        assertEquals(DecodedResult.Encoding.BECH32M, decodedResult.getEncoding());

        decodedResult = Txref.Impl.txrefDecode("txtest1:8jk0-uqay-zu4x-aw4h-zl");
        assertEquals(DecodedResult.Encoding.BECH32, decodedResult.getEncoding());
    }

    @Test
    public void txrefDecode_checkCommentary_oldEncoding() {

        DecodedResult decodedResult;

        decodedResult = Txref.Impl.txrefDecode("txtest1:8jk0-uqay-zu4x-gj9m-8a");
        assertEquals(DecodedResult.Encoding.BECH32M, decodedResult.getEncoding());
        assertEquals("", decodedResult.getCommentary());

        decodedResult = Txref.Impl.txrefDecode("txtest1:8jk0-uqay-zu4x-aw4h-zl");
        assertEquals(DecodedResult.Encoding.BECH32, decodedResult.getEncoding());
        assertNotEquals("", decodedResult.getCommentary());
        assertTrue(decodedResult.getCommentary().contains("txtest1:8jk0-uqay-zu4x-gj9m-8a"));
    }

    @Test
    public void txrefDecode_checkCommentary_mixedCase() {

        DecodedResult decodedResult;

        decodedResult = Txref.Impl.txrefDecode("txtest1:8jk0-uQay-zu4x-gj9m-8a");
        assertEquals(DecodedResult.Encoding.BECH32M, decodedResult.getEncoding());
        assertNotEquals("", decodedResult.getCommentary());
        assertTrue(decodedResult.getCommentary().contains("txtest1:8jk0-uqay-zu4x-gj9m-8a"));
    }

    @Test
    public void containsUppercaseCharacters() {
        assertFalse(Txref.Impl.cleanTxrefContainsUppercaseCharacters("test"));
        assertTrue(Txref.Impl.cleanTxrefContainsUppercaseCharacters("TEST"));
        assertTrue(Txref.Impl.cleanTxrefContainsUppercaseCharacters("Test"));
        assertFalse(Txref.Impl.cleanTxrefContainsUppercaseCharacters("123abc"));
    }

    @Test
    public void containsLowercaseCharacters() {
        assertTrue(Txref.Impl.cleanTxrefContainsLowercaseCharacters("test"));
        assertFalse(Txref.Impl.cleanTxrefContainsLowercaseCharacters("TEST"));
        assertTrue(Txref.Impl.cleanTxrefContainsLowercaseCharacters("Test"));
        assertFalse(Txref.Impl.cleanTxrefContainsLowercaseCharacters("123ABC"));
    }

    @Test
    public void containsMixedcaseCharacters() {
        assertFalse(Txref.Impl.cleanTxrefContainsMixedcaseCharacters("test"));
        assertFalse(Txref.Impl.cleanTxrefContainsMixedcaseCharacters("TEST"));
        assertTrue(Txref.Impl.cleanTxrefContainsMixedcaseCharacters("Test"));
        assertFalse(Txref.Impl.cleanTxrefContainsMixedcaseCharacters("123"));
        assertFalse(Txref.Impl.cleanTxrefContainsMixedcaseCharacters("123a"));
        assertTrue(Txref.Impl.cleanTxrefContainsMixedcaseCharacters("A123a"));
    }

}
