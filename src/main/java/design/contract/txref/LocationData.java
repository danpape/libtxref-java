package design.contract.txref;

import java.util.Objects;

public class LocationData {

    private String hrp;
    private String txref;
    private int blockHeight;
    private int transactionPosition;
    private int txoIndex;
    private int magicCode;
    private Encoding encoding;

    public LocationData(String hrp, String txref, int blockHeight, int transactionPosition, int txoIndex, int magicCode) {
        this.hrp = hrp;
        this.txref = txref;
        this.blockHeight = blockHeight;
        this.transactionPosition = transactionPosition;
        this.txoIndex = txoIndex;
        this.magicCode = magicCode;
        this.encoding = Encoding.INVALID;
    }

    public LocationData(String hrp, String txref, int blockHeight, int transactionPosition, int txoIndex, int magicCode, Encoding encoding) {
        this.hrp = hrp;
        this.txref = txref;
        this.blockHeight = blockHeight;
        this.transactionPosition = transactionPosition;
        this.txoIndex = txoIndex;
        this.magicCode = magicCode;
        this.encoding = encoding;
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

    public int getTransactionPosition() {
        return transactionPosition;
    }

    public void setTransactionPosition(int transactionPosition) {
        this.transactionPosition = transactionPosition;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LocationData that = (LocationData) o;
        return blockHeight == that.blockHeight &&
                transactionPosition == that.transactionPosition &&
                txoIndex == that.txoIndex &&
                magicCode == that.magicCode &&
                Objects.equals(hrp, that.hrp) &&
                Objects.equals(txref, that.txref) &&
                encoding == that.encoding;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hrp, txref, blockHeight, transactionPosition, txoIndex, magicCode, encoding);
    }

    public enum Encoding {
        INVALID, // no or invalid encoding was detected
        BECH32,  // encoding used original checksum constant (1)
        BECH32M; // encoding used default checksum constant (M = 0x2bc830a3)
    }
}
