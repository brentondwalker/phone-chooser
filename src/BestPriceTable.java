import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.TreeMap;

/**
 * This class will hold the table of the best price and corresponding carrier
 * for every known prefix.
 * 
 * The set of known prefixes is global across all carriers, and the best price
 * for any one of those prefixes is unique (though the winning carrier may not be).
 * Therefore we can make a single global table of the best price for each prefix.
 * 
 * This class relies on the assumption that prefixes cannot have a leading '0' digit.
 * 
 * @author brenton
 *
 */
public class BestPriceTable {

	public class CarrierPrice {
		public HashSet<String> carriers = new HashSet<String>();
		public double price;
		public CarrierPrice(String c, double p) {
			carriers.add(c);
			price = p;
		}
	};
	
	// the table of best prices for each prefix
	protected TreeMap<Integer,CarrierPrice> pricelist = null;

	// I'm being lazy by not using a prefix trie to store the price lists,
	// but this will get the same type optimization.  It will stop the
	// prefix search through the phone number when there is no possibility
	// of finding a match.
	protected HashSet<Integer> nonterminalPrefix = null;
	
	
	/**
	 * Constructor
	 */
	public BestPriceTable() {
		pricelist = new TreeMap<Integer,CarrierPrice>();
		nonterminalPrefix = new HashSet<Integer>();
	}
	
	
	/**
	 * Add this carrier's info to our BestPriceTable.
	 * 
	 * The info is only relevant in cases where this carrier has the best
	 * price for a given prefix.
	 * 
	 * @param name       - the carrier's name
	 * @param pricetable - big multi-line string containing the price table
	 */
	public void addCarrier(String name, String pricetable) {
		if (name==null || name=="") {
			System.err.println("WARNING: Carrier constructor called with null or empty name.  Using UNKNOWN.");
			name = "UNKNOWN";
		}
		if (pricetable==null) {
			return;
		}

	    BufferedReader br = new BufferedReader(new StringReader(pricetable));
	    String line;
	    try {
			while ((line = br.readLine()) != null) {
				String tline = line.trim();
				String[] fields = tline.split("\\s+");
				if (fields.length != 2) {
					System.err.println("WARNING: Illegal line in carrier info "+name+"\n\t"+line);
					continue;
				}
				// should check that the string is actually an integer
				int prefix = Integer.parseInt(fields[0]);
				double price = Double.parseDouble(fields[1]);

				// check if we already have a price for this prefix
				if (pricelist.containsKey(prefix)) {
					
					if (price == pricelist.get(prefix).price) {
						// if this carrier has the same price add it to the set
						pricelist.get(prefix).carriers.add(name);

					} else if (price < pricelist.get(prefix).price) {
						// if this carrier has a better price throw out the old one
						pricelist.put(prefix, new CarrierPrice(name, price));
					}
				} else {
					// if we have no prior price for this prefix create one
					pricelist.put(prefix, new CarrierPrice(name, price));
				}
								
				// record that all pre-prefixes are non-terminal
				for (int i=1; i<fields[0].length(); i++) {
					int preprefix = Integer.parseInt(fields[0].substring(0, i));
					nonterminalPrefix.add(preprefix);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Given an array of bytes, representing the digits of a phone number, return the
	 * the longest price-able prefix in this number, based on all prefixes we have ever seen.
	 * - if more than one prefix matches, returns the price for the longest prefix
	 * - if there is no match, or some other error, return something negative
	 * 
	 * @param phNumber  the PhoneNumber object
	 * @return          the longest price-able prefix in this number
	 */
	public int getPrefix(PhoneNumber phNumber) {
		int longestValidPrefix = 0;
		int prefix = 0;
		
		// maybe should throw an exception...?
		if (phNumber == null) {
			return 0;
		}

		// incrementally increase how much of the number is treated as a prefix,
		// and test if any carrier has a price for it.
		// This can actually test the case where the number is equal to a prefix.
		for (int i=0; i<phNumber.length(); i++) {
			if (phNumber.getDigit(i) > 9 || phNumber.getDigit(i) < 0) {
				System.err.println("ERROR: getPrice() illegal digit of phone number: "+phNumber.getDigit(i));
				return 0;
			}
			prefix = prefix*10 + phNumber.getDigit(i);
			if (pricelist.containsKey(prefix)) {
				longestValidPrefix = prefix;
			}

			// if this prefix is terminal, then there is no point in iterating
			// through the rest of the number
			if (! nonterminalPrefix.contains(prefix)) {
				//System.out.println("Abandoning search with prefix: "+prefix);
				return longestValidPrefix;
			}
		}
		return longestValidPrefix;
	}
	
	
	/**
	 * Look up the best price and carrier(s) for this prefix.
	 * If the prefix isn't available, return null.
	 * 
	 * @param prefix
	 * @return
	 */
	public CarrierPrice getCarrierPrice(int prefix) {
		CarrierPrice cp = null;
		if (pricelist.containsKey(prefix)) {
			cp = pricelist.get(prefix);
		}
		
		return cp;
	}

	
	/**
	 * return the number of prefixes in the pricelist
	 * 
	 * @return
	 */
	int size() {
		return pricelist.size();
	}
	
	/**
	 * returns a string containing the best price table
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();

		for (int prefix : pricelist.keySet()) {
			CarrierPrice cp = pricelist.get(prefix);
			if (cp == null) {
				System.err.println("WARNING: BestPriceTable.toString(): have a null entry in pricelist for "+prefix);
				continue;
			}
			s.append(prefix);
			s.append("\t@ ");
			s.append(cp.price);
			s.append("\t");
			boolean firstthing = true;
			for (String carrierName : pricelist.get(prefix).carriers) {
				if (! firstthing) {
					s.append(", ");
				} else {
					firstthing = false;
				}
				s.append(carrierName);
			}
			s.append("\n");
		}
		
		return s.toString();
	}
	
}


