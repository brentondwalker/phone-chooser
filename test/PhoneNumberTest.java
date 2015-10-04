import static org.junit.Assert.*;

import org.junit.Test;


public class PhoneNumberTest {

	@Test
	public void testPhoneNumber() {
		// basic test of constructor
		{
			PhoneNumber pn = new PhoneNumber("1234567890");
			assertNotEquals(pn, null);
			assertEquals(pn.length(), 10);
		}
		
		// constructor should throw exception on illegal input
		{
			boolean exceptionThrown = false;
			try {
				new PhoneNumber("1234a567");
			} catch (NumberFormatException e) {
				exceptionThrown = true;
			}
			assert(exceptionThrown);
		}

		// constructor should throw exception on null input
		{
			boolean exceptionThrown = false;
			try {
				new PhoneNumber(null);
			} catch (NumberFormatException e) {
				exceptionThrown = true;
			}
			assert(exceptionThrown);
		}

		// constructor should throw exception on empty input
		{
			boolean exceptionThrown = false;
			try {
				new PhoneNumber("");
			} catch (NumberFormatException e) {
				exceptionThrown = true;
			}
			assert(exceptionThrown);
		}

		// constructor should gracefully handle phone number with whitespace
		{
			PhoneNumber pn = new PhoneNumber("123 456 7890");
			assertNotEquals(pn, null);
			assertEquals(pn.length(), 10);
		}
	}

	
	@Test
	public void testLength() {
		// basic
		{
			PhoneNumber pn = new PhoneNumber("1234567890");
			assertEquals(pn.length(), 10);
		}
		
		// ridiculous
		{
			PhoneNumber pn = new PhoneNumber("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
			assertEquals(pn.length(), 200);
		}
	}

	
	@Test
	public void testGetDigit() {
		// basic
		{
			PhoneNumber pn = new PhoneNumber("1234567890");
			assertEquals(pn.getDigit(0), 1);
			assertEquals(pn.getDigit(9), 0);
		}
		
		// should throw an exception for invalid arg
		{
			boolean exceptionThrown = false;
			try {
				PhoneNumber pn = new PhoneNumber("1234567890");
				pn.getDigit(11);
			} catch (ArrayIndexOutOfBoundsException e) {
				exceptionThrown = true;
			}
			assert(exceptionThrown);
		}
	}

	@Test
	public void testToString() {
		// basic
		{
			String pns = "1234567890";
			PhoneNumber pn = new PhoneNumber(pns);
			assertEquals(pns, pn.toString());
		}

		// ridiculous
		{
			String pns = "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
			PhoneNumber pn = new PhoneNumber(pns);
			assertEquals(pns, pn.toString());
		}
	}

}
