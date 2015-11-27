package adm.gaia.events.indexer.conf;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

/**
 * Created by tsadok on 27/11/2015.
 */
public class EventsIndexerConfiguration extends Configuration{

    @JsonProperty("rabbitmq")
    private RabbitmqConfiguration rabbitmqConfiguration = new RabbitmqConfiguration();

    @JsonProperty("es")
    private EsConfiguration esConfiguration = new EsConfiguration();


    public RabbitmqConfiguration getRabbitmqConfiguration() {
        return rabbitmqConfiguration;
    }

    public void setRabbitmqConfiguration(RabbitmqConfiguration rabbitmqConfiguration) {
        this.rabbitmqConfiguration = rabbitmqConfiguration;
    }


    public EsConfiguration getEsConfiguration() {
        return esConfiguration;
    }

    public void setEsConfiguration(EsConfiguration esConfiguration) {
        this.esConfiguration = esConfiguration;
    }
}