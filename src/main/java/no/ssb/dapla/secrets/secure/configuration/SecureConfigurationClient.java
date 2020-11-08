package no.ssb.dapla.secrets.secure.configuration;

import no.ssb.dapla.secrets.api.SecretManagerClient;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
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
        loadResource(resourcePath);
    }

    private void loadResource(Path resourcePath) {
        byte[] bytes = new byte[0];
        char[] chars = new char[0];
        try {
            bytes = Files.readAllBytes(resourcePath);
            CharBuffer buffer = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(bytes));
            chars = new char[buffer.remaining()];
            buffer.get(chars);
            buffer.clear();

            int startPos = 0;
            int equalPos = -1;
            int newLinePos;

            for (int n = 0; n < chars.length; n++) {
                if (chars[n] == '=') {
                    equalPos = n;

                } else if (chars[n] == '\n') {
                    newLinePos = n;

                    // check if next char is \r
                    if (n + 1 < chars.length && chars[n + 1] == '\r') {
                        n++;
                    }

                    // split line
                    if (equalPos == -1) {
                        throw new RuntimeException("Line did not contain '='!");
                    }

                    char[] value = new char[0];
                    CharBuffer valueBuffer = null;
                    ByteBuffer valueByteBuffer = null;
                    try {
                        String key = new String(chars, startPos, equalPos - startPos);
                        value = Arrays.copyOfRange(chars, equalPos + 1, newLinePos);
                        valueBuffer = CharBuffer.wrap(value);
                        valueByteBuffer = StandardCharsets.UTF_8.encode(valueBuffer);
                        byte[] valueBytes = new byte[valueByteBuffer.remaining()];
                        valueByteBuffer.get(valueBytes);
                        secureMap.put(key, valueBytes);
                    } finally {
                        if (valueBuffer != null) valueBuffer.clear();
                        if (valueByteBuffer != null) valueByteBuffer.clear();
                        Arrays.fill(value, (char) 0);
                    }

                    startPos = newLinePos + 1;
                    equalPos = -1;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            Arrays.fill(bytes, (byte) 0);
            Arrays.fill(chars, (char) 0);
        }
    }

    @Override
    public String readString(String secretName) {
        return readString(secretName, null);
    }

    @Override
    public String readString(String secretName, String secretVersion) {
        if (closed.get()) {
            throw new IllegalStateException("Client is closed!");
        }
        return secureMap.containsKey(secretName) ? new String(secureMap.get(secretName), StandardCharsets.UTF_8) : null;
    }

    @Override
    public byte[] readBytes(String secretName) {
        return readBytes(secretName, null);
    }

    @Override
    public byte[] readBytes(String secretName, String secretVersion) {
        if (closed.get()) {
            throw new IllegalStateException("Client is closed!");
        }
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
