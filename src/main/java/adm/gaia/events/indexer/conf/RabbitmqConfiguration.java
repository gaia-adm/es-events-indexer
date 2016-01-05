package adm.gaia.events.indexer.conf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rabbitmq.client.ConnectionFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Created by tsadok on 27/11/2015.
 */
public class RabbitmqConfiguration {

    /**
     * The queue name dedicated for es-events-indexer
     */
    @JsonProperty
    private String queueName = "es-events-indexer";

    /**
     * Need to be the same name as the producers are using
     */
    @JsonProperty
    private String exchangeName = "events-to-index";

    /**
     * We use topic exchange, hence we can bind according to dynamic definition like "#.xxx"
     * # - means zero or more words that can come before.
     */
    @JsonProperty
    private String routingKey = "#.event";

    @JsonProperty
    private String host = "rabbitmq";//"192.168.59.103";

    @Min(1)
    @Max(65535)
    @JsonProperty
    private int port = ConnectionFactory.DEFAULT_AMQP_PORT;

    @JsonProperty
    private String username = ConnectionFactory.DEFAULT_USER;

    @JsonProperty
    private String password = ConnectionFactory.DEFAULT_PASS;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public String getQueueName() {
        return queueName;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public String getExchangeName() {
        return exchangeName;
    }
}
