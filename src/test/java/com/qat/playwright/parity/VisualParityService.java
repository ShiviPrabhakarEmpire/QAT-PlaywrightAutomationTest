package com.qat.playwright.parity;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Assertions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VisualParityService {

    private static final String CSS_NEUTRALIZER = """
        *, *::before, *::after {
            caret-color: transparent !important;
            transition: none !important;
            animation: none !important;
        }
        """;

    public static void comparePages(Page basePage, Page targetPage, String testName) throws IOException {
        neutralizePage(basePage);
        neutralizePage(targetPage);

        byte[] baseBytes = basePage.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        byte[] targetBytes = targetPage.screenshot(new Page.ScreenshotOptions().setFullPage(true));

        BufferedImage baseImage;
        BufferedImage targetImage;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(baseBytes);
             ByteArrayInputStream tais = new ByteArrayInputStream(targetBytes)) {
            baseImage = ImageIO.read(bais);
            targetImage = ImageIO.read(tais);
        }

        ImageComparison imageComparison = new ImageComparison(baseImage, targetImage);
        imageComparison.setPixelToleranceLevel(0.05);
        imageComparison.setAllowingPercentOfDifferentPixels(0.1);

        ImageComparisonResult result = imageComparison.compareImages();

        if (result.getImageComparisonState() == ImageComparisonState.MISMATCH || 
            result.getImageComparisonState() == ImageComparisonState.SIZE_MISMATCH) {
            
            Path diffDir = Paths.get("build/reports/visual-diffs");
            if (!Files.exists(diffDir)) {
                Files.createDirectories(diffDir);
            }
            File diffFile = diffDir.resolve(testName + "-diff.png").toFile();
            ImageIO.write(result.getResult(), "png", diffFile);

            Assertions.fail("Visual parity mismatch for " + testName + ". Diff saved to " + diffFile.getAbsolutePath() + " Differences: " + result.getDifferencePercent() + "%");
        }
    }

    private static void neutralizePage(Page page) {
        page.addStyleTag(new Page.AddStyleTagOptions().setContent(CSS_NEUTRALIZER));
        // Additional volatile element hiding can be added here
    }
}
