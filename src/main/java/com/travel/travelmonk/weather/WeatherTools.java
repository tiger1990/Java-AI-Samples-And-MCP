package com.travel.travelmonk.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class WeatherTools {

    private static final String BASE_URL = "https://api.weather.gov";

    private final RestClient restClient;

    public WeatherTools(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl(BASE_URL)
                .defaultHeader("Accept", "application/geo+json")
                .defaultHeader("User-Agent", "WeatherApiClient/1.0 (deepak@email.com)")
                .build();
    }

    // 🌦️ Public method: Get forecast summary by lat/lon
    @Tool(description = "Get weather forecast by specific lat/long")
    public String getForecast(double latitude, double longitude) {

        // Step 1: Get forecast URL from points API
        Points points = restClient.get()
                .uri("/points/{lat},{lon}", latitude, longitude)
                .retrieve()
                .body(Points.class);

        if (points == null || points.properties() == null) {
            return "Unable to fetch forecast metadata";
        }

        String forecastUrl = points.properties().forecast();

        // Step 2: Call forecast endpoint
        Forecast forecast = restClient.get()
                .uri(forecastUrl)
                .retrieve()
                .body(Forecast.class);

        if (forecast == null ||
                forecast.properties() == null ||
                forecast.properties().periods() == null ||
                forecast.properties().periods().isEmpty()) {
            return "No forecast available";
        }

        Period today = forecast.properties().periods().get(0);

        return String.format(
                "Forecast: %s, %d%s, %s",
                today.shortForecast(),
                today.temperature(),
                today.temperatureUnit(),
                today.detailedForecast()
        );
    }

    // ================= DTOs =================

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Points(@JsonProperty("properties") PointsProperties properties
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record PointsProperties(
                @JsonProperty("forecast") String forecast
        ) {}
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Forecast(
            @JsonProperty("properties") ForecastProperties properties
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record ForecastProperties(
                @JsonProperty("periods") List<Period> periods
        ) {}
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Period(
            @JsonProperty("name") String name,
            @JsonProperty("temperature") int temperature,
            @JsonProperty("temperatureUnit") String temperatureUnit,
            @JsonProperty("shortForecast") String shortForecast,
            @JsonProperty("detailedForecast") String detailedForecast
    ) {}

    @Tool(description = "Get weather alert for US state, input is two letter US state code like NY, CA")
    public String getActiveAlerts(@ToolParam(description = "Two letter US state code CA, NY") String state) {

        AlertsResponse response = restClient.get()
                .uri("/alerts/active/area/{state}", state)
                .retrieve()
                .body(AlertsResponse.class);

        if (response == null || response.features() == null || response.features().isEmpty()) {
            return "No active weather alerts for " + state;
        }

        StringBuilder result = new StringBuilder();

        for (Feature f : response.features()) {
            var p = f.properties();

            result.append("⚠️")
                    .append(p.headline()).append("\n")
                    .append("Severity: ").append(p.severity()).append("\n")
                    .append("Area: ").append(p.areaDesc()).append("\n")
                    .append("Details: ").append(p.description()).append("\n")
                    .append("Advice: ").append(p.instruction()).append("\n\n");
        }

        return result.toString();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AlertsResponse(
            @JsonProperty("features") List<Feature> features
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Feature(
            @JsonProperty("properties") AlertProperties properties
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AlertProperties(
            @JsonProperty("headline") String headline,
            @JsonProperty("severity") String severity,
            @JsonProperty("areaDesc") String areaDesc,
            @JsonProperty("description") String description,
            @JsonProperty("instruction") String instruction
    ) {}
}