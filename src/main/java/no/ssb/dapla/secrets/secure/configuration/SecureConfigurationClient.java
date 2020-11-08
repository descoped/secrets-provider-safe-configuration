package no.ssb.dapla.secrets.secure.configuration;

import no.ssb.dapla.secrets.api.SecretManagerClient;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class SecureConfigurationClient implements SecretManagerClient {

    private final Map<String, byte[]> secureMap = new LinkedHashMap<>();
    private final AtomicBoolean closed = new AtomicBoolean();

    public SecureConfigurationClient(String propertyResourcePath) {
        Objects.requireNonNull(propertyResourcePath);
        Path resourcePath = Paths.get(propertyResourcePath);
        if (!Files.isReadable(resourcePath)) {
            throw new RuntimeException("The file " + propertyResourcePath + " is not readable");
        }
    }

    @Override
    public String readString(String secretName) {
        return readString(secretName, null);
    }

    @Override
    public String readString(String secretName, String secretVersion) {
        return secureMap.containsKey(secretName) ? new String(secureMap.get(secretName), StandardCharsets.UTF_8) : null;
    }

    @Override
    public byte[] readBytes(String secretName) {
        return readBytes(secretName, null);
    }

    @Override
    public byte[] readBytes(String secretName, String secretVersion) {
        return secureMap.get(secretName);
    }

    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            for (Map.Entry<String, byte[]> entry : secureMap.entrySet()) {
                Arrays.fill(entry.getValue(), (byte) 0);
            }
            secureMap.clear();
        }
    }
}
