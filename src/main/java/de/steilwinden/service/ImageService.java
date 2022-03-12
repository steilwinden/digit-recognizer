package de.steilwinden.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

@Slf4j
@Service
public class ImageService {

    private static final int LIMIT_WIDTH = 28;
    private static final int LIMIT_HEIGHT = 28;
    private static final int RGB_BLACK = 0;
    private static final int FRAME = 40;

    private int calculateHeight(int width, int height) {
        return Math.round(LIMIT_WIDTH * height / (float) width);
    }

    public BufferedImage resizeImage(BufferedImage sourceImage) {
        Image thumbnail = sourceImage.getScaledInstance(LIMIT_WIDTH, LIMIT_HEIGHT, Image.SCALE_SMOOTH);
        BufferedImage bufferedThumbnail =
                new BufferedImage(thumbnail.getWidth(null), thumbnail.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedThumbnail.getGraphics();
        graphics.drawImage(thumbnail, 0, 0, null);
        graphics.dispose();
        return bufferedThumbnail;
    }

    private BufferedImage clone(BufferedImage source) {
        BufferedImage bufferedImage = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(source, 0, 0, null);
        graphics.dispose();
        return bufferedImage;
    }

    public BufferedImage thickenLine(final BufferedImage image) {
        int rgb;
        int thickness = 6;
        BufferedImage clonedImage = clone(image);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                rgb = image.getRGB(x, y);
                if (rgb != RGB_BLACK) {
                    for (int i = thickness * -1; i <= thickness; i++) {
                        for (int j = thickness * -1; j <= thickness; j++) {
                            drawWhitePoint(clonedImage, x + j, y + i);
                        }
                    }
                }
            }
        }
        return clonedImage;
    }

    private void drawWhitePoint(BufferedImage image, int x, int y) {
        if (x < 0 || y < 0 || x >= image.getWidth() || y >= image.getHeight()) {
            log.warn("cannot draw white point at: " + x + "," + y);
            return;
        }
        Color white = new Color(255, 255, 255);
        int rgb = white.getRGB();
        image.setRGB(x, y, rgb);
    }

    public void print(BufferedImage image) {
        int rgb;
        for (int y = 0; y < image.getHeight(); y++) {
            StringBuilder s = new StringBuilder();
            for (int x = 0; x < image.getWidth(); x++) {
                rgb = image.getRGB(x, y);
                s.append(rgb).append(",");
            }
            log.info(s.toString());
        }
    }

    public BufferedImage centerImage(BufferedImage image) {
        int x1 = image.getWidth();
        int x2 = 0;
        int y1 = image.getHeight();
        int y2 = 0;
        int rgb;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                rgb = image.getRGB(x, y);
                if (rgb != RGB_BLACK) {
                    if (x < x1) {
                        x1 = x;
                    }
                    if (x > x2) {
                        x2 = x;
                    }
                    if (y < y1) {
                        y1 = y;
                    }
                    if (y > y2) {
                        y2 = y;
                    }
                }
            }
        }
        int width = x2 - x1;
        int height = y2 - y1;
        int halfWidth = Math.round(width / 2f);
        int halfHeight = Math.round(height / 2f);
        int length = Math.max(width, height);
        int halfLength = Math.round(length / 2f);

        int imageHalfWidth = Math.round(image.getWidth() / 2f);
        int imageHalfHeight = Math.round(image.getHeight() / 2f);

        BufferedImage destImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics graphics = destImage.getGraphics();
        graphics.drawImage(image, imageHalfWidth - halfWidth, imageHalfHeight - halfHeight,
                imageHalfWidth + halfWidth, imageHalfHeight + halfHeight,
                x1 - FRAME, y1 - FRAME, x1 + width + FRAME, y1 + height + FRAME, null);
        graphics.dispose();

        return destImage.getSubimage(imageHalfWidth - halfLength,
                imageHalfHeight - halfLength, length, length);
    }

}
