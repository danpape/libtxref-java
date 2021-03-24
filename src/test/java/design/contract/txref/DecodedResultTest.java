package design.contract.txref;

import org.junit.Test;

import static org.junit.Assert.*;

public class DecodedResultTest {

    @Test
    public void getHrp() {
        String expected = "hello";
        DecodedResult decodedResult = new DecodedResult(expected, null, 0, 0, 0, 0);
        assertEquals(expected, decodedResult.getHrp());
    }

    @Test
    public void setHrp() {
        String expected = "hello";
        DecodedResult decodedResult = new DecodedResult(null, null, 0, 0, 0, 0);
        decodedResult.setHrp(expected);
        assertEquals(expected, decodedResult.getHrp());
    }

    @Test
    public void getTxref() {
        String expected = "hello";
        DecodedResult decodedResult = new DecodedResult(null, expected, 0, 0, 0, 0);
        assertEquals(expected, decodedResult.getTxref());
    }

    @Test
    public void setTxref() {
        String expected = "hello";
        DecodedResult decodedResult = new DecodedResult(null, null, 0, 0, 0, 0);
        decodedResult.setTxref(expected);
        assertEquals(expected, decodedResult.getTxref());
    }

    @Test
    public void getBlockHeight() {
        int expected = 6;
        DecodedResult decodedResult = new DecodedResult(null, null, expected, 0, 0, 0);
        assertEquals(expected, decodedResult.getBlockHeight());
    }

    @Test
    public void setBlockHeight() {
        int expected = 6;
        DecodedResult decodedResult = new DecodedResult(null, null, 0, 0, 0, 0);
        decodedResult.setBlockHeight(expected);
        assertEquals(expected, decodedResult.getBlockHeight());
    }

    @Test
    public void getTransactionPosition() {
        int expected = 6;
        DecodedResult decodedResult = new DecodedResult(null, null, 0, expected, 0, 0);
        assertEquals(expected, decodedResult.getTransactionPosition());
    }

    @Test
    public void setTransactionPosition() {
        int expected = 6;
        DecodedResult decodedResult = new DecodedResult(null, null, 0, 0, 0, 0);
        decodedResult.setTransactionPosition(expected);
        assertEquals(expected, decodedResult.getTransactionPosition());
    }

    @Test
    public void getTxoIndex() {
        int expected = 6;
        DecodedResult decodedResult = new DecodedResult(null, null, 0, 0, expected, 0);
        assertEquals(expected, decodedResult.getTxoIndex());
    }

    @Test
    public void setTxoIndex() {
        int expected = 6;
        DecodedResult decodedResult = new DecodedResult(null, null, 0, 0, 0, 0);
        decodedResult.setTxoIndex(expected);
        assertEquals(expected, decodedResult.getTxoIndex());
    }

    @Test
    public void getMagicCode() {
        int expected = 6;
        DecodedResult decodedResult = new DecodedResult(null, null, 0, 0, 0, expected);
        assertEquals(expected, decodedResult.getMagicCode());
    }

    @Test
    public void setMagicCode() {
        int expected = 6;
        DecodedResult decodedResult = new DecodedResult(null, null, 0, 0, 0, 0);
        decodedResult.setMagicCode(expected);
        assertEquals(expected, decodedResult.getMagicCode());
    }

    @Test
    public void testEquals_Reflexive() {
        DecodedResult a = new DecodedResult("a", null, 1, 2, 3, 4);
        assertEquals(a, a);
    }

    @Test
    public void testEquals_Symmetric() {
        DecodedResult a = new DecodedResult("a", null, 1, 2, 3, 4);
        DecodedResult b = new DecodedResult("a", null, 1, 2, 3, 4);

        assertTrue(a.equals(b) && b.equals(a));
    }

    @Test
    public void testEquals_Transitive() {
        DecodedResult a = new DecodedResult("a", null, 1, 2, 3, 4);
        DecodedResult b = new DecodedResult("a", null, 1, 2, 3, 4);
        DecodedResult c = new DecodedResult("a", null, 1, 2, 3, 4);

        assertEquals(a, b);
        assertEquals(b, c);
        assertEquals(a, c);
    }

    @Test
    public void testNotEquals() {
        DecodedResult a = new DecodedResult("a", null, 1, 2, 3, 4);
        DecodedResult b = new DecodedResult("b", null, 1, 2, 3, 5);

        assertNotEquals(a, b);
    }


    @Test
    public void testHashCode() {
        DecodedResult a = new DecodedResult("a", null, 1, 2, 3, 4);
        DecodedResult b = new DecodedResult("a", null, 1, 2, 3, 4);
        assertEquals(b.hashCode(), a.hashCode());
    }

    @Test
    public void testHashCodeNotEquals() {
        DecodedResult a = new DecodedResult("a", null, 1, 2, 3, 4);
        DecodedResult b = new DecodedResult("b", null, 1, 2, 3, 5);

        assertNotEquals(a.hashCode(), b.hashCode());
    }

}
