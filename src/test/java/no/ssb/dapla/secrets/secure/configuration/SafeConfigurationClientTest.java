package no.ssb.dapla.secrets.secure.configuration;

import no.ssb.dapla.secrets.api.SecretManagerClient;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SafeConfigurationClientTest {

    @Test
    public void readSecrets() {
        Map<String, String> providerConfiguration = Map.of(
                "secrets.provider", "safe-configuration",
                "secrets.propertyResourcePath", Paths.get(".").toAbsolutePath().normalize().resolve(Paths.get("src/test/resources/secret.properties")).toString()
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
                "secrets.propertyResourcePath", Paths.get(".").toAbsolutePath().normalize().resolve(Paths.get("src/test/resources/secret-dos.properties")).toString()
        );

        try (SecretManagerClient client = SecretManagerClient.create(providerConfiguration)) {
            assertArrayEquals("password1".getBytes(), client.readBytes("secret1"));
            assertArrayEquals("password2".getBytes(), client.readBytes("secret2"));
            assertArrayEquals("key=value".getBytes(), client.readBytes("secret3"));
        }
    }

    @Test
    public void writeThenReadSecret() {
        Map<String, String> providerConfiguration = Map.of(
                "secrets.provider", "safe-configuration",
                "secrets.propertyResourcePath", Paths.get(".").toAbsolutePath().normalize().resolve(Paths.get("src/test/resources/secret-dos.properties")).toString()
        );

        try (SecretManagerClient client = SecretManagerClient.create(providerConfiguration)) {
            assertEquals("latest", client.addVersion("question", "42".getBytes(StandardCharsets.UTF_8)));
            assertEquals("42", new String(client.readBytes("question"), StandardCharsets.UTF_8));
            assertEquals("latest", client.addVersion("question", "43".getBytes(StandardCharsets.UTF_8)));
            assertEquals("43", new String(client.readBytes("question"), StandardCharsets.UTF_8));
        }
    }
}
