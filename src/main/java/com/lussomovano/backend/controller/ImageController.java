package com.lussomovano.backend.controller;

import com.lussomovano.backend.service.ImageUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageUploadService imageUploadService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            logger.warn("Upload failed: file is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Image file is empty");
        }

        try {
            logger.info("Uploading image: {}", file.getOriginalFilename());
            String imageUrl = imageUploadService.uploadImage(file);
            logger.info("Image uploaded successfully. URL: {}", imageUrl);
            return ResponseEntity.ok().body(imageUrl);
        } catch (IOException e) {
            logger.error("Error uploading image", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }
}
