package com.example.fintech.day2.ioandserialization;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class PortfolioSerializer {

    public record Asset(
        @JsonProperty("asset_id") String id,
        String currency,
        BigDecimal value,
        String type,
        Instant createdAt
    ) {}

    private final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .enable(SerializationFeature.INDENT_OUTPUT);

    public String toJson(List<Asset> assets) {
        try {
            return mapper.writeValueAsString(assets);
        } catch (IOException e) {
            throw new RuntimeException("Serialization failed", e);
        }
    }

    public List<Asset> fromJson(String json) {
        try {
            return mapper.readValue(json, new TypeReference<List<Asset>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Deserialization failed", e);
        }
    }

    public void writeToFile(Path path, String json) throws IOException {
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        Files.writeString(path, json);
    }

    public List<Asset> readFromFile(Path path) throws IOException {
        return fromJson(Files.readString(path));
    }

    public List<Asset> fromCsv(String csv) {
        String[] lines = csv.split("\n");
        return Arrays.stream(lines)
            .skip(1)  // skip header
            .filter(line -> !line.isBlank())
            .map(line -> {
                String[] parts = line.split(",");
                if (parts.length != 4) {
                    throw new IllegalArgumentException(
                        "Expected 4 fields, got %d: %s".formatted(parts.length, line));
                }
                try {
                    return new Asset(
                        parts[0].trim(),
                        parts[1].trim(),
                        new BigDecimal(parts[2].trim()),
                        parts[3].trim(),
                        Instant.EPOCH
                    );
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid decimal value: " + parts[2], e);
                }
            })
            .toList();
    }
}
