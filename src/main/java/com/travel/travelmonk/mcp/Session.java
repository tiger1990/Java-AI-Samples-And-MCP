package com.travel.travelmonk.mcp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Session(String day, String time, String title, String type, String[] speakers, String room){

}
