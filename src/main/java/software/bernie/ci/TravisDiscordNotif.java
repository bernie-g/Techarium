package software.bernie.ci;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import lombok.SneakyThrows;

import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class TravisDiscordNotif {

    @SneakyThrows
    public static void main(String[] args) {
        Map<String, String> env = System.getenv();
        String commit_message = env.get("COMMIT_MESSAGE");
        String author_name = env.get("AUTHOR_NAME");
        String branch_name = env.get("TRAVIS_BRANCH");
        String webhook_url = env.get("DISCORD_WEBHOOK_URL");
        String travis_pull_request = env.get("TRAVIS_PULL_REQUEST");
        Objects.requireNonNull(commit_message);
        Objects.requireNonNull(author_name);
        Objects.requireNonNull(branch_name);
        Objects.requireNonNull(travis_pull_request);

        boolean isPullRequest = !travis_pull_request.equals("false");
        notifyDiscord(webhook_url, commit_message, author_name, branch_name, isPullRequest);
    }

    private static void notifyDiscord(String webhook_url, String commit_message, String author_name, String branch_name, boolean isPullRequest) {
        String buildPing = "<@&853419684329816074>";
        String description = String
                .format("**Branch:** %s\n\n**Update Details**:```%s```", branch_name, commit_message);
        String footer = String.format("Update Author: %s", author_name);
        if (!isPullRequest && (branch_name.equals("master") || branch_name.equals("feature/CI"))) {
            WebhookClient.withUrl(webhook_url).send(
                    new WebhookMessageBuilder()
                            .setContent(buildPing)
                            .addEmbeds(
                                    new WebhookEmbedBuilder()
                                            .setColor(9193502)
                                            .setDescription(description)
                                            .setFooter(new WebhookEmbed.EmbedFooter(footer, null))
                                            .build())
                            .build())
                    .thenApply((future) -> uploadJar(webhook_url));
        }
    }

    private static CompletableFuture<Void> uploadJar(String webhook_url) {
        return WebhookClient.withUrl(webhook_url).send(new File("./build/libs/techarium-1.0.jar"))
                .thenRun(() -> System.exit(0));
    }
}
