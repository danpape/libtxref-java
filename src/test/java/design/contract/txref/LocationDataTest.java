package design.contract.txref;

import org.junit.Test;

import static org.junit.Assert.*;

public class LocationDataTest {

    @Test
    public void getHrp() {
        String expected = "hello";
        LocationData locationData = new LocationData(expected, null, 0, 0, 0, 0);
        assertEquals(expected, locationData.getHrp());
    }

    @Test
    public void setHrp() {
        String expected = "hello";
        LocationData locationData = new LocationData(null, null, 0, 0, 0, 0);
        locationData.setHrp(expected);
        assertEquals(expected, locationData.getHrp());
    }

    @Test
    public void getTxref() {
        String expected = "hello";
        LocationData locationData = new LocationData(null, expected, 0, 0, 0, 0);
        assertEquals(expected, locationData.getTxref());
    }

    @Test
    public void setTxref() {
        String expected = "hello";
        LocationData locationData = new LocationData(null, null, 0, 0, 0, 0);
        locationData.setTxref(expected);
        assertEquals(expected, locationData.getTxref());
    }

    @Test
    public void getBlockHeight() {
        int expected = 6;
        LocationData locationData = new LocationData(null, null, expected, 0, 0, 0);
        assertEquals(expected, locationData.getBlockHeight());
    }

    @Test
    public void setBlockHeight() {
        int expected = 6;
        LocationData locationData = new LocationData(null, null, 0, 0, 0, 0);
        locationData.setBlockHeight(expected);
        assertEquals(expected, locationData.getBlockHeight());
    }

    @Test
    public void getTransactionPosition() {
        int expected = 6;
        LocationData locationData = new LocationData(null, null, 0, expected, 0, 0);
        assertEquals(expected, locationData.getTransactionPosition());
    }

    @Test
    public void setTransactionPosition() {
        int expected = 6;
        LocationData locationData = new LocationData(null, null, 0, 0, 0, 0);
        locationData.setTransactionPosition(expected);
        assertEquals(expected, locationData.getTransactionPosition());
    }

    @Test
    public void getTxoIndex() {
        int expected = 6;
        LocationData locationData = new LocationData(null, null, 0, 0, expected, 0);
        assertEquals(expected, locationData.getTxoIndex());
    }

    @Test
    public void setTxoIndex() {
        int expected = 6;
        LocationData locationData = new LocationData(null, null, 0, 0, 0, 0);
        locationData.setTxoIndex(expected);
        assertEquals(expected, locationData.getTxoIndex());
    }

    @Test
    public void getMagicCode() {
        int expected = 6;
        LocationData locationData = new LocationData(null, null, 0, 0, 0, expected);
        assertEquals(expected, locationData.getMagicCode());
    }

    @Test
    public void setMagicCode() {
        int expected = 6;
        LocationData locationData = new LocationData(null, null, 0, 0, 0, 0);
        locationData.setMagicCode(expected);
        assertEquals(expected, locationData.getMagicCode());
    }

    @Test
    public void testEquals_Reflexive() {
        LocationData a = new LocationData("a", null, 1, 2, 3, 4);
        assertEquals(a, a);
    }

    @Test
    public void testEquals_Symmetric() {
        LocationData a = new LocationData("a", null, 1, 2, 3, 4);
        LocationData b = new LocationData("a", null, 1, 2, 3, 4);

        assertTrue(a.equals(b) && b.equals(a));
    }

    @Test
    public void testEquals_Transitive() {
        LocationData a = new LocationData("a", null, 1, 2, 3, 4);
        LocationData b = new LocationData("a", null, 1, 2, 3, 4);
        LocationData c = new LocationData("a", null, 1, 2, 3, 4);

        assertEquals(a, b);
        assertEquals(b, c);
        assertEquals(a, c);
    }

    @Test
    public void testNotEquals() {
        LocationData a = new LocationData("a", null, 1, 2, 3, 4);
        LocationData b = new LocationData("b", null, 1, 2, 3, 5);

        assertNotEquals(a, b);
    }


    @Test
    public void testHashCode() {
        LocationData a = new LocationData("a", null, 1, 2, 3, 4);
        LocationData b = new LocationData("a", null, 1, 2, 3, 4);
        assertEquals(b.hashCode(), a.hashCode());
    }

    @Test
    public void testHashCodeNotEquals() {
        LocationData a = new LocationData("a", null, 1, 2, 3, 4);
        LocationData b = new LocationData("b", null, 1, 2, 3, 5);

        assertNotEquals(a.hashCode(), b.hashCode());
    }

}
