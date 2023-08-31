package streams_files_dirs.exercises.solutions;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static javax.imageio.ImageWriteParam.MODE_DISABLED;
import static javax.imageio.ImageWriteParam.MODE_EXPLICIT;

public class CopyImage {
    public static void main(String[] args) {
        Path input = Path.of("src/streams_files_dirs/exercises/resources/denis-yUvKrvfu0C4-unsplash.jpg");
        Path output = Path.of("src/streams_files_dirs/exercises/resources/copy.jpg");


        try {
            //Simple way to copy file
            //I prefer it because it's faster
            Files.copy(input, output, REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        //Another way to copy images, ImageIO provides a lot of utility methods related to image processing
//        //This way the copy looses a percent of the original size due to compression
//        //It can be set manually
//
//        ImageWriter writer = null;
//
//        try (ImageOutputStream out = ImageIO.createImageOutputStream(output.toFile())) {
//            BufferedImage image = ImageIO.read(input.toFile());
//
//            writer = ImageIO.getImageWritersByFormatName("jpg").next();
//
//            ImageWriteParam jpgWriteParam = writer.getDefaultWriteParam();
//            jpgWriteParam.setCompressionMode(MODE_EXPLICIT);
//            jpgWriteParam.setCompressionQuality(0.9f);
//
//            writer.setOutput(out);
//            writer.write(null, new IIOImage(image, null, null), jpgWriteParam);
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (writer != null) {
//                writer.dispose();
//            }
//        }
    }
}