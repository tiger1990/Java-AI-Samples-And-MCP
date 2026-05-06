package com.travel.travelmonk.rag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class RagConfiguration {

    public static final Logger log = LoggerFactory.getLogger(RagConfiguration.class);

    private final String vectorStoreName = "vectorStore.json";
    // Inject the path from application.yaml
//    @Value("${travelmonk.vectorstore.path}")
//    private String vectorStorePath;

    @Value("classpath:/data/models.json")
    private Resource models;

    @Bean
    SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel){
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        File vectorStoreFile = getVectorStoreFile();
        if(vectorStoreFile.exists()){
            log.info("Vector Store file already exist{}", vectorStoreFile.getAbsolutePath());
            simpleVectorStore.load(vectorStoreFile);
        }else{
            log.info("Creating Vector Store file{}", vectorStoreFile.getAbsolutePath());
            TextReader textReader = new TextReader(models);
            textReader.getCustomMetadata().put("fileName", "models.txt");
            List<Document> documents = textReader.get();


            TokenTextSplitter splitter = TokenTextSplitter.builder()
                    .withChunkSize(1000)
                    .withMinChunkSizeChars(350)
                    .withMinChunkLengthToEmbed(5)
                    .withMaxNumChunks(10000)
                    .withKeepSeparator(true)
                    .build();

            List<Document> splitDocuments = splitter.apply(documents);
            simpleVectorStore.add(splitDocuments);
            simpleVectorStore.save(vectorStoreFile);
        }
        return simpleVectorStore;
    }

//    private File getVectorStoreFile(){
//        Path path = Paths.get("src", "main", "resources", "data");
//        String absolutePath = path.toFile().getAbsolutePath() + "/" + vectorStoreName;
//        return new File(absolutePath);
//    }

    private File getVectorStoreFile() {
        // This forces the file to be created in your Documents/Java_AI_Samples/data folder
        // regardless of where Claude launches the JAR from.
        String userHome = System.getProperty("user.home");
        Path path = Paths.get(userHome, "Documents", "Java_AI_Samples", "data");

        File dir = path.toFile();
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            log.info("Creating directory {}: {}", dir.getAbsolutePath(), created);
        }

        return new File(dir, vectorStoreName);
    }
}
