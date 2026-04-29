package com.travel.travelmonk.mcp;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class SessionTools {

    private static final Logger logger = LoggerFactory.getLogger(SessionTools.class);
    private List<Session> sessions = new ArrayList<>();

    private final ObjectMapper objectMapper;

    public SessionTools(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Tool(name = "spring_io_sessions", description = "Get sessions for Spring IO")
    public List<Session> findAllSessions() {
        return sessions;
    }

    @PostConstruct
    public void init() {
        logger.info("SessionTools init loading spring sessions");
        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/sessions.json")) {
            var conference = objectMapper.readValue(inputStream, Conference.class);
            this.sessions = conference.sessions();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load sessions from JSON");
        }
    }
}
