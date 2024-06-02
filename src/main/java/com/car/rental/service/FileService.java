package com.car.rental.service;

import com.car.rental.model.Car;
import com.car.rental.model.ImageUploadForm;
import com.car.rental.model.User;
import com.car.rental.utils.Constants;
import com.car.rental.utils.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.jboss.logmanager.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@ApplicationScoped
public class FileService {

    @Inject
    UserService userService;
    @Inject
    CarService carService;
    public final static Logger LOGGER = Logger.getLogger(TokenService.class.getSimpleName());

    public Response saveUserProfilePicture(ImageUploadForm form, String userId) {
        String directory = "uploaded-images/users/" + userId + "/";
        var response = this.saveImage(form, directory);
        if (response.getStatusInfo() != Response.Status.OK) {
            return response;
        }
        User user = userService.findUserById(Long.parseLong(userId));
        user.setProfilePicturePath(form.fileName);
        userService.updateUser(user);
        return Response.status(Response.Status.OK).entity("{\"message\": \"Saved picture successfully!\"}").build();
    }

    public Response saveCarPicture(ImageUploadForm form, String carId) {
        String directory = "uploaded-images/cars/" + carId + "/thumbnail/";
        var response = this.saveImage(form, directory);
        if (response.getStatusInfo() != Response.Status.OK) {
            return response;
        }
        Car car = carService.findCarById(Long.parseLong(carId));
        car.setPicturePath(form.fileName);
        carService.updateCar(car);
        return response;
    }

    public Response saveImage(ImageUploadForm form, String directoryPath) {
        try {
            // Convert byte array to BufferedImage
            ByteArrayInputStream bis = new ByteArrayInputStream(form.data);
            BufferedImage originalImage = ImageIO.read(bis);

            // Resize the image if necessary
            BufferedImage resizedImage = resizeImage(originalImage);

            // Save the image to the specified directory
            File directory = new File(directoryPath);

            boolean result = directory.mkdirs();
            LOGGER.info(result ? "Directory created!" : "Directory exists!");

            File file = new File(directoryPath + form.fileName);
            ImageIO.write(resizedImage, "jpg", file);

            return Response.ok("File uploaded successfully").build();
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("File upload failed").build();
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage) {
        int maxWidth = Constants.maxProfilePictureWidth;
        int maxHeight = Constants.maxProfilePictureHeight;

        if (originalImage.getWidth() > maxWidth || originalImage.getHeight() > maxHeight) {
            return Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, maxWidth, maxHeight);
        }
        return originalImage;
    }

    public String uploadCarImages(MultipartFormDataInput input, String carId) {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<String> fileNames = new ArrayList<>();

        List<InputPart> inputParts = uploadForm.get("file");
        if (inputParts != null) {
            LOGGER.info("InputParts size: " + inputParts.size());
            String fileName;
            for (InputPart inputPart : inputParts) {
                try {
                    MultivaluedMap<String, String> header = inputPart.getHeaders();
                    fileName = getFileName(header);
                    fileNames.add(fileName);
                    System.out.println("File Name: " + fileName);
                    InputStream inputStream = inputPart.getBody(InputStream.class, null);
                    byte[] bytes = IOUtils.toByteArray(inputStream);

                    String path = "uploaded-images/cars/" + carId + "/";
                    File directory = new File(path);
                    boolean result = directory.mkdirs();
                    LOGGER.info(result ? "Directory created!" : "Directory exists!");

                    File file = new File(directory + "/" + fileName);
                    fileName = file.getAbsolutePath();
                    Files.write(Paths.get(fileName), bytes, StandardOpenOption.CREATE_NEW);
                } catch (Exception e) {
                    LOGGER.warning(e.getMessage());
                }
            }
        } else {
            LOGGER.info("No parts found!");
        }
        return String.join(", ", fileNames);
    }

    private String getFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                return name[1].trim().replaceAll("\"", "");
            }
        }
        return "unknown";
    }
}
