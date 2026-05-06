package com.travel.travelmonk.mcp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Conference(String name, int year, String[] dates, String location, List<Session> sessions) {
}

