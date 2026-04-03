package com.example.stockcontroller.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileImageService {

    @Value("${file.upload-dir}") // e.g., uploads/
    private String uploadDir;

    public String uploadImage(MultipartFile file) throws IOException {

        // 1️⃣ Create upload directory if it doesn't exist
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        // 2️⃣ Get original file name
        String fileName = file.getOriginalFilename();

        // 3️⃣ Build the full path
        Path path = Paths.get(uploadDir, fileName);

        // 4️⃣ Save file bytes to disk
        Files.write(path, file.getBytes());

        // 5️⃣ Return file name (or path if you want)
        return fileName;
    }

    public boolean deleteImage(String fileName) {
        if(fileName == null || fileName.isEmpty()) {
            return false;
        }
        Path path = Paths.get(uploadDir, fileName);
        File file = path.toFile();
        if(file.exists()) {
            return file.delete(); // returns true if deleted
        }
        return false;
    }
}