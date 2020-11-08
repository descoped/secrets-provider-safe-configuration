package no.ssb.dapla.secrets.secure.configuration;

import no.ssb.dapla.secrets.api.SecretManagerClient;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

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
}
