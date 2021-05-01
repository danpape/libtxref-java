package design.contract.txref;

import design.contract.bech32.Bech32;

import java.util.Arrays;

public class Txref {

    public static final class Limits {
        public static final int TXREF_STRING_MIN_LENGTH = 18;                    // ex: "tx1rqqqqqqqqwtvvjr"
        public static final int TXREF_STRING_NO_HRP_MIN_LENGTH = 15;             // ex: "rqqqqqqqqwtvvjr"

        public static final int TXREF_EXT_STRING_MIN_LENGTH = 21;                // ex: "tx1y29umqjxppqqsfp2tt"
        public static final int TXREF_EXT_STRING_NO_HRP_MIN_LENGTH = 18;         // ex: "y29umqjxppqqsfp2tt"

        public static final int TXREF_STRING_MIN_LENGTH_TESTNET = 22;            // ex: "txtest1xqqqqqqqqrrdksa"

        public static final int TXREF_EXT_STRING_MIN_LENGTH_TESTNET = 25;        // ex: "txtest1829umqjxppqq73wpgv"

        public static final int MAX_BLOCK_HEIGHT         = 0xFFFFFF; // 16777215

        public static final int MAX_TRANSACTION_POSITION = 0x7FFF;   // 32767

        public static final int MAX_TXO_INDEX            = 0x7FFF;   // 32767

        public static final int MAX_MAGIC_CODE           = 0x1F;

        public static final int DATA_SIZE                = 9;

        public static final int DATA_EXTENDED_SIZE       = 12;

        private Limits() {
            throw new IllegalStateException("should not instantiate");
        }
    }

    // bech32 "human readable part"s
    static final String BECH32_HRP_MAIN = "tx";
    static final String BECH32_HRP_TEST = "txtest";

    // magic codes used for chain identification and namespacing
    static final char MAGIC_BTC_MAIN = 0x3;
    static final char MAGIC_BTC_MAIN_EXTENDED = 0x4;
    static final char MAGIC_BTC_TEST = 0x6;
    static final char MAGIC_BTC_TEST_EXTENDED = 0x7;

    // characters used when pretty-printing
    static final char COLON = ':';
    static final char HYPHEN = '-';


    enum InputParam {UNKNOWN, ADDRESS, TXID, TXREF, TXREFEXT}


    public static final class Impl {

        static boolean isStandardSize(long dataSize) {
            return dataSize == Limits.DATA_SIZE;
        }

        static boolean isExtendedSize(long dataSize) {
            return dataSize == Limits.DATA_EXTENDED_SIZE;
        }

        static boolean isDataSizeValid(long dataSize) {
            return isStandardSize(dataSize) || isExtendedSize(dataSize);
        }

        // is a txref string, missing the HRP, still of a valid length for a txref?
        static boolean isLengthValid(long length) {
            return length == Limits.TXREF_STRING_NO_HRP_MIN_LENGTH ||
                    length == Limits.TXREF_EXT_STRING_NO_HRP_MIN_LENGTH;
        }

        // a block's height can only be in a certain range
        static void checkBlockHeightRange(int blockHeight) {
            if(blockHeight < 0 || blockHeight > Limits.MAX_BLOCK_HEIGHT)
                throw new IllegalArgumentException("block height is too large");
        }

        // a transaction's position can only be in a certain range
        static void checkTransactionPositionRange(int transactionPosition) {
            if(transactionPosition < 0 || transactionPosition > Limits.MAX_TRANSACTION_POSITION)
                throw new IllegalArgumentException("transaction position is too large");
        }

        // a TXO's index can only be in a certain range
        static void checkTxoIndexRange(int txoIndex) {
            if(txoIndex < 0 || txoIndex > Limits.MAX_TXO_INDEX)
                throw new IllegalArgumentException("txo index is too large");
        }

        // the magic code can only be in a certain range
        static void checkMagicCodeRange(int magicCode) {
            if(magicCode < 0 || magicCode > Limits.MAX_MAGIC_CODE)
                throw new IllegalArgumentException("magic code is too large");
        }

        // check that the magic code is for one of the extended txrefs
        static void checkExtendedMagicCode(int magicCode) {
            if(magicCode != MAGIC_BTC_MAIN_EXTENDED && magicCode != MAGIC_BTC_TEST_EXTENDED)
                throw new IllegalArgumentException("magic code does not support extended txrefs");
        }

        // separate groups of chars in the txref string to make it look nicer
        static String addGroupSeparators(
                String raw,
                int hrplen) {
            return addGroupSeparators(raw, hrplen, 4);
        }

        static String addGroupSeparators(
                String raw,
                int hrplen,
                int separatorOffset) {

            if(hrplen > Bech32.Limits.MAX_HRP_LENGTH) {
                throw new IllegalArgumentException("HRP must be less than 84 characters long");
            }

            if(separatorOffset < 1) {
                throw new IllegalArgumentException("separatorOffset must be > 0");
            }

            if(raw.length() < 2)
                throw new IllegalArgumentException("Can't add separator characters to strings with length < 2");

            if(raw.length() == hrplen) // no separators needed
                return raw;

            if(raw.length() < hrplen)
                throw new IllegalArgumentException("HRP length can't be greater than input length");

            // number of separators that will be inserted
            int numSeparators = (raw.length() - hrplen - 1) / separatorOffset;

            // output length
            int outputLength = raw.length() + numSeparators;

            // create output string, starting with all hyphens
            char[] output = new char[outputLength];
            Arrays.fill(output, HYPHEN);

            // copy over the raw string, skipping every offset # chars, after the HRP
            int rawPos = 0;
            int outputPos = 0;
            for(char c : raw.toCharArray()) {

                output[outputPos++] = c;

                ++rawPos;
                if(rawPos > hrplen && (rawPos - hrplen) % separatorOffset == 0)
                    ++outputPos;
            }

            return new String(output);
        }

        // pretty print a txref returned by bech32::encode()
        static String prettyPrint(String plain, int hrplen) {

            // add colon after the HRP and bech32 separator character, then add dashes
            // every 4 characters after that
            int hrpPlusSeparatorLength = hrplen + 1;         // 1 for '1'
            int hrpPlusSeparatorAndColonLength = hrplen + 2; // 2 for '1' and ':'
            return addGroupSeparators(
                    plain.substring(0, hrpPlusSeparatorLength) + COLON + plain.substring(hrpPlusSeparatorLength),
                    hrpPlusSeparatorAndColonLength);
        }

        // extract the magic code from the decoded data part
        static char extractMagicCode(char[] dp) {
            return dp[0];
        }

        // extract the version from the decoded data part
        static char extractVersion(char[] dp) {
            return (char)(dp[1] & 0x1);
        }

        // extract the block height from the decoded data part
        static int extractBlockHeight(char[] dp) {
            char version = extractVersion(dp);

            if(version == 0) {
                int blockHeight = (dp[1] >> 1);
                blockHeight |= (dp[2] << 4);
                blockHeight |= (dp[3] << 9);
                blockHeight |= (dp[4] << 14);
                blockHeight |= (dp[5] << 19);
                return blockHeight;
            }
            else {
                throw new IllegalArgumentException("Unknown txref version detected: " + version);
            }
        }

        // extract the transaction position from the decoded data part
        static int extractTransactionPosition(char[] dp) {
            char version = extractVersion(dp);

            if(version == 0) {
                int transactionPosition = dp[6];
                transactionPosition |= (dp[7] << 5);
                transactionPosition |= (dp[8] << 10);
                return transactionPosition;
            }
            else {
                throw new IllegalArgumentException("Unknown txref version detected: " + version);
            }
        }

        // extract the TXO index from the decoded data part
        static int extractTxoIndex(char[] dp) {
            if(dp.length < 12) {
                // non-extended txrefs don't store the txoIndex, so just return 0
                return 0;
            }

            char version = extractVersion(dp);

            if(version == 0) {
                int txoIndex = dp[9];
                txoIndex |= (dp[10] << 5);
                txoIndex |= (dp[11] << 10);
                return txoIndex;
            }
            else {
                throw new IllegalArgumentException("Unknown txref version detected: " + version);
            }
        }

        // some txref strings may have had the HRP stripped off. Attempt to prepend one if needed.
        // assumes that stripUnknownChars() has already been called
        static String addHrpIfNeeded(final String txref) {
            if(isLengthValid(txref.length()) && (txref.charAt(0) == 'r' || txref.charAt(0) == 'y')) {
                return Txref.BECH32_HRP_MAIN + Bech32.SEPARATOR + txref;
            }
            if(isLengthValid(txref.length()) && (txref.charAt(0) == 'x' || txref.charAt(0) == '8')) {
                return Txref.BECH32_HRP_TEST + Bech32.SEPARATOR + txref;
            }
            return txref;
        }

        static String txrefEncode(
            final String hrp,
            int magicCode,
            int blockHeight,
            int transactionPosition) {

            checkBlockHeightRange(blockHeight);
            checkTransactionPositionRange(transactionPosition);
            checkMagicCodeRange(magicCode);

            char[] dp = new char[Limits.DATA_SIZE];

            // set the magic code
            dp[0] = (char) magicCode;                        // sets 1-3 bits in the 1st 5 bits

            // set version bit to 0
            dp[1] &= ~(1 << 0);                              // sets 1 bit in 2nd 5 bits

            // set block height
            dp[1] |= (blockHeight & 0xF) << 1;               // sets 4 bits in 2nd 5 bits
            dp[2] |= (blockHeight & 0x1F0) >> 4;             // sets 5 bits in 3rd 5 bits
            dp[3] |= (blockHeight & 0x3E00) >> 9;            // sets 5 bits in 4th 5 bits
            dp[4] |= (blockHeight & 0x7C000) >> 14;          // sets 5 bits in 5th 5 bits
            dp[5] |= (blockHeight & 0xF80000) >> 19;         // sets 5 bits in 6th 5 bits (24 bits total for blockHeight)

            // set transaction position
            dp[6] |= (transactionPosition & 0x1F);           // sets 5 bits in 7th 5 bits
            dp[7] |= (transactionPosition & 0x3E0) >> 5;     // sets 5 bits in 8th 5 bits
            dp[8] |= (transactionPosition & 0x7C00) >> 10;   // sets 5 bits in 9th 5 bits (15 bits total for transactionPosition)

            // Bech32 encode
            String result = Bech32.encode(hrp, dp);

            // add the dashes
            return prettyPrint(result, hrp.length());
        }

        static String txrefExtEncode(
            final String hrp,
            int magicCode,
            int blockHeight,
            int transactionPosition,
            int txoIndex) {

            checkBlockHeightRange(blockHeight);
            checkTransactionPositionRange(transactionPosition);
            checkTxoIndexRange(txoIndex);
            checkMagicCodeRange(magicCode);
            checkExtendedMagicCode(magicCode);

            char[] dp = new char[Limits.DATA_EXTENDED_SIZE];

            // set the magic code
            dp[0] = (char) magicCode;                      // sets 1-3 bits in the 1st 5 bits

            // set version bit to 0
            dp[1] &= ~(1 << 0);                            // sets 1 bit in 2nd 5 bits

            // set block height
            dp[1] |= (blockHeight & 0xF) << 1;             // sets 4 bits in 3rd 5 bits
            dp[2] |= (blockHeight & 0x1F0) >> 4;           // sets 5 bits in 4th 5 bits
            dp[3] |= (blockHeight & 0x3E00) >> 9;          // sets 5 bits in 5th 5 bits
            dp[4] |= (blockHeight & 0x7C000) >> 14;        // sets 5 bits in 6th 5 bits
            dp[5] |= (blockHeight & 0xF80000) >> 19;       // sets 5 bits in 7th 5 bits (24 bits total for blockHeight)

            // set transaction position
            dp[6] |= (transactionPosition & 0x1F);         // sets 5 bits in 8th 5 bits
            dp[7] |= (transactionPosition & 0x3E0) >> 5;   // sets 5 bits in 9th 5 bits
            dp[8] |= (transactionPosition & 0x7C00) >> 10; // sets 5 bits in 10th 5 bits (15 bits total for transactionPosition)

            // set txo index
            dp[9] |= txoIndex & 0x1F;                      // sets 5 bits in 11th 5 bits
            dp[10] |= (txoIndex & 0x3E0) >> 5;             // sets 5 bits in 12th 5 bits
            dp[11] |= (txoIndex & 0x7C00) >> 10;           // sets 5 bits in 13th 5 bits (15 bits total for txoIndex)

            // Bech32 encode
            String result = Bech32.encode(hrp, dp);

            // add the dashes
            return prettyPrint(result, hrp.length());
        }

        static DecodedResult txrefDecode(String txref) {
            String txrefClean = Bech32.stripUnknownChars(txref);
            txrefClean = txrefClean.toLowerCase();
            txrefClean = Impl.addHrpIfNeeded(txrefClean);
            design.contract.bech32.DecodedResult bech32DecodedResult = Bech32.decode(txrefClean);

            if(bech32DecodedResult.getHrp() == null) {
                throw new IllegalArgumentException("decoding failed");
            }

            int dataSize = bech32DecodedResult.getDp().length;
            if(!Impl.isDataSizeValid(dataSize)) {
                throw new IllegalArgumentException("decoded dp size is incorrect");
            }

            DecodedResult result = new DecodedResult(
                    bech32DecodedResult.getHrp(),
                    Impl.prettyPrint(txrefClean, bech32DecodedResult.getHrp().length()),
                    Impl.extractBlockHeight(bech32DecodedResult.getDp()),
                    Impl.extractTransactionPosition(bech32DecodedResult.getDp()),
                    Impl.extractTxoIndex(bech32DecodedResult.getDp()),
                    Impl.extractMagicCode(bech32DecodedResult.getDp()));

            if(bech32DecodedResult.getEncoding() == design.contract.bech32.DecodedResult.Encoding.BECH32M) {
                result.setEncoding(DecodedResult.Encoding.BECH32M);
            }
            else if(bech32DecodedResult.getEncoding() == design.contract.bech32.DecodedResult.Encoding.BECH32) {
                result.setEncoding(DecodedResult.Encoding.BECH32);
                String updatedTxref;
                if(result.getMagicCode() == MAGIC_BTC_MAIN_EXTENDED || result.getMagicCode() == MAGIC_BTC_TEST_EXTENDED) {
                    updatedTxref = txrefExtEncode(result.getHrp(), result.getMagicCode(), result.getBlockHeight(), result.getTransactionPosition(), result.getTxoIndex());
                }
                else {
                    updatedTxref = txrefEncode(result.getHrp(), result.getMagicCode(), result.getBlockHeight(), result.getTransactionPosition());
                }
                result.setCommentary("The txref " + result.getTxref() +
                        " uses an old encoding scheme and should be updated to " + updatedTxref +
                        " See https://github.com/dcdpr/libtxref-java#regarding-bech32-checksums for more information.");
            }

            return result;
        }

        static InputParam classifyInputStringBase(final String str) {

            // before testing for various txrefs, get rid of any unknown
            // characters, ex: dashes, periods
            String s = Bech32.stripUnknownChars(str);

            if(s.length() == Limits.TXREF_STRING_MIN_LENGTH || s.length() == Limits.TXREF_STRING_MIN_LENGTH_TESTNET )
                return InputParam.TXREF;

            if(s.length() == Limits.TXREF_EXT_STRING_MIN_LENGTH || s.length() == Limits.TXREF_EXT_STRING_MIN_LENGTH_TESTNET)
                return InputParam.TXREFEXT;

            return InputParam.UNKNOWN;
        }

        static InputParam classifyInputStringMissingHRP(final String str) {

            // before testing for various txrefs, get rid of any unknown
            // characters, ex: dashes, periods
            String s = Bech32.stripUnknownChars(str);

            if(s.length() == Limits.TXREF_STRING_NO_HRP_MIN_LENGTH)
                return InputParam.TXREF;

            if(s.length() == Limits.TXREF_EXT_STRING_NO_HRP_MIN_LENGTH)
                return InputParam.TXREFEXT;

            return InputParam.UNKNOWN;
        }

        private Impl() {
            throw new IllegalStateException("should not instantiate");
        }
    }

    // encodes the position of a confirmed bitcoin transaction on the
    // mainnet network and returns a bech32 encoded "transaction
    // position reference" (txref). If txoIndex is greater than 0, then
    // an extended reference is returned (txref-ext). If txoIndex is zero,
    // but forceExtended=true, then an extended reference is returned
    // (txref-ext).
    public static String encode(
            int blockHeight,
            int transactionPosition,
            int txoIndex,
            boolean forceExtended,
            final String hrp) {

        if(txoIndex == 0 && !forceExtended)
            return Impl.txrefEncode(hrp, MAGIC_BTC_MAIN, blockHeight, transactionPosition);

        return Impl.txrefExtEncode(hrp, MAGIC_BTC_MAIN_EXTENDED, blockHeight, transactionPosition, txoIndex);

    }

    public static String encode(
            int blockHeight,
            int transactionPosition,
            int txoIndex,
            boolean forceExtended) {

        if(txoIndex == 0 && !forceExtended)
            return Impl.txrefEncode(Txref.BECH32_HRP_MAIN, MAGIC_BTC_MAIN, blockHeight, transactionPosition);

        return Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, MAGIC_BTC_MAIN_EXTENDED, blockHeight, transactionPosition, txoIndex);

    }

    public static String encode(
            int blockHeight,
            int transactionPosition,
            int txoIndex) {

        if(txoIndex == 0)
            return Impl.txrefEncode(Txref.BECH32_HRP_MAIN, MAGIC_BTC_MAIN, blockHeight, transactionPosition);

        return Impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, MAGIC_BTC_MAIN_EXTENDED, blockHeight, transactionPosition, txoIndex);

    }

    public static String encode(
            int blockHeight,
            int transactionPosition) {

        return Impl.txrefEncode(Txref.BECH32_HRP_MAIN, MAGIC_BTC_MAIN, blockHeight, transactionPosition);
    }

    // encodes the position of a confirmed bitcoin transaction on the
    // testnet network and returns a bech32 encoded "transaction
    // position reference" (txref). If txoIndex is greater than 0, then
    // an extended reference is returned (txref-ext). If txoIndex is zero,
    // but forceExtended=true, then an extended reference is returned
    // (txref-ext).
    public static String encodeTestnet(
            int blockHeight,
            int transactionPosition,
            int txoIndex,
            boolean forceExtended,
            String hrp) {

        if(txoIndex == 0 && !forceExtended)
            return Impl.txrefEncode(hrp, MAGIC_BTC_TEST, blockHeight, transactionPosition);

        return Impl.txrefExtEncode(hrp, MAGIC_BTC_TEST_EXTENDED, blockHeight, transactionPosition, txoIndex);

    }

    public static String encodeTestnet(
            int blockHeight,
            int transactionPosition,
            int txoIndex,
            boolean forceExtended) {

        if(txoIndex == 0 && !forceExtended)
            return Impl.txrefEncode(Txref.BECH32_HRP_TEST, MAGIC_BTC_TEST, blockHeight, transactionPosition);

        return Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, MAGIC_BTC_TEST_EXTENDED, blockHeight, transactionPosition, txoIndex);

    }

    public static String encodeTestnet(
            int blockHeight,
            int transactionPosition,
            int txoIndex) {

        if(txoIndex == 0)
            return Impl.txrefEncode(Txref.BECH32_HRP_TEST, MAGIC_BTC_TEST, blockHeight, transactionPosition);

        return Impl.txrefExtEncode(Txref.BECH32_HRP_TEST, MAGIC_BTC_TEST_EXTENDED, blockHeight, transactionPosition, txoIndex);

    }

    public static String encodeTestnet(
            int blockHeight,
            int transactionPosition) {

        return Impl.txrefEncode(Txref.BECH32_HRP_TEST, MAGIC_BTC_TEST, blockHeight, transactionPosition);
    }

    // decodes a bech32 encoded "transaction position reference" (txref) and
    // returns identifying data
    public static DecodedResult decode(String txref) {
        return Impl.txrefDecode(txref);
    }

    // determine if the input string is a Bitcoin address, txid, txref, or
    // txrefext_param. This is not meant to be an exhaustive test--should only be
    // used as a first pass to see what sort of string might be passed in as input.
    public static InputParam classifyInputString(final String str) {

        if(str.isEmpty())
            return InputParam.UNKNOWN;

        // if exactly 64 chars in length, it is likely a transaction id
        if(str.length() == 64)
            return InputParam.TXID;

        // if it starts with certain chars, and is of a certain length, it may be a bitcoin address
        char c0 = str.charAt(0);
        if(c0 == '1' || c0 == '3' || c0 == 'm' || c0 == 'n' || c0 == '2')
            if(str.length() >= 26 && str.length() < 36)
                return InputParam.ADDRESS;

        // check if it could be a standard txref or txrefext
        InputParam baseResult = Impl.classifyInputStringBase(str);

        // check if it could be a truncated txref or txrefext (missing the HRP)
        InputParam missingResult = Impl.classifyInputStringMissingHRP(str);

        // if one result is 'unknown' and the other isn't, then return the good one
        if(baseResult != InputParam.UNKNOWN && missingResult == InputParam.UNKNOWN)
            return baseResult;
        if(baseResult == InputParam.UNKNOWN && missingResult != InputParam.UNKNOWN)
            return missingResult;

        // special case: if baseResult is 'txref_param' and missingResult is 'txrefext_param' then
        // we need to dig deeper as TXREF_STRING_MIN_LENGTH == TXREF_EXT_STRING_NO_HRP_MIN_LENGTH
        if (baseResult == InputParam.TXREF && missingResult == InputParam.TXREFEXT) {
            if (str.charAt(0) == Txref.BECH32_HRP_MAIN.charAt(0) &&     // 't'
                    str.charAt(1) == Txref.BECH32_HRP_MAIN.charAt(1) && // 'x'
                    str.charAt(2) == Bech32.SEPARATOR)                  // '1'
                return InputParam.TXREF;
            else
                return InputParam.TXREFEXT;
        }

        // otherwise, just return
        assert(baseResult == InputParam.UNKNOWN);
        return baseResult;
    }

}
