package no.ssb.dapla.secrets.secure.configuration;

import io.descoped.service.provider.api.ProviderName;
import no.ssb.dapla.secrets.api.SecretManagerClient;
import no.ssb.dapla.secrets.api.SecretManagerClientInitializer;

import java.util.Map;
import java.util.Set;

@ProviderName("safe-configuration")
public class SafeConfigurationClientInitializer implements SecretManagerClientInitializer {

    @Override
    public String providerId() {
        return "safe-configuration";
    }

    @Override
    public Set<String> configurationKeys() {
        return Set.of("secrets.property-resource-path");
    }

    @Override
    public SecretManagerClient initialize(Map<String, String> configuration) {
        String propertyResourcePath = configuration.get("secrets.property-resource-path");
        return new SafeConfigurationClient(propertyResourcePath);
    }
}
