/*
 * The MIT License
 *
 * Copyright 2017 Kohwalter.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
//        System.out.println("tryParseFloat");
        boolean result = Utils.tryParseDouble("");
        assertEquals(false, result);
        result = Utils.tryParseDouble("1");
        assertEquals(true, result);
        result = Utils.tryParseDouble("1.5");
        assertEquals(true, result);
        result = Utils.tryParseDouble("-1.5");
        assertEquals(true, result);
        result = Utils.tryParseDouble("+1.5");
        assertEquals(true, result);
        result = Utils.tryParseDouble("-1.5");
        assertEquals(true, result);
        result = Utils.tryParseDouble("1.5");
        assertEquals(true, result);
        result = Utils.tryParseDouble("1.5");
        assertEquals(true, result);
        result = Utils.tryParseDouble("asd1");
        assertEquals(false, result);
        result = Utils.tryParseDouble("8.4696454E-4");
        assertEquals(true, result);
        
    }

    /**
     * Test of convertFloat method, of class Utils.
     */
    @Test
    public void testConvertFloat() {
//        System.out.println("convertFloat");
        Utils instance = new Utils();
        double result = instance.convertStringToDouble("1");
        assertEquals(1, result, 0.0);
        result = instance.convertStringToDouble("0");
        assertEquals(0.0F, result, 0.0);
        result = instance.convertStringToDouble("1.0");
        assertEquals(1.0F, result, 0.0);
        result = instance.convertStringToDouble("1.0");
        assertEquals(1.0F, result, 0.0);
        result = instance.convertStringToDouble("1.0");
        assertEquals(1.0F, result, 0.0);
        result = instance.convertStringToDouble("-1.0");
        assertEquals(-1.0F, result, 0.0);
        result = instance.convertStringToDouble("-1.0");
        assertEquals(- 1.0F, result, 0.0);
        result = instance.convertStringToDouble("1.5");
        assertEquals(1.5F, result, 0.0);
        result = instance.convertStringToDouble("-1.5");
        assertEquals(-1.5F, result, 0.0);
    }

    /**
     * Test of tryParseInt method, of class Utils.
     */
    @Test
    public void testTryParseInt() {
//        System.out.println("tryParseInt");
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
//        System.out.println("convertInt");
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
//        System.out.println("roundToInt");
        Utils instance = new Utils();
        double result = instance.roundToInt("0");
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
    public void testDoubleEqualTo() {
//        System.out.println("FloatEqualTo");
        double left = 0.0F;
        double right = 0.0F;
        double epsilon = 0.0F;
        assertEquals(true, Utils.DoubleEqualTo(left, right, epsilon));
        
        left = 1.0F;
        right = 0.0F;
        epsilon = 0.0F;
        assertEquals(false, Utils.DoubleEqualTo(left, right, epsilon));
        
        left = 0.0F;
        right = 1.0F;
        epsilon = 0.0F;
        assertEquals(false, Utils.DoubleEqualTo(left, right, epsilon));
        
        left = 1.0F;
        right = 0.0F;
        epsilon = 1.0F;
        assertEquals(true, Utils.DoubleEqualTo(left, right, epsilon));
        
        left = 1.0F;
        right = 0.0F;
        epsilon = 2.0F;
        assertEquals(true, Utils.DoubleEqualTo(left, right, epsilon));
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
