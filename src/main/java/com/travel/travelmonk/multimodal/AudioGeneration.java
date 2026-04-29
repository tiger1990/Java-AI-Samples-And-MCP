package com.travel.travelmonk.multimodal;

import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class AudioGeneration {

    private final OpenAiAudioSpeechModel openAiAudioSpeechModel;

    public AudioGeneration(OpenAiAudioSpeechModel openAiAudioSpeechModel){
        this.openAiAudioSpeechModel = openAiAudioSpeechModel;
    }

    @GetMapping("/speak")
    public ResponseEntity<byte[]> generateSpeech(
            @RequestParam(defaultValue ="Its great time to travel during summers") String text) throws IOException {

        var options = OpenAiAudioSpeechOptions.builder()
                .model("tts-1-hd")
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY)
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .speed(1.0)
                .build();

        TextToSpeechPrompt speechPrompt = new TextToSpeechPrompt(text, options);
        TextToSpeechResponse textToSpeechResponse = openAiAudioSpeechModel.call(speechPrompt);
        byte[] audioBytes = textToSpeechResponse.getResult().getOutput();

        Path dir = Paths.get("audio/generated");
        Files.createDirectories(dir);

        String fileName = "audio_" + Instant.now().toEpochMilli() + ".mp3";
        Path filePath = dir.resolve(fileName);
        Files.write(filePath, audioBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment: filename=\\speech.mp3\"")
                .body(audioBytes);
    }

    @GetMapping("/audio/play")
    public ResponseEntity<byte[]> playAudio(@RequestParam String fileName) throws IOException {
        Path filePath = Paths.get("audio/generated").resolve(fileName);
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }
        byte[] audioBytes = Files.readAllBytes(filePath);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(audioBytes);
    }

    @GetMapping("/audio/list")
    public List<String> listAudioFiles() throws IOException {
        Path dir = Paths.get("audio/generated");
        if (!Files.exists(dir)) {
            return List.of();
        }
        try (Stream<Path> files = Files.list(dir)) {
            return files
                    .filter(p -> p.toString().endsWith(".mp3"))
                    .map(p -> p.getFileName().toString())
                    .sorted()
                    .collect(Collectors.toList());
        }
    }
}
