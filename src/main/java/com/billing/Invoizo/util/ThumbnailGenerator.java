package com.billing.Invoizo.util;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component("thumbnailGenerator")
public class ThumbnailGenerator {

    public static final Logger logger = LoggerFactory.getLogger(ThumbnailGenerator.class);

    public void createThumbnail(String filePath, String fileName) throws IOException {
        logger.info("Creating Thumbnail image for " + fileName);
        try {

            File f = new File(filePath + File.separator + fileName);
            BufferedImage img = ImageIO.read(f); // load image
            BufferedImage thumbImg = Scalr.resize(img, Method.QUALITY, Mode.AUTOMATIC, 140, 105, Scalr.OP_ANTIALIAS);
            File f2 = new File(filePath + File.separator + "thumb" + File.separator + fileName);
            if (!f2.exists()) {
                f2.mkdirs();
            }
            ImageIO.write(thumbImg, fileName.substring(fileName.lastIndexOf(".") + 1), f2);
        } catch (Exception e) {
            logger.info("Exception Occured While Creating Thumbnail :" + e.getMessage());
        }
    }

}