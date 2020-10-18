package testPackage;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PrimeTests {

    @Test
    public void testPrime() {
        Assert.assertTrue(Prime.isPrimeFast(Integer.MAX_VALUE));
    }

    @Test(enabled = false)
    public void testPrimeLong() {
        long n = Integer.MAX_VALUE;
        Assert.assertTrue(Prime.isPrime(n));
    }

    @Test
    public void testNonPrime() {
        Assert.assertFalse(Prime.isPrime(Integer.MAX_VALUE - 2));
    }
}
