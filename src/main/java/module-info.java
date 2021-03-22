import no.ssb.dapla.secrets.api.SecretManagerClientInitializer;
import no.ssb.dapla.secrets.secure.configuration.SafeConfigurationClientInitializer;

module dapla.secrets.provider.safe.configuration {

    requires no.ssb.service.provider.api;
    requires dapla.secrets.client.api;

    requires org.slf4j;

    provides SecretManagerClientInitializer with SafeConfigurationClientInitializer;

    exports no.ssb.dapla.secrets.secure.configuration;

}
