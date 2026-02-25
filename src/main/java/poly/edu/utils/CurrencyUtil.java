package poly.edu.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class CurrencyUtil {

	/**
	 * Format a price value to Vietnamese Dong (VND) format Example: 1000000 ->
	 * "1.000.000 ₫"
	 * 
	 * @param price the price value to format
	 * @return formatted price string with VND symbol
	 */
	public String format(Double price) {
		if (price == null) {
			return "0 ₫";
		}

		DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
		symbols.setGroupingSeparator('.');
		symbols.setDecimalSeparator(',');

		DecimalFormat formatter = new DecimalFormat("#,###", symbols);
		return formatter.format(price) + " ₫";
	}
	
	/**
	 * Format a price value to Vietnamese Dong (VND) format (static version for backward compatibility)
	 * 
	 * @param price the price value to format
	 * @return formatted price string with VND symbol
	 */
	public static String formatVND(Double price) {
		if (price == null) {
			return "0 ₫";
		}

		DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
		symbols.setGroupingSeparator('.');
		symbols.setDecimalSeparator(',');

		DecimalFormat formatter = new DecimalFormat("#,###", symbols);
		return formatter.format(price) + " ₫";
	}
}
