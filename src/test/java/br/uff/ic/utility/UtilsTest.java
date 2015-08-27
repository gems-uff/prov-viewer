/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kohwalter
 */
public class UtilsTest {
    
    public UtilsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of tryParseFloat method, of class Utils.
     */
    @Test
    public void testTryParseFloat() {
        System.out.println("tryParseFloat");
        boolean result = Utils.tryParseFloat("");
        assertEquals(false, result);
        result = Utils.tryParseFloat("1");
        assertEquals(true, result);
        result = Utils.tryParseFloat("1.5");
        assertEquals(true, result);
        result = Utils.tryParseFloat("-1.5");
        assertEquals(true, result);
        result = Utils.tryParseFloat("+1.5");
        assertEquals(true, result);
        result = Utils.tryParseFloat("- 1.5");
        assertEquals(true, result);
        result = Utils.tryParseFloat("+ 1.5");
        assertEquals(true, result);
        result = Utils.tryParseFloat("1,5");
        assertEquals(true, result);
        result = Utils.tryParseFloat("asd1");
        assertEquals(false, result);
    }

    /**
     * Test of convertFloat method, of class Utils.
     */
    @Test
    public void testConvertFloat() {
        System.out.println("convertFloat");
        Utils instance = new Utils();
        float result = instance.convertFloat("1");
        assertEquals(1, result, 0.0);
        result = instance.convertFloat("0");
        assertEquals(0.0F, result, 0.0);
        result = instance.convertFloat("1.0");
        assertEquals(1.0F, result, 0.0);
        result = instance.convertFloat("+ 1.0");
        assertEquals(1.0F, result, 0.0);
        result = instance.convertFloat("+1.0");
        assertEquals(1.0F, result, 0.0);
        result = instance.convertFloat("-1.0");
        assertEquals(-1.0F, result, 0.0);
        result = instance.convertFloat("- 1.0");
        assertEquals(- 1.0F, result, 0.0);
        result = instance.convertFloat("1.5");
        assertEquals(1.5F, result, 0.0);
        result = instance.convertFloat("-1.5");
        assertEquals(-1.5F, result, 0.0);
    }

    /**
     * Test of tryParseInt method, of class Utils.
     */
    @Test
    public void testTryParseInt() {
        System.out.println("tryParseInt");
        boolean result = Utils.tryParseInt("");
        assertEquals(false, result);
        result = Utils.tryParseInt("1");
        assertEquals(true, result);
        result = Utils.tryParseInt("-1");
        assertEquals(true, result);
        result = Utils.tryParseInt("+1");
        assertEquals(true, result);
        result = Utils.tryParseInt("- 1");
        assertEquals(true, result);
        result = Utils.tryParseInt("+ 1");
        assertEquals(true, result);
        result = Utils.tryParseInt("1.5");
        assertEquals(false, result);
        result = Utils.tryParseInt("1,5");
        assertEquals(false, result);
        result = Utils.tryParseInt("asd1");
        assertEquals(false, result);
    }

    /**
     * Test of convertInt method, of class Utils.
     */
    @Test
    public void testConvertInt() {
        System.out.println("convertInt");
        Utils instance = new Utils();
        int result = instance.convertInt("1");
        assertEquals(1, result);
        result = instance.convertInt("-1");
        assertEquals(-1, result);
        result = instance.convertInt("- 1");
        assertEquals(-1, result);
        result = instance.convertInt("+1");
        assertEquals(1, result);
        result = instance.convertInt("+ 1");
        assertEquals(1, result);
    }

    /**
     * Test of roundToInt method, of class Utils.
     */
    @Test
    public void testRoundToInt() {
        System.out.println("roundToInt");
        Utils instance = new Utils();
        float result = instance.roundToInt("0");
        assertEquals(0, result, 0.0);
        result = instance.roundToInt("1.4");
        assertEquals(1, result, 0.0);
        result = instance.roundToInt("1.5");
        assertEquals(2, result, 0.0);
        result = instance.roundToInt("1.6");
        assertEquals(2, result, 0.0);
        result = instance.roundToInt("+1.6");
        assertEquals(2, result, 0.0);
        result = instance.roundToInt("+ 1.6");
        assertEquals(2, result, 0.0);
        result = instance.roundToInt("-1.6");
        assertEquals(-2, result, 0.0);
        result = instance.roundToInt("- 1.6");
        assertEquals(-2, result, 0.0);
    }
    
}
