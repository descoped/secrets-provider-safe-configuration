package no.ssb.dapla.secrets.secure.configuration;

import no.ssb.dapla.secrets.api.SecretManagerClient;
import no.ssb.dapla.secrets.api.SecretManagerClientInitializer;
import no.ssb.service.provider.api.ProviderName;

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
        return Set.of("secrets.propertyResourcePath");
    }

    @Override
    public SecretManagerClient initialize(Map<String, String> configuration) {
        String propertyResourcePath = configuration.get("secrets.propertyResourcePath");
        return new SafeConfigurationClient(propertyResourcePath);
    }
}
