package in.amarjeet.ngramanalyzer.util;

public class StrUtils {

	/**
	 * Returns TRUE if first string is numerically larger than or equal to second. 
	 * Example:- 
	 * 		if num1 = "123" and num2 = "22" 
	 * 		then it will return true, 
	 * 		because num1 isLarger than num2.
	 * 
	 * @param String
	 *            consist of digits[0-9] only with no leading zeros.
	 * @param String
	 *            consist of digits[0-9] only with no leading zeros.
	 * @return true if first number is numerically grater than equal to second, otherwise false.
	 */
	public static boolean isLarger(String num1, String num2) {
		int result = 0;
		if (num1.length() > num2.length()) {
			result = 1;
		} else if (num1.length() < num2.length()) {
			result = -1;
		} else {
			result = num1.compareTo(num2);
		}
		return result < 0 ? false : true;
	}

	/**
	 * Adds two strings numerically. Example :- if num1 = "123" and num2 = "22",
	 * then it will return a string "145".
	 * 
	 * @param String
	 *            consist of digits[0-9] only with no leading zeros.
	 * @param String
	 *            consist of digits[0-9] only with no leading zeros.
	 * @return numerical addition of strings.
	 */
	public static String addStr(String num1, String num2) {
		String longer = null, shorter = null;
		if (num1.length() >= num2.length()) {
			longer = num1;
			shorter = num2;
		} else {
			longer = num2;
			shorter = num1;
		}
		char[] res = new char[longer.length()];

		int mask = shorter.length() - longer.length();

		int carry = 0;
		for (int i = longer.length() - 1; i >= 0; i--) {
			char longerDigit = longer.charAt(i);
			char shorterDigit = (mask + i) < 0 ? '0' : shorter.charAt(mask + i);
			int sumDigit = Character.getNumericValue(longerDigit) + Character.getNumericValue(shorterDigit) + carry;
			carry = sumDigit / 10;
			sumDigit = sumDigit % 10;
			res[i] = Character.forDigit(sumDigit, 10);
		}
		return carry == 0 ? String.valueOf(res) : "1" + String.valueOf(res);
	}
}
