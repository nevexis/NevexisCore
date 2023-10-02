package dev.nevah5.nevexis.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DiscordWebhook {

    private String content;
    private final Map<String, Object> embed = new HashMap<>();

    public DiscordWebhookBuilder builder() {
        return new DiscordWebhookBuilder();
    }

    public static class DiscordWebhookBuilder {

        private final DiscordWebhook discordWebhook;

        public DiscordWebhookBuilder() {
            this.discordWebhook = new DiscordWebhook();
        }

        public DiscordWebhookBuilder setContent(final String content) {
            this.discordWebhook.content = content;
            return this;
        }


        public DiscordWebhookBuilder setEmbedTitle(String title) {
            this.discordWebhook.embed.put("title", title);
            return this;
        }

        public DiscordWebhookBuilder setEmbedDescription(String description) {
            this.discordWebhook.embed.put("description", description);
            return this;
        }

        public DiscordWebhookBuilder setEmbedColor(int color) {
            this.discordWebhook.embed.put("color", color);
            return this;
        }

        public DiscordWebhook build() {
            return this.discordWebhook;
        }
    }

    public void execute(final String webhookUrl) {
        try {
            // Create a connection
            URL url = new URL(webhookUrl);
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
            if (responseCode != 204) {
                throw new Exception("Could not post Webhook.");
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
