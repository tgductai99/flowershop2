package poly.edu.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${upload.path:uploads/products}")
	private String uploadPath;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// Serve uploaded images from external directory
		Path uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
		String uploadDirPath = uploadDir.toUri().toString();
		
		// Map /uploads/** to the uploads directory
		registry.addResourceHandler("/uploads/**")
				.addResourceLocations(uploadDirPath);
		
		// Map /images/products/** to serve from BOTH external uploads and static resources
		// Spring will check locations in order: external uploads first, then static resources
		registry.addResourceHandler("/images/products/**")
				.addResourceLocations(uploadDirPath, "classpath:/static/images/products/");
	}
}
