package com.travel.travelmonk.multimodal;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Map;

@RestController
public class ImageGeneration {

    public final OpenAiImageModel imageModel;

    public ImageGeneration(OpenAiImageModel imageModel) {
        this.imageModel = imageModel;
    }

    @GetMapping("/generate-image")
    public ResponseEntity<Map<String, String>> generateImage(
            @RequestParam(defaultValue = "A beautiful sunset over mountains") String prompt) throws IOException {

        ImageOptions imageOptions = OpenAiImageOptions.builder()
                .model("dall-e-3")
                .width(1024)
                .height(1024)
                .quality("hd")
                .style("vivid")
                .build();

        ImagePrompt imagePrompt = new ImagePrompt(prompt, imageOptions);
        ImageResponse imageResponse = imageModel.call(imagePrompt);

        String imageUrl = imageResponse.getResult().getOutput().getUrl();

        Path dir = Paths.get("images/generated");
        Files.createDirectories(dir);

        String fileName = "image_" + Instant.now().toEpochMilli() + ".png";
        Path filePath = dir.resolve(fileName);

        try (InputStream in = URI.create(imageUrl).toURL().openStream()) {
            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        System.out.println("Image saved to: " + filePath.toAbsolutePath());

        return ResponseEntity.ok(Map.of(
                "prompt", prompt,
                "imageUrl", imageUrl
        ));
    }
}

