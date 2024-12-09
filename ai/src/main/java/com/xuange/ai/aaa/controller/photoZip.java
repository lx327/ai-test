package com.xuange.ai.aaa.controller;

import com.xuange.ai.aaa.common.Tread;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.*;
import java.util.function.Supplier;
//xuange
@RestController
@CrossOrigin
@Component
@RequestMapping("phoneZip")
public class photoZip {
    @GetMapping("zipPhoto")
    public ResponseEntity uploadAndCompressImage(@RequestBody MultipartFile file) {
        ThreadPoolExecutor treadPool = Tread.getTreadPool(10, 20, 2, TimeUnit.MINUTES, new LinkedBlockingQueue<>(), null);
        CompletableFuture<ResponseEntity> uploads = CompletableFuture.supplyAsync(new Supplier<ResponseEntity>() {
            @Override
            public ResponseEntity get() {
                if (file.isEmpty()) {
                    return ResponseEntity.badRequest().body("No file uploaded.");
                }

                try {
                    // Read the image from the uploaded file
                    BufferedImage originalImage = ImageIO.read(file.getInputStream());


                    // Specify the directory to save the compressed image
                    File dir = new File("uploads");
                    if (!dir.exists()) {
                        dir.mkdirs(); // Create the directory if it doesn't exist
                    }

                    // Create a compressed image file
                    File compressedImageFile = new File(dir, "compressed_" + file.getOriginalFilename());
//            File file1 = new File(dir, file.getOriginalFilename());
                    compressImage(originalImage, compressedImageFile, 0.5f); // Set quality to 0.5

                    return ResponseEntity.ok("Image uploaded and compressed successfully: " + compressedImageFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                    return ResponseEntity.status(500).body("Failed to upload and compress image.");
                }

            }
        }, treadPool);

        try {
            return uploads.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void compressImage(BufferedImage originalImage, File compressedImageFile, float quality) throws IOException {
        // Get a writer for the JPEG format
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter writer = writers.next();

        // Set up the output stream
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(compressedImageFile)) {
            writer.setOutput(ios);

            // Set the compression quality
            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality); // 0.0 to 1.0
            }

            // Write the compressed image
            writer.write(null, new javax.imageio.IIOImage(originalImage, null, null), param);
        } finally {
            writer.dispose();
        }
    }


}
