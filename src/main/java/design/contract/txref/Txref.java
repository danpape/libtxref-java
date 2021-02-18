package design.contract.txref;

import design.contract.bech32.Bech32;

import java.util.Arrays;

public interface Txref {

    interface limits {
        int TXREF_STRING_MIN_LENGTH = 18;                    // ex: "tx1rqqqqqqqqmhuqhp"
        int TXREF_STRING_NO_HRP_MIN_LENGTH = 15;             // ex: "rqqqqqqqqmhuqhp"

        int TXREF_EXT_STRING_MIN_LENGTH = 21;                // ex: "tx1yqqqqqqqqqqqksvh26"
        int TXREF_EXT_STRING_NO_HRP_MIN_LENGTH = 18;         // ex: "yqqqqqqqqqqqksvh26"

        int TXREF_STRING_MIN_LENGTH_TESTNET = 22;            // ex: "txtest1rqqqqqqqqmhuqhp"

        int TXREF_EXT_STRING_MIN_LENGTH_TESTNET = 25;        // ex: "txtest18jk0uqayzu4xaw4hzl"

        int TXREF_EXTRA_PRETTY_PRINT_CHARS = 4;              // ex: "tx1:rqqq-qqqq-qmhu-qhp"
        int TXREF_EXT_EXTRA_PRETTY_PRINT_CHARS = 5;          // ex: "tx1:yqqq-qqqq-qqqq-ksvh-26"

        int TXREF_MAX_LENGTH =
                TXREF_EXT_STRING_MIN_LENGTH_TESTNET + TXREF_EXT_EXTRA_PRETTY_PRINT_CHARS;

        int MAX_BLOCK_HEIGHT         = 0xFFFFFF; // 16777215

        int MAX_TRANSACTION_POSITION = 0x7FFF;   // 32767

        int MAX_TXO_INDEX            = 0x7FFF;   // 32767

        int MAX_MAGIC_CODE           = 0x1F;

        int DATA_SIZE                = 9;

        int DATA_EXTENDED_SIZE       = 12;

    }

    // bech32 "human readable part"s
    String BECH32_HRP_MAIN = "tx";
    String BECH32_HRP_TEST = "txtest";

    // magic codes used for chain identification and namespacing
    char MAGIC_BTC_MAIN = 0x3;
    char MAGIC_BTC_MAIN_EXTENDED = 0x4;
    char MAGIC_BTC_TEST = 0x6;
    char MAGIC_BTC_TEST_EXTENDED = 0x7;

    // characters used when pretty-printing
    char colon = ':';
    char hyphen = '-';


    enum InputParam { unknown_param, address_param, txid_param, txref_param, txrefext_param }


    interface impl {

        static boolean isStandardSize(long dataSize) {
            return dataSize == limits.DATA_SIZE;
        }

        static boolean isExtendedSize(long dataSize) {
            return dataSize == limits.DATA_EXTENDED_SIZE;
        }

        static boolean isDataSizeValid(long dataSize) {
            return isStandardSize(dataSize) || isExtendedSize(dataSize);
        }

        // is a txref string, missing the HRP, still of a valid length for a txref?
        static boolean isLengthValid(long length) {
            return length == limits.TXREF_STRING_NO_HRP_MIN_LENGTH ||
                    length == limits.TXREF_EXT_STRING_NO_HRP_MIN_LENGTH;
        }

        // a block's height can only be in a certain range
        static void checkBlockHeightRange(int blockHeight) {
            if(blockHeight < 0 || blockHeight > limits.MAX_BLOCK_HEIGHT)
                throw new RuntimeException("block height is too large");
        }

        // a transaction's position can only be in a certain range
        static void checkTransactionPositionRange(int transactionPosition) {
            if(transactionPosition < 0 || transactionPosition > limits.MAX_TRANSACTION_POSITION)
                throw new RuntimeException("transaction position is too large");
        }

        // a TXO's index can only be in a certain range
        static void checkTxoIndexRange(int txoIndex) {
            if(txoIndex < 0 || txoIndex > limits.MAX_TXO_INDEX)
                throw new RuntimeException("txo index is too large");
        }

        // the magic code can only be in a certain range
        static void checkMagicCodeRange(int magicCode) {
            if(magicCode < 0 || magicCode > limits.MAX_MAGIC_CODE)
                throw new RuntimeException("magic code is too large");
        }

        // check that the magic code is for one of the extended txrefs
        static void checkExtendedMagicCode(int magicCode) {
            if(magicCode != MAGIC_BTC_MAIN_EXTENDED && magicCode != MAGIC_BTC_TEST_EXTENDED)
                throw new RuntimeException("magic code does not support extended txrefs");
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
                throw new RuntimeException("HRP must be less than 84 characters long");
            }

            if(separatorOffset < 1) {
                throw new RuntimeException("separatorOffset must be > 0");
            }

            if(raw.length() < 2)
                throw new RuntimeException("Can't add separator characters to strings with length < 2");

            if(raw.length() == hrplen) // no separators needed
                return raw;

            if(raw.length() < hrplen)
                throw new RuntimeException("HRP length can't be greater than input length");

            // number of separators that will be inserted
            int numSeparators = (raw.length() - hrplen - 1) / separatorOffset;

            // output length
            int outputLength = raw.length() + numSeparators;

            // create output string, starting with all hyphens
            char[] output = new char[outputLength];
            Arrays.fill(output, hyphen);

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
                    plain.substring(0, hrpPlusSeparatorLength) + colon + plain.substring(hrpPlusSeparatorLength),
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
                throw new RuntimeException("Unknown txref version detected: " + version);
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
                throw new RuntimeException("Unknown txref version detected: " + version);
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
                throw new RuntimeException("Unknown txref version detected: " + version);
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

            char[] dp = new char[limits.DATA_SIZE];

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

            char[] dp = new char[limits.DATA_EXTENDED_SIZE];

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

        static LocationData txrefDecode(String txref) {
            String txrefClean = Bech32.stripUnknownChars(txref);
            txrefClean = impl.addHrpIfNeeded(txrefClean);
            design.contract.bech32.DecodedResult hd = Bech32.decode(txrefClean);

            if(hd.getHrp() == null) {
                throw new RuntimeException("decoding failed");
            }

            int dataSize = hd.getDp().length;
            if(!impl.isDataSizeValid(dataSize)) {
                throw new RuntimeException("decoded dp size is incorrect");
            }

            LocationData result = new LocationData(
                    hd.getHrp(),
                    impl.prettyPrint(txrefClean, hd.getHrp().length()),
                    impl.extractBlockHeight(hd.getDp()),
                    impl.extractTransactionPosition(hd.getDp()),
                    impl.extractTxoIndex(hd.getDp()),
                    impl.extractMagicCode(hd.getDp()));

            if(hd.getEncoding() == design.contract.bech32.DecodedResult.Encoding.BECH32M) {
                result.setEncoding(LocationData.Encoding.BECH32M);
            }
            else if(hd.getEncoding() == design.contract.bech32.DecodedResult.Encoding.BECH32) {
                result.setEncoding(LocationData.Encoding.BECH32);
            }

            return result;
        }

        static InputParam classifyInputStringBase(final String str) {

            // before testing for various txrefs, get rid of any unknown
            // characters, ex: dashes, periods
            String s = Bech32.stripUnknownChars(str);

            if(s.length() == limits.TXREF_STRING_MIN_LENGTH || s.length() == limits.TXREF_STRING_MIN_LENGTH_TESTNET )
                return InputParam.txref_param;

            if(s.length() == limits.TXREF_EXT_STRING_MIN_LENGTH || s.length() == limits.TXREF_EXT_STRING_MIN_LENGTH_TESTNET)
                return InputParam.txrefext_param;

            return InputParam.unknown_param;
        }

        static InputParam classifyInputStringMissingHRP(final String str) {

            // before testing for various txrefs, get rid of any unknown
            // characters, ex: dashes, periods
            String s = Bech32.stripUnknownChars(str);

            if(s.length() == limits.TXREF_STRING_NO_HRP_MIN_LENGTH)
                return InputParam.txref_param;

            if(s.length() == limits.TXREF_EXT_STRING_NO_HRP_MIN_LENGTH)
                return InputParam.txrefext_param;

            return InputParam.unknown_param;
        }


    }

    // encodes the position of a confirmed bitcoin transaction on the
    // mainnet network and returns a bech32 encoded "transaction
    // position reference" (txref). If txoIndex is greater than 0, then
    // an extended reference is returned (txref-ext). If txoIndex is zero,
    // but forceExtended=true, then an extended reference is returned
    // (txref-ext).
    static String encode(
            int blockHeight,
            int transactionPosition,
            int txoIndex,
            boolean forceExtended,
            final String hrp) {

        if(txoIndex == 0 && !forceExtended)
            return impl.txrefEncode(hrp, MAGIC_BTC_MAIN, blockHeight, transactionPosition);

        return impl.txrefExtEncode(hrp, MAGIC_BTC_MAIN_EXTENDED, blockHeight, transactionPosition, txoIndex);

    }

    static String encode(
            int blockHeight,
            int transactionPosition,
            int txoIndex,
            boolean forceExtended) {

        if(txoIndex == 0 && !forceExtended)
            return impl.txrefEncode(Txref.BECH32_HRP_MAIN, MAGIC_BTC_MAIN, blockHeight, transactionPosition);

        return impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, MAGIC_BTC_MAIN_EXTENDED, blockHeight, transactionPosition, txoIndex);

    }

    static String encode(
            int blockHeight,
            int transactionPosition,
            int txoIndex) {

        if(txoIndex == 0)
            return impl.txrefEncode(Txref.BECH32_HRP_MAIN, MAGIC_BTC_MAIN, blockHeight, transactionPosition);

        return impl.txrefExtEncode(Txref.BECH32_HRP_MAIN, MAGIC_BTC_MAIN_EXTENDED, blockHeight, transactionPosition, txoIndex);

    }

    static String encode(
            int blockHeight,
            int transactionPosition) {

        return impl.txrefEncode(Txref.BECH32_HRP_MAIN, MAGIC_BTC_MAIN, blockHeight, transactionPosition);
    }

    // encodes the position of a confirmed bitcoin transaction on the
    // testnet network and returns a bech32 encoded "transaction
    // position reference" (txref). If txoIndex is greater than 0, then
    // an extended reference is returned (txref-ext). If txoIndex is zero,
    // but forceExtended=true, then an extended reference is returned
    // (txref-ext).
    static String encodeTestnet(
            int blockHeight,
            int transactionPosition,
            int txoIndex,
            boolean forceExtended,
            String hrp) {

        if(txoIndex == 0 && !forceExtended)
            return impl.txrefEncode(hrp, MAGIC_BTC_TEST, blockHeight, transactionPosition);

        return impl.txrefExtEncode(hrp, MAGIC_BTC_TEST_EXTENDED, blockHeight, transactionPosition, txoIndex);

    }

    static String encodeTestnet(
            int blockHeight,
            int transactionPosition,
            int txoIndex,
            boolean forceExtended) {

        if(txoIndex == 0 && !forceExtended)
            return impl.txrefEncode(Txref.BECH32_HRP_TEST, MAGIC_BTC_TEST, blockHeight, transactionPosition);

        return impl.txrefExtEncode(Txref.BECH32_HRP_TEST, MAGIC_BTC_TEST_EXTENDED, blockHeight, transactionPosition, txoIndex);

    }

    static String encodeTestnet(
            int blockHeight,
            int transactionPosition,
            int txoIndex) {

        if(txoIndex == 0)
            return impl.txrefEncode(Txref.BECH32_HRP_TEST, MAGIC_BTC_TEST, blockHeight, transactionPosition);

        return impl.txrefExtEncode(Txref.BECH32_HRP_TEST, MAGIC_BTC_TEST_EXTENDED, blockHeight, transactionPosition, txoIndex);

    }

    static String encodeTestnet(
            int blockHeight,
            int transactionPosition) {

        return impl.txrefEncode(Txref.BECH32_HRP_TEST, MAGIC_BTC_TEST, blockHeight, transactionPosition);
    }

    // decodes a bech32 encoded "transaction position reference" (txref) and
    // returns identifying data
    static LocationData decode(String txref) {
        return impl.txrefDecode(txref);
    }

    // determine if the input string is a Bitcoin address, txid, txref, or
    // txrefext_param. This is not meant to be an exhaustive test--should only be
    // used as a first pass to see what sort of string might be passed in as input.
    static InputParam classifyInputString(final String str) {

        if(str.isEmpty())
            return InputParam.unknown_param;

        // if exactly 64 chars in length, it is likely a transaction id
        if(str.length() == 64)
            return InputParam.txid_param;

        // if it starts with certain chars, and is of a certain length, it may be a bitcoin address
        char c0 = str.charAt(0);
        if(c0 == '1' || c0 == '3' || c0 == 'm' || c0 == 'n' || c0 == '2')
            if(str.length() >= 26 && str.length() < 36)
                return InputParam.address_param;

        // check if it could be a standard txref or txrefext
        InputParam baseResult = impl.classifyInputStringBase(str);

        // check if it could be a truncated txref or txrefext (missing the HRP)
        InputParam missingResult = impl.classifyInputStringMissingHRP(str);

        // if one result is 'unknown' and the other isn't, then return the good one
        if(baseResult != InputParam.unknown_param && missingResult == InputParam.unknown_param)
            return baseResult;
        if(baseResult == InputParam.unknown_param && missingResult != InputParam.unknown_param)
            return missingResult;

        // special case: if baseResult is 'txref_param' and missingResult is 'txrefext_param' then
        // we need to dig deeper as TXREF_STRING_MIN_LENGTH == TXREF_EXT_STRING_NO_HRP_MIN_LENGTH
        if (baseResult == InputParam.txref_param && missingResult == InputParam.txrefext_param) {
            if (str.charAt(0) == Txref.BECH32_HRP_MAIN.charAt(0) &&     // 't'
                    str.charAt(1) == Txref.BECH32_HRP_MAIN.charAt(1) && // 'x'
                    str.charAt(2) == Bech32.SEPARATOR)                  // '1'
                return InputParam.txref_param;
            else
                return InputParam.txrefext_param;
        }

        // otherwise, just return
        assert(baseResult == InputParam.unknown_param);
        return baseResult;
    }



}
