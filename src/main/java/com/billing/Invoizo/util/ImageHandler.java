package com.billing.Invoizo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component("imageHandler")
public class ImageHandler {

    @Autowired
    @Qualifier("thumbnailGenerator")
    private ThumbnailGenerator thumbnailGenerator;

    /* for single image */
    public String handleImage(MultipartFile file, String imageFolderPath, boolean isThumbRequired) {
        String fileName = null;
        try {
            if (!file.isEmpty()) {
                fileName = getFileName(file);
                file.transferTo(new File(imageFolderPath + File.separator + fileName));
            }
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        if (isThumbRequired) {
            try {
                thumbnailGenerator.createThumbnail(imageFolderPath, fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    private String getFileName(MultipartFile file) {
        return System.currentTimeMillis() + "_" + file.getOriginalFilename();
    }
}
