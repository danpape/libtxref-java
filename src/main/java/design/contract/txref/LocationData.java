package design.contract.txref;

import java.util.Objects;

@SuppressWarnings("WeakerAccess")
public class LocationData {
        private String hrp;
        private String txref;
        private int blockHeight;
        private int transactionPosition;
        private int txoIndex;
        private int magicCode;

    public LocationData(String hrp, String txref, int blockHeight, int transactionPosition, int txoIndex, int magicCode) {
        this.hrp = hrp;
        this.txref = txref;
        this.blockHeight = blockHeight;
        this.transactionPosition = transactionPosition;
        this.txoIndex = txoIndex;
        this.magicCode = magicCode;
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
                Objects.equals(txref, that.txref);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hrp, txref, blockHeight, transactionPosition, txoIndex, magicCode);
    }
}
