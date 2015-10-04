import static org.junit.Assert.*;

import org.junit.Test;


public class BestPriceTableTest {

	static String[] pricelistArray = {
		"NTI",
		"1	     0.9\n"+
		"123	 0.17\n"+
		"1234	 0.2\n",

		"BTT",
		"1	     0.92\n"+
		"12	     5.1\n"+
		"123	 0.10\n"+
		"1234	 0.2\n",
		
		"ERR",
		"999	 abcd\n"+
		"abcd	 99.99\n"+
		"888	 99.99    99.99  99.99  99.99\n"+
		"777  \n"
	};
	
	@Test
	public void testBestPriceTable() {
		{
			assert(new BestPriceTable() != null);
		}
	}

	@Test
	public void testAddCarrier() {
		// basic test, add single carrier
		{
			BestPriceTable bpt = new BestPriceTable();
			bpt.addCarrier(pricelistArray[0], pricelistArray[1]);
			assertEquals(3, bpt.size());
			assertNotEquals(null, bpt.getCarrierPrice(1));
			assertNotEquals(null, bpt.getCarrierPrice(123));
			assertEquals(0.9, bpt.getCarrierPrice(1).price, 0.0001);
		}
		
		// test that adding a new carrier replaces the high prices but not the low ones
		{
			BestPriceTable bpt = new BestPriceTable();
			bpt.addCarrier(pricelistArray[0], pricelistArray[1]);
			assertEquals(3, bpt.size());
			bpt.addCarrier(pricelistArray[2], pricelistArray[3]);
			assertEquals(4, bpt.size());

			assertEquals(0.9, bpt.getCarrierPrice(1).price, 0.0001);
			assert(bpt.getCarrierPrice(1).carriers.contains("NTI"));
			assert(! bpt.getCarrierPrice(1).carriers.contains("BTT"));
			
			assertEquals(0.10, bpt.getCarrierPrice(123).price, 0.0001);
			assert(bpt.getCarrierPrice(1).carriers.contains("BTT"));
			assert(! bpt.getCarrierPrice(1).carriers.contains("NTT"));
		}
		
		// test that a tie adds both carriers to the list
		{
			BestPriceTable bpt = new BestPriceTable();
			bpt.addCarrier(pricelistArray[0], pricelistArray[1]);
			bpt.addCarrier(pricelistArray[2], pricelistArray[3]);
			assertEquals(2, bpt.getCarrierPrice(1234).carriers.size());
		}
		
		// test that it handles invalid input gracefully
		{
			BestPriceTable bpt = new BestPriceTable();
			bpt.addCarrier(pricelistArray[0], pricelistArray[1]);
			assertEquals(3, bpt.size());
			
			boolean exceptionThrown = false;
			try {
				bpt.addCarrier(pricelistArray[4], pricelistArray[5]);
			} catch (Exception e) {
				exceptionThrown = true;
			}
			assert(! exceptionThrown);
			assertEquals(3, bpt.size());
		}
	}

	@Test
	public void testGetPrefix() {
		{
			BestPriceTable bpt = new BestPriceTable();
			bpt.addCarrier(pricelistArray[0], pricelistArray[1]);

			assertEquals(1, bpt.getPrefix(new PhoneNumber("1999999999")));
			assertEquals(1, bpt.getPrefix(new PhoneNumber("1299999999")));
			assertEquals(123, bpt.getPrefix(new PhoneNumber("123999999")));
			assertEquals(1234, bpt.getPrefix(new PhoneNumber("123499999999")));
			assertEquals(0, bpt.getPrefix(new PhoneNumber("999999999")));
		}
	}

	@Test
	public void testGetCarrierPrice() {
		{
			BestPriceTable bpt = new BestPriceTable();
			bpt.addCarrier(pricelistArray[0], pricelistArray[1]);

			// test when the prefix is there
			BestPriceTable.CarrierPrice cp = bpt.getCarrierPrice(1);
			assertNotEquals(null, cp);
			assertEquals(0.9, cp.price, 0.00001);
			assertEquals(1, cp.carriers.size());
			assert(cp.carriers.contains("NTI"));
			
			// test when the prefix is not there
			cp = bpt.getCarrierPrice(0);
			assertEquals(null, cp);
			cp = bpt.getCarrierPrice(-1);
			assertEquals(null, cp);
			cp = bpt.getCarrierPrice(9999999);
			assertEquals(null, cp);
		}
	}

	@Test
	public void testSize() {
		{
			BestPriceTable bpt = new BestPriceTable();
			bpt.addCarrier(pricelistArray[0], pricelistArray[1]);
			assertEquals(3, bpt.size());
		}
	}

	@Test
	public void testToString() {
		{
			BestPriceTable bpt = new BestPriceTable();
			bpt.addCarrier(pricelistArray[0], pricelistArray[1]);
			String s = "1\t@ 0.9\tNTI\n"+
					"123\t@ 0.17\tNTI\n"+
					"1234\t@ 0.2\tNTI\n";
			assertEquals(s, bpt.toString());
		}
	}

}
