package live.tikgik.expenses.shared.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import java.io.File;


@Slf4j
public class AppConstants {

	public static String uploadDirectory;
	public static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";
	public static final String UPLOAD_DIR_NAME = "uploads";
	public static final String PRODUCT_DIR_NAME = "products";
	public static final String COLOR_IMG_DIR_NAME = "product_colors";
	public static final String MEASURES_DIR_NAME = "prodSize";
	public static final String PRODUCT_POLICY_DIR_NAME = "product_policy";
	public static final String PRODUCT_WARRANTY_DIR_NAME = "product_warranty";

	public static final String PRODUCT_ACCESSORIES_DIR_NAME = "product_accessories";
	public static final String PRODUCT_ELEC_SPEC_DIR_NAME = "product_elec_spec";


	@PostConstruct
	private static void getBaseDir() {
		File baseDir = new File(System.getProperty("user.home"), UPLOAD_DIR_NAME);
		File uploadDir = new File(baseDir, PRODUCT_DIR_NAME);

		if (!baseDir.exists()) {
			if (baseDir.mkdirs()) {
				log.info("Created base directory: " + baseDir.getAbsolutePath());
			} else {
				log.error("Failed to create base directory: " + baseDir.getAbsolutePath());
				// Handle the error or throw an exception
				uploadDirectory = uploadDir.getAbsolutePath();
				return;
			}
		}

		if (!uploadDir.exists()) {
			if (uploadDir.mkdirs()) {
				log.info("Created product directory: " + uploadDir.getAbsolutePath());
			} else {
				log.error("Failed to create product directory: " + uploadDir.getAbsolutePath());
				// Handle the error or throw an exception
				uploadDirectory = uploadDir.getAbsolutePath();
			}
		}
	}

	public static final String PAGE_NUMBER = "0";

	public static final String PAGE_SIZE = "2";
	public static final String SORT_CATEGORIES_BY = "categoryId";
	public static final String SORT_PRODUCTS_BY = "productId";
	public static final String SORT_DIR = "asc";
	public static final String[] PUBLIC_URLS = { "/v3/api-docs/**", "/swagger-ui/**", "/api/register/**", "/api/login" };
	public static final String[] USER_URLS = { "/api/public/**" };
	public static final String[] ADMIN_URLS = { "/api/admin/**" };
	
}