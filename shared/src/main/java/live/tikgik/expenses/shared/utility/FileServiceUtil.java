package live.tikgik.expenses.shared.utility;


import live.tikgik.expenses.shared.service.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileServiceUtil {


    public String saveColorImage(String sourceData ,String colorRefNo) throws IOException {
        BufferedImage image = saveImg(sourceData);
        // write the image to a file
        File folder = new File(AppConstants.uploadDirectory + File.separator + "product_colors");
        if(!folder.exists())
            if (folder.mkdir())
                log.info("created product_colors dir in upload dir");

        File outputfile = new File(folder +  File.separator + colorRefNo + ".png");
        log.info("file created");

        ImageIO.write(image, "png", outputfile);
        log.info("img saved");


        return AppConstants.uploadDirectory  +File.separator+colorRefNo + ".png";
    }
    public BufferedImage saveImg(String sourceData ) throws IOException {

        // create a buffered image
        BufferedImage image = null;
        byte[] imageByte;

        Base64.Decoder decoder = Base64.getDecoder();
        imageByte = decoder.decode(sourceData);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);

        image = ImageIO.read(bis);
        bis.close();
        log.info("img converted");
        return image;
    }

    public String saveFile(String base64File, String fileDir, String refNo, String extension) throws IOException {
        if (base64File == null || fileDir == null || refNo == null || extension == null) {
            throw new RuntimeException("cannot save file base64 file is null");
        }
        // Decode base64 file
        byte[] fileBytes = Base64.getDecoder().decode(base64File);

        // Generate unique file name
        String fileName = refNo + "." +extension;


        // Create the upload directory if it doesn't exist
        File uploadDir = new File(AppConstants.uploadDirectory + File.separator + fileDir);
        if (!uploadDir.exists()) {
            if (!uploadDir.mkdirs()) {
                return "Failed to create the directory.";
            }
        }
        Path filePath = uploadDir.toPath().resolve(fileName);
//        if (extension.equals("png")){
//            fileBytes = optimizeImage(fileBytes);
//        }
        try (OutputStream outputStream = Files.newOutputStream(filePath,
                StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            outputStream.write(fileBytes);
        }
//        if (!extension.equals("png")){
//            fileZipper.zipFile(filePath.toFile().getAbsolutePath(), uploadDir.getAbsolutePath());
//        }
        return filePath.toString();
    }

    public byte[] optimizeImage(byte[] imageData) throws IOException {
        BufferedImage image;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData)) {
            image = ImageIO.read(inputStream);
        }
        int newWidth = 800;
        int newHeight = (int) (image.getHeight() * ((double) newWidth / image.getWidth()));
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        resizedImage.getGraphics().drawImage(image, 0, 0, newWidth, newHeight, null);
        byte[] compressedImageData;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(resizedImage, "jpg", outputStream);
            compressedImageData = outputStream.toByteArray();
        }

        return compressedImageData;
    }
    public static void saveRestDocs(String sourceData, String fileDir, String refNo, String formatName) throws Exception {

        // create a buffered image
        BufferedImage image = null;
        byte[] imageByte;

        Base64.Decoder decoder = Base64.getDecoder();
        imageByte = decoder.decode(sourceData.trim().replace("\"", ""));
        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
//		try
        {
            image = ImageIO.read(bis);
            bis.close();

            // write the image to a file
            File outputfile = new File(AppConstants.uploadDirectory + fileDir + File.separator + refNo  + "."+ formatName);
            ImageIO.write(image, formatName, outputfile);


        } /*catch (IOException e) {
			e.printStackTrace();
		}*/
    }
}
