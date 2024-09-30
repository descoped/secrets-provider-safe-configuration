import no.ssb.dapla.secrets.api.SecretManagerClientInitializer;

module secrets.provider.safe.configuration {

    requires io.descoped.service.provider.api;
    requires secrets.client.api;

    requires org.slf4j;

    provides SecretManagerClientInitializer with no.ssb.dapla.secrets.secure.configuration.SafeConfigurationClientInitializer;

    exports no.ssb.dapla.secrets.secure.configuration;

}
