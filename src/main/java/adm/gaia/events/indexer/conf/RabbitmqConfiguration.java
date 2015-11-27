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
     * The server-side consumer's queue that provides point-to-point semantics for stock requests.
     */
    @JsonProperty
    private String queueName = "es-events-indexer";

    /**
     * Key that clients will use to send to send events
     * Should be the same name as the queue since we use default RabbitMQ binding and expect the producers to
     * use the default exchange ("") that by default bind the routingKey to the queue with the same name
     */
    @JsonProperty
    private String routingKey = queueName;

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
}
