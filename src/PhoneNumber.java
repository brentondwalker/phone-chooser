/**
 * Class representing a phone number, including any prefix.
 * Digits are stored as an array of byte.
 * 
 * @author brenton
 *
 */
public class PhoneNumber {
	
	protected byte[] digits = null;
	
	/**
	 * Constructor
	 * 
	 * Pass in a string containing the digits of the phone number (no spaces).
	 * 
	 * Anything other than plain ascii digits is illegal input, so don't worry
	 * about unicode here.
	 * 
	 * @param numberString
	 */
	public PhoneNumber(String numberString) {
		
		if (numberString==null || numberString.length()==0) {
			throw new NumberFormatException("The string is null or empty!");
		}
		
		// convert the number to bytes of ascii
		digits = numberString.replaceAll("\\s","").getBytes();
		
		// the ascii values of 0 and 9
		byte zero = "0".getBytes()[0];
		byte nine = "9".getBytes()[0];
		
		for (int i=0; i<digits.length; i++) {
			if (digits[i] < zero || digits[i] > nine) {
				throw new NumberFormatException("The string is not a valid bunch of digits: \""+numberString+"\"");
			}
			
			// move the ascii values down to numeric values
			digits[i] = (byte) (digits[i] - zero);
		}
	}
	

	/**
	 * returns the number of digits in the number
	 * 
	 * @return
	 */
	public int length() {
		return digits.length;
	}
	

	/**
	 * returns the digit in position pos as an integer
	 * 
	 * @param pos
	 * @return
	 */
	public int getDigit(int pos) {
		if (pos < 0 || pos > digits.length) {
			throw new ArrayIndexOutOfBoundsException("illegal digit position passed to getDigit: "+pos);
		}
		
		return (int)digits[pos];
	}

	
	/**
	 * toString method.  returns a string containing the phone number.
	 * 
	 * Iteratively build a string through concatenation.  Not really efficient.
	 * 
	 */
	public String toString() {
		String s = "";
		for (int i=0; i<digits.length; i++) {
			s += digits[i];
		}
		return s;
	}
	
}
