package dev.nevah5.nevexis.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.nevah5.nevexis.NevexisCore;
import org.bukkit.Bukkit;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DiscordWebhook {

    private final Map<String, Object> embed = new HashMap<>();
    private String content;


    public void execute(final NevexisCore plugin) {
        if (!plugin.isACTIVITY_ENABLED()) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            executeWebhook(plugin);
        });
    }

    public void executeSync(final NevexisCore plugin) {
        if (!plugin.isACTIVITY_ENABLED()) {
            return;
        }
        executeWebhook(plugin);
    }

    private void executeWebhook(final NevexisCore plugin) {
        try {
            // Create a connection
            URL url = new URL(plugin.getACTIVITY_WEBHOOK_URL());
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

    public DiscordWebhookBuilder builder() {
        return new DiscordWebhookBuilder(this);
    }

    public static class DiscordWebhookBuilder {

        private final DiscordWebhook discordWebhook;

        public DiscordWebhookBuilder(final DiscordWebhook discordWebhook) {
            this.discordWebhook = discordWebhook;
        }

        public DiscordWebhookBuilder setContent(final String content) {
            this.discordWebhook.content = content;
            return this;
        }

        public DiscordWebhookBuilder setEmbedTitle(final String title) {
            this.discordWebhook.embed.put("title", title);
            return this;
        }

        public DiscordWebhookBuilder setEmbedDescription(final String description) {
            this.discordWebhook.embed.put("description", description);
            return this;
        }

        public DiscordWebhookBuilder setEmbedUrl(final String url) {
            this.discordWebhook.embed.put("url", url);
            return this;
        }

        public DiscordWebhookBuilder setEmbedTimestamp() {
            this.discordWebhook.embed.put("timestamp", DateTimeFormatter.ISO_INSTANT.format(Instant.now()));
            return this;
        }

        public DiscordWebhookBuilder setEmbedColor(final int color) {
            this.discordWebhook.embed.put("color", color);
            return this;
        }

        public DiscordWebhookBuilder setEmbedFooter(final String text, final String url) {
            final Map<String, String> footer = new HashMap<>();
            footer.put("text", text);
            footer.put("icon_url", url);
            this.discordWebhook.embed.put("footer", footer);
            return this;
        }

        public DiscordWebhookBuilder setEmbedImage(final String url, final Integer height, final Integer width) {
            final Map<String, Object> image = new HashMap<>();
            image.put("url", url);
            image.put("height", height);
            image.put("width", width);
            this.discordWebhook.embed.put("image", image);
            return this;
        }

        public DiscordWebhookBuilder setEmbedThumbnail(final String url, final Integer height, final Integer width) {
            final Map<String, Object> thumbnail = new HashMap<>();
            thumbnail.put("url", url);
            thumbnail.put("height", height);
            thumbnail.put("width", width);
            this.discordWebhook.embed.put("thumbnail", thumbnail);
            return this;
        }

        public DiscordWebhookBuilder setEmbedAuthor(final String name, final String url, final String icon_url) {
            final Map<String, String> author = new HashMap<>();
            author.put("name", name);
            author.put("url", url);
            author.put("icon_url", icon_url);
            this.discordWebhook.embed.put("author", author);
            return this;
        }

        public DiscordWebhookBuilder addEmbedField(final String name, final String value, final Boolean isInline) {
            final Map<String, Object> field = new HashMap<>();
            field.put("name", name);
            field.put("value", value);
            field.put("inline", isInline);
            List<Map<String, Object>> fields = (List<Map<String, Object>>) this.discordWebhook.embed.get("fields");
            if (fields == null) {
                fields = new ArrayList<>();
            }
            fields.add(field);
            this.discordWebhook.embed.put("fields", fields);
            return this;
        }

        public DiscordWebhook build() {
            return this.discordWebhook;
        }
    }
}
