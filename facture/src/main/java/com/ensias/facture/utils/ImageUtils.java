package com.ensias.facture.utils;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class ImageUtils {
    public static String encodeImageToBase64(String path) throws IOException {
        byte[] imageBytes = Files.readAllBytes(Path.of(path));
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}

