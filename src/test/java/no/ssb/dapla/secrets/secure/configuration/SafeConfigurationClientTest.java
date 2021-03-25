package no.ssb.dapla.secrets.secure.configuration;

import no.ssb.dapla.secrets.api.SecretManagerClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SafeConfigurationClientTest {

    @Test
    public void readSecrets() {
        Map<String, String> providerConfiguration = Map.of(
                "secrets.provider", "safe-configuration",
                "secrets.property-resource-path", Paths.get(".").toAbsolutePath().normalize().resolve(Paths.get("src/test/resources/secret.properties")).toString()
        );

        try (SecretManagerClient client = SecretManagerClient.create(providerConfiguration)) {
            assertArrayEquals("password1".getBytes(), client.readBytes("secret1"));
            assertArrayEquals("password2".getBytes(), client.readBytes("secret2"));
            assertArrayEquals("key=value".getBytes(), client.readBytes("secret3"));
        }
    }

    @Test
    public void readDosFileSecrets() {
        Map<String, String> providerConfiguration = Map.of(
                "secrets.provider", "safe-configuration",
                "secrets.property-resource-path", Paths.get(".").toAbsolutePath().normalize().resolve(Paths.get("src/test/resources/secret-dos.properties")).toString()
        );

        try (SecretManagerClient client = SecretManagerClient.create(providerConfiguration)) {
            assertArrayEquals("password1".getBytes(), client.readBytes("secret1"));
            assertArrayEquals("password2".getBytes(), client.readBytes("secret2"));
            assertArrayEquals("key=value".getBytes(), client.readBytes("secret3"));
        }
    }

    @Test
    public void writeThenReadSecret() throws IOException {
        Files.copy(Path.of("src/test/resources/secret.properties"), Path.of("/tmp/application-secret.properties"), StandardCopyOption.REPLACE_EXISTING);

        Map<String, String> providerConfiguration = Map.of(
                "secrets.provider", "safe-configuration",
                "secrets.property-resource-path", "/tmp/application-secret.properties"
        );

        try (SecretManagerClient client = SecretManagerClient.create(providerConfiguration)) {
            assertEquals("latest", client.addVersion("question", "42".getBytes(StandardCharsets.UTF_8)));
            assertEquals("42", new String(client.readBytes("question"), StandardCharsets.UTF_8));
            assertEquals("latest", client.addVersion("question", "43".getBytes(StandardCharsets.UTF_8)));
            assertEquals("43", new String(client.readBytes("question"), StandardCharsets.UTF_8));
        }
    }
}
