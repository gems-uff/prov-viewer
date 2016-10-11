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
        result = Utils.tryParseFloat("-1.5");
        assertEquals(true, result);
        result = Utils.tryParseFloat("1.5");
        assertEquals(true, result);
        result = Utils.tryParseFloat("1.5");
        assertEquals(true, result);
        result = Utils.tryParseFloat("asd1");
        assertEquals(false, result);
        result = Utils.tryParseFloat("8.4696454E-4");
        assertEquals(true, result);
        
    }

    /**
     * Test of convertFloat method, of class Utils.
     */
    @Test
    public void testConvertFloat() {
        System.out.println("convertFloat");
        Utils instance = new Utils();
        double result = instance.convertFloat("1");
        assertEquals(1, result, 0.0);
        result = instance.convertFloat("0");
        assertEquals(0.0F, result, 0.0);
        result = instance.convertFloat("1.0");
        assertEquals(1.0F, result, 0.0);
        result = instance.convertFloat("1.0");
        assertEquals(1.0F, result, 0.0);
        result = instance.convertFloat("1.0");
        assertEquals(1.0F, result, 0.0);
        result = instance.convertFloat("-1.0");
        assertEquals(-1.0F, result, 0.0);
        result = instance.convertFloat("-1.0");
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

    /**
     * Test of FloatEqualTo method, of class Utils.
     */
    @Test
    public void testFloatEqualTo() {
        System.out.println("FloatEqualTo");
        float left = 0.0F;
        float right = 0.0F;
        float epsilon = 0.0F;
        assertEquals(true, Utils.FloatEqualTo(left, right, epsilon));
        
        left = 1.0F;
        right = 0.0F;
        epsilon = 0.0F;
        assertEquals(false, Utils.FloatEqualTo(left, right, epsilon));
        
        left = 0.0F;
        right = 1.0F;
        epsilon = 0.0F;
        assertEquals(false, Utils.FloatEqualTo(left, right, epsilon));
        
        left = 1.0F;
        right = 0.0F;
        epsilon = 1.0F;
        assertEquals(true, Utils.FloatEqualTo(left, right, epsilon));
        
        left = 1.0F;
        right = 0.0F;
        epsilon = 2.0F;
        assertEquals(true, Utils.FloatEqualTo(left, right, epsilon));
    }
    
    /**
     * Test of median and quartile methods, of class Utils.
     */
    @Test
    public void testmedian() {
        Object[] test = {"1","2","3","4","5","6","7","8","9"};
        assertEquals("5.0", Utils.median(test, 0, test.length));
        assertEquals("2.5", Utils.quartile(test, 1));
        assertEquals("7.5", Utils.quartile(test, 3));
        
        Object[] test2 = {"3","5","7","8","12","13","14","18","21"};
        assertEquals("12.0", Utils.median(test2, 0, test2.length));
        assertEquals("6.0", Utils.quartile(test2, 1));
        assertEquals("16.0", Utils.quartile(test2, 3));
        
        Object[] test3 = {"3","7","8","5","12","14","21","15","18","14"};
        assertEquals("13.0", Utils.median(test3, 0, test3.length));
        assertEquals("7.0", Utils.quartile(test3, 1));
        assertEquals("15.0", Utils.quartile(test3, 3));
        
        Object[] test4 = {"19","26","25","37","32","28","22","23","29", "34", "39", "31"};
        assertEquals("28.5", Utils.median(test4, 0, test4.length));
        assertEquals("24.0", Utils.quartile(test4, 1));
        assertEquals("33.0", Utils.quartile(test4, 3));
        
        Object[] test5 = {"1", "2", "3"};
        assertEquals("2.0", Utils.median(test5, 0, test5.length));
        assertEquals("1", Utils.quartile(test5, 1));
        assertEquals("3", Utils.quartile(test5, 3));
    }
    
}
