import io.descoped.secrets.api.SecretManagerClientInitializer;

module secrets.provider.safe.configuration {

    requires io.descoped.service.provider.api;
    requires secrets.client.api;

    requires org.slf4j;

    provides SecretManagerClientInitializer with io.descoped.secrets.secure.configuration.SafeConfigurationClientInitializer;

    exports io.descoped.secrets.secure.configuration;

}
