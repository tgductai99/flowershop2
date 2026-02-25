package poly.edu.utils;

import java.util.ArrayList;
import java.util.List;

public class PaginationUtil {

	/**
	 * Paginate an list
	 * 
	 * @param list The full list to paginate
	 * @param page The page number (starts from 0)
	 * @param size The number of items per page
	 * @return A sublist containing items for the requested page
	 */
	public static <T> List<T> paginate(List<T> list, int page, int size) {

		if (list == null || list.size() == 0) {
			return new ArrayList<>();
		}
		if (page < 0 || size <= 0) {
			throw new IllegalArgumentException("Page must be >= 0 and size must be > 0");
		}

		int total = list.size();
		int startIndex = page * size;

		// If page is beyond available data, return empty list
		if (startIndex >= total) {
			return new ArrayList<>();
		}

		// Calculate end index (don't go beyond list length)
		int endIndex = Math.min(startIndex + size, total);

		// Extract the page slice
		return list.subList(startIndex, endIndex);

	}

	/**
	 * Calculate total number of pages
	 */
	public static <T> int getTotalPages(List<T> list, int size) {
		if (list == null || list.size() == 0 || size <= 0) {
			return 0;
		}
		return (int) Math.ceil((double) list.size() / size);
	}
}