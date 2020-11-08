package no.ssb.dapla.secrets.secure.configuration;

import no.ssb.dapla.secrets.api.SecretManagerClient;
import no.ssb.dapla.secrets.api.SecretManagerClientInitializer;
import no.ssb.service.provider.api.ProviderName;

import java.util.Map;
import java.util.Set;

@ProviderName("secure-configuration")
public class SecureConfigurationClientInitializer implements SecretManagerClientInitializer {

    @Override
    public String providerId() {
        return "secure-configuration";
    }

    @Override
    public Set<String> configurationKeys() {
        return Set.of("secrets.propertyResourcePath");
    }

    @Override
    public SecretManagerClient initialize(Map<String, String> configuration) {
        String propertyResourcePath = configuration.get("secrets.propertyResourcePath");
        return new SecureConfigurationClient(propertyResourcePath);
    }
}
