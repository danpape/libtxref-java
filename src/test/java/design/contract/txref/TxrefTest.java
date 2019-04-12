package design.contract.txref;

import design.contract.bech32.Bech32;
import design.contract.bech32.HrpAndDp;
import org.junit.Test;

import static org.junit.Assert.*;

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
        Txref.impl.checkBlockHeightRange(Txref.limits.MAX_BLOCK_HEIGHT+1);
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
        Txref.impl.checkTransactionPositionRange(Txref.limits.MAX_TRANSACTION_POSITION+1);
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
        Txref.impl.checkTxoIndexRange(Txref.limits.MAX_TXO_INDEX+1);
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
        Txref.impl.checkMagicCodeRange(Txref.limits.MAX_MAGIC_CODE+1);
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
        Txref.impl.addGroupSeparators("00", Bech32.limits.MAX_HRP_LENGTH+1, 1);
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

    @Test
    public void extractMagicCode_mainnet() {
        HrpAndDp hd = Bech32.decode("tx1rqqqqqqqqmhuqhp");
        assertEquals(Txref.MAGIC_BTC_MAIN, Txref.impl.extractMagicCode(hd));
    }

    @Test
    public void extractMagicCode_testnet() {
        HrpAndDp hd = Bech32.decode("txtest1xjk0uqayzat0dz8");
        assertEquals(Txref.MAGIC_BTC_TEST, Txref.impl.extractMagicCode(hd));
    }

    @Test
    public void extractVersion_mainnet() {
        HrpAndDp hd = Bech32.decode("tx1rqqqqqqqqmhuqhp");
        assertEquals(0, Txref.impl.extractVersion(hd));
    }

    @Test
    public void extractVersion_testnet() {
        HrpAndDp hd = Bech32.decode("txtest1xjk0uqayzat0dz8");
        assertEquals(0, Txref.impl.extractVersion(hd));
    }

    @Test
    public void extractBlockHeight() {

        HrpAndDp hd = Bech32.decode ("tx1rqqqqqqqqmhuqhp");
        int blockHeight = Txref.impl.extractBlockHeight(hd);
        assertEquals(0, blockHeight);

        hd = Bech32.decode ("tx1rqqqqqlll8xhjkg");
        blockHeight = Txref.impl.extractBlockHeight(hd);
        assertEquals(0, blockHeight);

        hd = Bech32.decode ("tx1r7llllqqqghqqr8");
        blockHeight = Txref.impl.extractBlockHeight(hd);
        assertEquals(0xFFFFFF, blockHeight);

        hd = Bech32.decode ("tx1r7lllllll5xtjzw");
        blockHeight = Txref.impl.extractBlockHeight(hd);
        assertEquals(0xFFFFFF, blockHeight);

        hd = Bech32.decode ("tx1rjk0uqayzsrwhqe");
        blockHeight = Txref.impl.extractBlockHeight(hd);
        assertEquals(466793, blockHeight);

        hd = Bech32.decode ("txtest1xjk0uqayzat0dz8");
        blockHeight = Txref.impl.extractBlockHeight(hd);
        assertEquals(466793, blockHeight);
    }

    @Test
    public void extractTransactionPosition() {

        HrpAndDp hd = Bech32.decode ("tx1rqqqqqqqqmhuqhp");
        int transactionPosition = Txref.impl.extractTransactionPosition(hd);
        assertEquals(0, transactionPosition);

        hd = Bech32.decode ("tx1rqqqqqlll8xhjkg");
        transactionPosition = Txref.impl.extractTransactionPosition(hd);
        assertEquals(0x7FFF, transactionPosition);

        hd = Bech32.decode ("tx1r7llllqqqghqqr8");
        transactionPosition = Txref.impl.extractTransactionPosition(hd);
        assertEquals(0, transactionPosition);

        hd = Bech32.decode ("tx1r7lllllll5xtjzw");
        transactionPosition = Txref.impl.extractTransactionPosition(hd);
        assertEquals(0x7FFF, transactionPosition);

        hd = Bech32.decode ("tx1rjk0uqayzsrwhqe");
        transactionPosition = Txref.impl.extractTransactionPosition(hd);
        assertEquals(2205, transactionPosition);

        hd = Bech32.decode ("txtest1xjk0uqayzat0dz8");
        transactionPosition = Txref.impl.extractTransactionPosition(hd);
        assertEquals(2205, transactionPosition);
    }

    @Test
    public void extractTxoIndex() {
        // these will all return 0 as none are extended txrefs

        HrpAndDp hd = Bech32.decode ("tx1rqqqqqqqqmhuqhp");
        int txoIndex = Txref.impl.extractTxoIndex(hd);
        assertEquals(0, txoIndex);

        hd = Bech32.decode ("tx1rqqqqqlll8xhjkg");
        txoIndex = Txref.impl.extractTxoIndex(hd);
        assertEquals(0, txoIndex);

        hd = Bech32.decode ("tx1r7llllqqqghqqr8");
        txoIndex = Txref.impl.extractTxoIndex(hd);
        assertEquals(0, txoIndex);

        hd = Bech32.decode ("tx1r7lllllll5xtjzw");
        txoIndex = Txref.impl.extractTxoIndex(hd);
        assertEquals(0, txoIndex);

        hd = Bech32.decode ("tx1rjk0uqayzsrwhqe");
        txoIndex = Txref.impl.extractTxoIndex(hd);
        assertEquals(0, txoIndex);

        hd = Bech32.decode ("txtest1xjk0uqayzat0dz8");
        txoIndex = Txref.impl.extractTxoIndex(hd);
        assertEquals(0, txoIndex);
    }

    @Test
    public void addHrpIfNeeded_isNeeded() {
        assertEquals("tx1rqqqqqqqqmhuqhp", Txref.impl.addHrpIfNeeded("rqqqqqqqqmhuqhp"));
        assertEquals("txtest1xjk0uqayzat0dz8", Txref.impl.addHrpIfNeeded("xjk0uqayzat0dz8"));
    }

    @Test
    public void addHrpIfNeeded_isNotNeeded() {
        assertEquals("tx1rqqqqqqqqmhuqhp", Txref.impl.addHrpIfNeeded("tx1rqqqqqqqqmhuqhp"));
        assertEquals("txtest1xjk0uqayzat0dz8", Txref.impl.addHrpIfNeeded("txtest1xjk0uqayzat0dz8"));
    }

    @Test
    public void txrefEncode_mainnet() {
        assertEquals("tx1:rqqq-qqqq-qmhu-qhp",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0, 0));
        assertEquals("tx1:rqqq-qqll-l8xh-jkg",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0, 0x7FFF));
        assertEquals("tx1:r7ll-llqq-qghq-qr8",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0xFFFFFF, 0));
        assertEquals("tx1:r7ll-llll-l5xt-jzw",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0xFFFFFF, 0x7FFF));
        assertEquals("tx1:rjk0-uqay-zsrw-hqe",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 466793, 2205));
    }

    @Test
    public void txrefEncode_testnet() {
        assertEquals("txtest1:xqqq-qqqq-qkla-64l",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0, 0));
        assertEquals("txtest1:xqqq-qqll-l2wk-g5k",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0, 0x7FFF));
        assertEquals("txtest1:x7ll-llqq-q9lp-6pe",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0xFFFFFF, 0));
        assertEquals("txtest1:x7ll-llll-lew2-gqs",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0xFFFFFF, 0x7FFF));
        assertEquals("txtest1:xjk0-uqay-zat0-dz8",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 466793, 2205));
    }

    @Test
    public void txrefDecode_mainnet() {
        LocationData ld;

        ld = Txref.impl.txrefDecode("tx1:rqqq-qqqq-qmhu-qhp");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:rqqq-qqll-l8xh-jkg");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:r7ll-llqq-qghq-qr8");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:r7ll-llll-l5xt-jzw");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:rjk0-uqay-zsrw-hqe");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(466793, ld.getBlockHeight());
        assertEquals(2205, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());
    }

    @Test
    public void txrefDecode_testnet() {
        LocationData ld;

        ld = Txref.impl.txrefDecode("txtest1:xqqq-qqqq-qkla-64l");
        assertEquals(Txref.MAGIC_BTC_TEST, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:xqqq-qqll-l2wk-g5k");
        assertEquals(Txref.MAGIC_BTC_TEST, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:x7ll-llqq-q9lp-6pe");
        assertEquals(Txref.MAGIC_BTC_TEST, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:x7ll-llll-lew2-gqs");
        assertEquals(Txref.MAGIC_BTC_TEST, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:xjk0-uqay-zat0-dz8");
        assertEquals(Txref.MAGIC_BTC_TEST, ld.getMagicCode());
        assertEquals(466793, ld.getBlockHeight());
        assertEquals(2205, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());
    }


    // Extended Txrefs...

    @Test
    public void extractExtendedMagicCode_mainnet() {
        HrpAndDp hd = Bech32.decode("tx1yjk0uqayzu4xnk6upc");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, Txref.impl.extractMagicCode(hd));
    }

    @Test
    public void extractExtendedMagicCode_testnet() {
        HrpAndDp hd = Bech32.decode("txtest18jk0uqayzu4xaw4hzl");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, Txref.impl.extractMagicCode(hd));
    }

    @Test
    public void extractExtendedBlockHeight() {

        HrpAndDp hd = Bech32.decode ("tx1yqqqqqqqqqqqksvh26");
        int blockHeight = Txref.impl.extractBlockHeight(hd);
        assertEquals(0, blockHeight);

        hd = Bech32.decode ("tx1y7llllqqqqqqa5zytc");
        blockHeight = Txref.impl.extractBlockHeight(hd);
        assertEquals(0xFFFFFF, blockHeight);

        hd = Bech32.decode ("tx1yjk0uqayzu4xnk6upc");
        blockHeight = Txref.impl.extractBlockHeight(hd);
        assertEquals(466793, blockHeight);

        hd = Bech32.decode ("txtest18jk0uqayzu4xaw4hzl");
        blockHeight = Txref.impl.extractBlockHeight(hd);
        assertEquals(466793, blockHeight);
    }

    @Test
    public void extractExtendedTransactionPosition() {

        HrpAndDp hd = Bech32.decode ("tx1yqqqqqqqqqqqksvh26");
        int transactionPosition = Txref.impl.extractTransactionPosition(hd);
        assertEquals(0, transactionPosition);

        hd = Bech32.decode ("tx1yqqqqqlllqqqv0h22k");
        transactionPosition = Txref.impl.extractTransactionPosition(hd);
        assertEquals(0x7FFF, transactionPosition);

        hd = Bech32.decode ("tx1yjk0uqayzu4xnk6upc");
        transactionPosition = Txref.impl.extractTransactionPosition(hd);
        assertEquals(2205, transactionPosition);

        hd = Bech32.decode ("txtest18jk0uqayzu4xaw4hzl");
        transactionPosition = Txref.impl.extractTransactionPosition(hd);
        assertEquals(2205, transactionPosition);
    }

    @Test
    public void extractExtendedTxoIndex() {
        HrpAndDp hd = Bech32.decode ("tx1yqqqqqqqqqqqksvh26");
        int txoIndex = Txref.impl.extractTxoIndex(hd);
        assertEquals(0, txoIndex);

        hd = Bech32.decode ("tx1yqqqqqqqqpqq5j9qnz");
        txoIndex = Txref.impl.extractTxoIndex(hd);
        assertEquals(1, txoIndex);

        hd = Bech32.decode ("tx1yqqqqqqqqu4x8fexrm");
        txoIndex = Txref.impl.extractTxoIndex(hd);
        assertEquals(0x1ABC, txoIndex);

        hd = Bech32.decode ("tx1yjk0uqayzu4xnk6upc");
        txoIndex = Txref.impl.extractTxoIndex(hd);
        assertEquals(0x1ABC, txoIndex);

        hd = Bech32.decode ("txtest18jk0uqayzu4xaw4hzl");
        txoIndex = Txref.impl.extractTxoIndex(hd);
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
        assertEquals("tx1:yqqq-qqqq-qqqq-ksvh-26",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0, 0));
        assertEquals("tx1:yqqq-qqll-lqqq-v0h2-2k",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0x7FFF, 0));
        assertEquals("tx1:y7ll-llqq-qqqq-a5zy-tc",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0, 0));
        assertEquals("tx1:y7ll-llll-lqqq-8tee-t5",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x7FFF, 0));

        assertEquals("tx1:yqqq-qqqq-qpqq-5j9q-nz",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0, 1));
        assertEquals("tx1:yqqq-qqll-lpqq-wd7a-nw",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0x7FFF, 1));
        assertEquals("tx1:y7ll-llqq-qpqq-lktn-jq",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0, 1));
        assertEquals("tx1:y7ll-llll-lpqq-9fsw-jv",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x7FFF, 1));

        assertEquals("tx1:yqqq-qqqq-qu4x-8fex-rm",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0, 0x1ABC));
        assertEquals("tx1:yqqq-qqll-lu4x-akzm-rh",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0x7FFF, 0x1ABC));
        assertEquals("tx1:y7ll-llqq-qu4x-vdh4-ze",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0, 0x1ABC));
        assertEquals("tx1:y7ll-llll-lu4x-kjvg-z4",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x7FFF, 0x1ABC));

        assertEquals("tx1:yjk0-uqay-zu4x-nk6u-pc",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 466793, 2205, 0x1ABC));
    }

    @Test
    public void txrefEncode_extended_testnet() {
        assertEquals("txtest1:8qqq-qqqq-qqqq-cgru-fa",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0, 0));
        assertEquals("txtest1:8qqq-qqll-lqqq-zhcp-f3",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0x7FFF, 0));
        assertEquals("txtest1:87ll-llqq-qqqq-nvd0-gl",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0, 0));
        assertEquals("txtest1:87ll-llll-lqqq-fnkj-gn",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x7FFF, 0));

        assertEquals("txtest1:8qqq-qqqq-qpqq-622t-s9",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0, 1));
        assertEquals("txtest1:8qqq-qqll-lpqq-q43k-sf",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0x7FFF, 1));
        assertEquals("txtest1:87ll-llqq-qpqq-3wyc-38",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0, 1));
        assertEquals("txtest1:87ll-llll-lpqq-t3l9-3t",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x7FFF, 1));

        assertEquals("txtest1:8qqq-qqqq-qu4x-f3kd-qu",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0, 0x1ABC));
        assertEquals("txtest1:8qqq-qqll-lu4x-nwds-qs",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0x7FFF, 0x1ABC));
        assertEquals("txtest1:87ll-llqq-qu4x-z4c7-p7",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0, 0x1ABC));
        assertEquals("txtest1:87ll-llll-lu4x-c2rr-pj",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x7FFF, 0x1ABC));

        assertEquals("txtest1:8jk0-uqay-zu4x-aw4h-zl",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 466793, 2205, 0x1ABC));
    }

    @Test
    public void txrefDecode_extended_mainnet() {
        LocationData ld;

        ld = Txref.impl.txrefDecode("tx1:yqqq-qqqq-qqqq-ksvh-26");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("tx1:yqqq-qqll-lqqq-v0h2-2k");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("tx1:y7ll-llqq-qqqq-a5zy-tc");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("tx1:y7ll-llll-lqqq-8tee-t5");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:yqqq-qqqq-qpqq-5j9q-nz");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("tx1:yqqq-qqll-lpqq-wd7a-nw");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("tx1:y7ll-llqq-qpqq-lktn-jq");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("tx1:y7ll-llll-lpqq-9fsw-jv");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:yqqq-qqqq-qu4x-8fex-rm");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("tx1:yqqq-qqll-lu4x-akzm-rh");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("tx1:y7ll-llqq-qu4x-vdh4-ze");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("tx1:y7ll-llll-lu4x-kjvg-z4");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:yjk0-uqay-zu4x-nk6u-pc");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(466793, ld.getBlockHeight());
        assertEquals(2205, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());
    }

    @Test
    public void txrefDecode_extended_testnet() {
        LocationData ld;

        ld = Txref.impl.txrefDecode("txtest1:8qqq-qqqq-qqqq-cgru-fa");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("txtest1:8qqq-qqll-lqqq-zhcp-f3");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("txtest1:87ll-llqq-qqqq-nvd0-gl");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("txtest1:87ll-llll-lqqq-fnkj-gn");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:8qqq-qqqq-qpqq-622t-s9");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("txtest1:8qqq-qqll-lpqq-q43k-sf");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("txtest1:87ll-llqq-qpqq-3wyc-38");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("txtest1:87ll-llll-lpqq-t3l9-3t");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:8qqq-qqqq-qu4x-f3kd-qu");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("txtest1:8qqq-qqll-lu4x-nwds-qs");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("txtest1:87ll-llqq-qu4x-z4c7-p7");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());
        ld = Txref.impl.txrefDecode("txtest1:87ll-llll-lu4x-c2rr-pj");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:8jk0-uqay-zu4x-aw4h-zl");
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
        assertEquals("tx1:rqqq-qqqq-qmhu-qhp",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0, 0));

        // Transaction #2205 of Block #466793:
        assertEquals("tx1:rjk0-uqay-zsrw-hqe",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 466793, 2205));

        // The following list gives properly encoded Bitcoin mainnet TxRef's
        assertEquals("tx1:rqqq-qqqq-qmhu-qhp",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0, 0));
        assertEquals("tx1:rqqq-qqll-l8xh-jkg",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0, 0x7FFF));
        assertEquals("tx1:r7ll-llqq-qghq-qr8",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0xFFFFFF, 0x0));
        assertEquals("tx1:r7ll-llll-l5xt-jzw",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0xFFFFFF, 0x7FFF));

        // The following list gives properly encoded Bitcoin testnet TxRef's
        assertEquals("txtest1:xqqq-qqqq-qkla-64l",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0, 0));
        assertEquals("txtest1:xqqq-qqll-l2wk-g5k",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0, 0x7FFF));
        assertEquals("txtest1:x7ll-llqq-q9lp-6pe",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0xFFFFFF, 0x0));
        assertEquals("txtest1:x7ll-llll-lew2-gqs",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST, 0xFFFFFF, 0x7FFF));

        // The following list gives valid (though strangley formatted) Bitcoin TxRef's
        assertEquals("tx1:rjk0-uqay-zsrw-hqe",
                Txref.impl.txrefEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN, 0x71F69, 0x89D));

        // The following list gives properly encoded Bitcoin mainnet TxRef's with Outpoints
        assertEquals("tx1:yqqq-qqqq-qqqq-ksvh-26",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0, 0));
        assertEquals("tx1:yqqq-qqll-lqqq-v0h2-2k",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0x7FFF, 0));
        assertEquals("tx1:y7ll-llqq-qqqq-a5zy-tc",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x0, 0));
        assertEquals("tx1:y7ll-llll-lqqq-8tee-t5",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x7FFF, 0));

        assertEquals("tx1:yqqq-qqqq-qpqq-5j9q-nz",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0, 1));
        assertEquals("tx1:yqqq-qqll-lpqq-wd7a-nw",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0, 0x7FFF, 1));
        assertEquals("tx1:y7ll-llqq-qpqq-lktn-jq",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x0, 1));
        assertEquals("tx1:y7ll-llll-lpqq-9fsw-jv",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0xFFFFFF, 0x7FFF, 1));

        assertEquals("tx1:yjk0-uqay-zrfq-g2cg-t8",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0x71F69, 0x89D, 0x123));
        assertEquals("tx1:yjk0-uqay-zu4x-nk6u-pc",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, Txref.MAGIC_BTC_MAIN_EXTENDED, 0x71F69, 0x89D, 0x1ABC));

        // The following list gives properly encoded Bitcoin testnet TxRef's with Outpoints
        assertEquals("txtest1:8qqq-qqqq-qqqq-cgru-fa",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0, 0));
        assertEquals("txtest1:8qqq-qqll-lqqq-zhcp-f3",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0x7FFF, 0));
        assertEquals("txtest1:87ll-llqq-qqqq-nvd0-gl",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x0, 0));
        assertEquals("txtest1:87ll-llll-lqqq-fnkj-gn",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x7FFF, 0));

        assertEquals("txtest1:8qqq-qqqq-qpqq-622t-s9",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0, 1));
        assertEquals("txtest1:8qqq-qqll-lpqq-q43k-sf",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0, 0x7FFF, 1));
        assertEquals("txtest1:87ll-llqq-qpqq-3wyc-38",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x0, 1));
        assertEquals("txtest1:87ll-llll-lpqq-t3l9-3t",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0xFFFFFF, 0x7FFF, 1));

        assertEquals("txtest1:8jk0-uqay-zrfq-xjhr-gq",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0x71F69, 0x89D, 0x123));
        assertEquals("txtest1:8jk0-uqay-zu4x-aw4h-zl",
                Txref.impl.txrefExtEncode(Txref.BECH32_HRP_TEST, Txref.MAGIC_BTC_TEST_EXTENDED, 0x71F69, 0x89D, 0x1ABC));
    }

    @Test
    public void txrefDecode_bip_examples() {

        LocationData ld;

        // Genesis Coinbase Transaction (Transaction #0 of Block #0):
        ld = Txref.impl.txrefDecode("tx1:rqqq-qqqq-qmhu-qhp");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());


        // Transaction #2205 of Block #466793:
        ld = Txref.impl.txrefDecode("tx1:rjk0-uqay-zsrw-hqe");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(466793, ld.getBlockHeight());
        assertEquals(2205, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());


        // The following list gives properly encoded Bitcoin mainnet TxRef's
        ld = Txref.impl.txrefDecode("tx1:rqqq-qqqq-qmhu-qhp");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:rqqq-qqll-l8xh-jkg");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:r7ll-llqq-qghq-qr8");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:r7ll-llll-l5xt-jzw");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());


        // The following list gives properly encoded Bitcoin testnet TxRef's
        ld = Txref.impl.txrefDecode("txtest1:xqqq-qqqq-qkla-64l");
        assertEquals(Txref.MAGIC_BTC_TEST, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:xqqq-qqll-l2wk-g5k");
        assertEquals(Txref.MAGIC_BTC_TEST, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:x7ll-llqq-q9lp-6pe");
        assertEquals(Txref.MAGIC_BTC_TEST, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:x7ll-llll-lew2-gqs");
        assertEquals(Txref.MAGIC_BTC_TEST, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());


        // The following list gives valid (though strangley formatted) Bitcoin TxRef's
        ld = Txref.impl.txrefDecode("tx1:rjk0-uqay-zsrw-hqe");
        assertEquals(Txref.MAGIC_BTC_MAIN, ld.getMagicCode());
        assertEquals(0x71F69, ld.getBlockHeight());
        assertEquals(0x89D, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());


        // The following list gives properly encoded Bitcoin mainnet TxRef's with Outpoints
        ld = Txref.impl.txrefDecode("tx1:yqqq-qqqq-qqqq-ksvh-26");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:yqqq-qqll-lqqq-v0h2-2k");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:y7ll-llqq-qqqq-a5zy-tc");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:y7ll-llll-lqqq-8tee-t5");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());


        ld = Txref.impl.txrefDecode("tx1:yqqq-qqqq-qpqq-5j9q-nz");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:yqqq-qqll-lpqq-wd7a-nw");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:y7ll-llqq-qpqq-lktn-jq");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:y7ll-llll-lpqq-9fsw-jv");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());


        ld = Txref.impl.txrefDecode("tx1:yjk0-uqay-zrfq-g2cg-t8");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0x71F69, ld.getBlockHeight());
        assertEquals(0x89D, ld.getTransactionPosition());
        assertEquals(0x123, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("tx1:yjk0-uqay-zu4x-nk6u-pc");
        assertEquals(Txref.MAGIC_BTC_MAIN_EXTENDED, ld.getMagicCode());
        assertEquals(0x71F69, ld.getBlockHeight());
        assertEquals(0x89D, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());


        // The following list gives properly encoded Bitcoin testnet TxRef's with Outpoints
        ld = Txref.impl.txrefDecode("txtest1:8qqq-qqqq-qqqq-cgru-fa");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:8qqq-qqll-lqqq-zhcp-f3");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:87ll-llqq-qqqq-nvd0-gl");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:87ll-llll-lqqq-fnkj-gn");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(0, ld.getTxoIndex());


        ld = Txref.impl.txrefDecode("txtest1:8qqq-qqqq-qpqq-622t-s9");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:8qqq-qqll-lpqq-q43k-sf");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:87ll-llqq-qpqq-3wyc-38");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:87ll-llll-lpqq-t3l9-3t");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0xFFFFFF, ld.getBlockHeight());
        assertEquals(0x7FFF, ld.getTransactionPosition());
        assertEquals(1, ld.getTxoIndex());


        ld = Txref.impl.txrefDecode("txtest1:8jk0-uqay-zrfq-xjhr-gq");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0x71F69, ld.getBlockHeight());
        assertEquals(0x89D, ld.getTransactionPosition());
        assertEquals(0x123, ld.getTxoIndex());

        ld = Txref.impl.txrefDecode("txtest1:8jk0-uqay-zu4x-aw4h-zl");
        assertEquals(Txref.MAGIC_BTC_TEST_EXTENDED, ld.getMagicCode());
        assertEquals(0x71F69, ld.getBlockHeight());
        assertEquals(0x89D, ld.getTransactionPosition());
        assertEquals(0x1ABC, ld.getTxoIndex());
    }
}
