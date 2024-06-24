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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Slf4j
@ApplicationScoped
public class FileService {
    public final static Logger LOGGER = Logger.getLogger(TokenService.class.getSimpleName());

    @Inject
    UserService userService;
    @Inject
    CarService carService;


    public Response getUserProfilePicture(String userId) {
        User user = userService.findUserById(Long.parseLong(userId));
        String profilePicturePath = user.getProfilePicturePath();
        String directoryPath = "uploaded-images/users/" + userId + "/";
        String fullPath = directoryPath + profilePicturePath;

        File thumbnailFile = new File(fullPath);
        if (!thumbnailFile.exists()) {
            LOGGER.warning("No profile picture uploaded for user:" + userId);

            return Response.status(Response.Status.NOT_FOUND).entity("Profile picture file not found").build();
        }
        try {
            Path imagePath = Paths.get(fullPath);
            byte[] imageData = Files.readAllBytes(imagePath);
            LOGGER.info("Got picture for user:" + userId);
            return Response.ok(imageData).build();
        } catch (Exception e) {
            LOGGER.warning("Error reading image file:" + Arrays.toString(e.getStackTrace()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error reading image file").build();
        }
    }

    public Response getCarThumbnail(String carId) {
        Car car = carService.findCarById(Long.parseLong(carId));
        String thumbnailPath = car.getPicturePath();
        String directoryPath = "uploaded-images/cars/" + carId + "/thumbnail/";
        String fullPath = directoryPath + thumbnailPath;

        File thumbnailFile = new File(fullPath);
        if (!thumbnailFile.exists()) {
            LOGGER.warning("Main picture file not found for car:" + carId);
            return Response.status(Response.Status.NOT_FOUND).entity("Main picture file not found").build();
        }

        try {
            Path imagePath = Paths.get(fullPath);
            byte[] imageData = Files.readAllBytes(imagePath);
            LOGGER.log(Level.INFO, "Got picture for car:" + carId);
            return Response.ok(imageData).build();
        } catch (Exception e) {
            LOGGER.warning(Arrays.toString(e.getStackTrace()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error reading image file").build();
        }
    }

    public Response saveUserProfilePicture(ImageUploadForm form, String userId) {
        String directory = "uploaded-images/users/" + userId + "/";
        var response = this.saveImage(form, directory);
        if (response.getStatusInfo() != Response.Status.OK) {
            return response;
        }
        User user = userService.findUserById(Long.parseLong(userId));
        user.setProfilePicturePath(form.fileName);
        userService.updateUser(user);
        LOGGER.log(Level.INFO, "Updated profile picture for user:" + userId);
        return Response.status(Response.Status.OK).entity("{\"message\": \"Saved profile picture successfully!\"}").build();
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
        LOGGER.log(Level.INFO, "Uploaded thumbnail picture for car:" + carId);
        return Response.status(Response.Status.OK).entity("{\"message\": \"Saved car picture successfully!\"}").build();
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
            LOGGER.info("Uploaded file:" + file.getName());
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
            LOGGER.warning("Resizing image!");
            return Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, maxWidth, maxHeight);
        }
        LOGGER.warning("Could not resize image!");
        return originalImage;
    }

    public Response uploadCarImages(MultipartFormDataInput input, String carId) {
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
        String uploadedCarImages = String.join(", ", fileNames);
        String responseMessage = "All files " + uploadedCarImages + " uploaded successfully.";
        LOGGER.info(responseMessage);

        return Response.status(Response.Status.OK).entity("{\"message\": \"" + responseMessage + "\"}").build();
    }

    private String getFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                LOGGER.info("Got File Name:" + filename);
                return name[1].trim().replaceAll("\"", "");
            }
        }
        LOGGER.warning("Could not get file name!");
        return "unknown";
    }
}
