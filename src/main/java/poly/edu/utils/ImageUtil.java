package poly.edu.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class ImageUtil {

	// Upload directory outside classpath
	private static final String UPLOAD_DIR = "uploads/products";

	public static String save(MultipartFile imageFile) throws IOException {

		if (imageFile == null || imageFile.isEmpty()) {
			return null;
		}

		// Create upload directory if it doesn't exist
		Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
		Files.createDirectories(uploadPath);

		// Generate unique filename with timestamp
		String originalFilename = imageFile.getOriginalFilename();
		String fileExtension = "";
		if (originalFilename != null && originalFilename.contains(".")) {
			fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
		}
		String fileName = System.currentTimeMillis() + fileExtension;
		
		// Save file to upload directory
		Path filePath = uploadPath.resolve(fileName);
		Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		
		System.out.println("Image saved successfully to: " + filePath.toString());

		// Return path relative to /uploads/ URL (e.g., "products/123456.jpg")
		return "products/" + fileName;
	}

	public static boolean delete(String fileName) {

		if (fileName == null || fileName.isBlank()) {
			return false;
		}

		try {
			Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
			Path filePath;

			if (fileName.startsWith("products/")) {
				// New format: "products/123456.jpg"
				String actualFileName = fileName.substring("products/".length());
				filePath = uploadPath.resolve(actualFileName);
			} else {
				// Old format or direct filename
				filePath = uploadPath.resolve(fileName);
			}

			System.out.println("Attempting to delete image: " + filePath.toString());
			
			// Only delete if file exists
			boolean deleted = Files.deleteIfExists(filePath);
			if (deleted) {
				System.out.println("Image deleted successfully");
			} else {
				System.out.println("Image file not found: " + filePath.toString());
			}
			return deleted;

		} catch (IOException e) {
			System.err.println("Error deleting image: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
