
/**
 * Takes a list of phone numbers as arguments, instantiates a bunch of Carrier objects
 * and for each number picks the carrier with the best price.
 * 
 * @author brenton
 *
 */
public class CarrierFinder {

	// there are a lot of ways the carrier price lists could be given.
	// This is basically pretending like we read them from a file, without
	// actually messing with files.
	static String[] pricelistArray = {
		"NTI",
		"1	 0.9\n"+
		"268	 5.1\n"+
		"46	 0.17\n"+
		"4620	 0.0\n"+
		"468	 0.15\n"+
		"4631	 0.15\n"+
		"4673	 0.9\n"+
		"46732	 1.1\n",

		"BTT",
		"1	 0.92\n"+
		"44	 0.5\n"+
		"46	 0.2\n"+
		"467	 1.0\n"+
		"48	 1.2\n"
	};
	
	public static void main(String args[]) {

		if (args.length==0) {
			System.out.println("usage: phone-chooser <number1> <number2> ...");
			System.exit(0);
		}
		
		// instantiate the BestPriceList object
		BestPriceTable pricelist = new BestPriceTable();
		
		if (pricelistArray.length%2 != 0) {
			System.err.println("ERROR: static pricelist array must have even length.");
			System.exit(0);
		}
		for (int i=0; i<pricelistArray.length; i+=2) {
			pricelist.addCarrier(pricelistArray[i], pricelistArray[i+1]);
			//System.out.println(carrierList.get(i/2));
		}

		System.out.println("Full Best Price Table:\n-------------------------------\n");
		System.out.println(pricelist);
		System.out.println("-------------------------------\n\n");
		
		// for each number passed in, find the carrier with the best price
		for (String numberString : args) {
			PhoneNumber phNum = null;
			try {
				phNum = new PhoneNumber(numberString);
			} catch(NumberFormatException e) {
				// if one of the inputs is illegal, print the error, but still try the others
				System.err.println(e);
				continue;
			}
			
			int prefix = pricelist.getPrefix(phNum);
			BestPriceTable.CarrierPrice cp = pricelist.getCarrierPrice(prefix);
			
			System.out.println("Phone number: "+numberString);
			if (cp == null) {
				System.out.println("\t** No carriers found!");				
			} else {
				String carrierString = "";
				for (String name : cp.carriers) {
					carrierString += name+"  ";
				}
				System.out.println("\tBest price: +"+prefix+" @ "+cp.price+" on carrier(s) "+carrierString);
			}
		}
	}
	
}
