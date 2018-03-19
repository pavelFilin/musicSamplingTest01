package Applet.Main;

import org.junit.Test;

import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void getBytesFromSamples() throws Exception {
        int[] initInt = { 2571, 512, 32634, 2431};

        byte[] expected = { 10, 11 , 2 , 0, 127, 122, 9, 127};

        byte[] actual = Main.getBytesFromSamples(initInt);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void multipleVolume() throws Exception {
    }

    @Test
    public void getSamples() throws Exception {
        byte[] initBytes = { 10, 11 , 2 , 0, 127, 122, 9, 127};

        int[] expected = { 2571, 512, 32634, 2431};

        int[] actual = Main.getSamples(initBytes);

        assertArrayEquals(expected, actual);
    }

}