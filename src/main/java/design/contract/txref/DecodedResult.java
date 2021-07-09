package design.contract.txref;

import java.util.Objects;

public class DecodedResult {

    private String hrp;
    private String txref;
    private int blockHeight;
    private int transactionIndex;
    private int txoIndex;
    private int magicCode;
    private Encoding encoding;
    private String commentary;

    public DecodedResult(String hrp, String txref, int blockHeight, int transactionIndex, int txoIndex, int magicCode) {
        this.hrp = hrp;
        this.txref = txref;
        this.blockHeight = blockHeight;
        this.transactionIndex = transactionIndex;
        this.txoIndex = txoIndex;
        this.magicCode = magicCode;
        this.encoding = Encoding.INVALID;
        this.commentary = "";
    }

    public String getHrp() {
        return hrp;
    }

    public void setHrp(String hrp) {
        this.hrp = hrp;
    }

    public String getTxref() {
        return txref;
    }

    public void setTxref(String txref) {
        this.txref = txref;
    }

    public int getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(int blockHeight) {
        this.blockHeight = blockHeight;
    }

    public int getTransactionIndex() {
        return transactionIndex;
    }

    public void setTransactionIndex(int transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    public int getTxoIndex() {
        return txoIndex;
    }

    public void setTxoIndex(int txoIndex) {
        this.txoIndex = txoIndex;
    }

    public int getMagicCode() {
        return magicCode;
    }

    public void setMagicCode(int magicCode) {
        this.magicCode = magicCode;
    }

    public Encoding getEncoding() {
        return encoding;
    }

    public void setEncoding(Encoding encoding) {
        this.encoding = encoding;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DecodedResult that = (DecodedResult) o;
        return blockHeight == that.blockHeight &&
                transactionIndex == that.transactionIndex &&
                txoIndex == that.txoIndex &&
                magicCode == that.magicCode &&
                Objects.equals(hrp, that.hrp) &&
                Objects.equals(txref, that.txref) &&
                Objects.equals(commentary, that.commentary) &&
                encoding == that.encoding;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hrp, txref, blockHeight, transactionIndex, txoIndex, magicCode, encoding, commentary);
    }

    public enum Encoding {
        INVALID, // no or invalid encoding was detected
        BECH32,  // encoding used original checksum constant (1)
        BECH32M; // encoding used default checksum constant (M = 0x2bc830a3)
    }
}
