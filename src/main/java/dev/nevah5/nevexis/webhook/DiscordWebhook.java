package dev.nevah5.nevexis.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DiscordWebhook {

    private final String webhookUrl;
    private String content;
    private final Map<String, Object> embed = new HashMap<>();

    public DiscordWebhook(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public DiscordWebhook setContent(String content) {
        this.content = content;
        return this;
    }

    public DiscordWebhook setEmbedTitle(String title) {
        embed.put("title", title);
        return this;
    }

    public DiscordWebhook setEmbedDescription(String description) {
        embed.put("description", description);
        return this;
    }

    public DiscordWebhook setEmbedColor(int color) {
        embed.put("color", color);
        return this;
    }

    public void execute() throws Exception {
        // Create a connection
        URL url = new URL(this.webhookUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);

        // Prepare the payload using ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> payload = new HashMap<>();
        if (this.content != null) {
            payload.put("content", this.content);
        }
        if (!this.embed.isEmpty()) {
            payload.put("embeds", Collections.singletonList(this.embed));
        }

        // Serialize the payload to JSON
        byte[] jsonData = objectMapper.writeValueAsBytes(payload);

        // Write the JSON payload to the output stream
        try (OutputStream os = connection.getOutputStream()) {
            os.write(jsonData, 0, jsonData.length);
        }

        // Get the response code (should be 204 if successful)
        int responseCode = connection.getResponseCode();
        System.out.println("POST Response Code: " + responseCode);
    }
}
