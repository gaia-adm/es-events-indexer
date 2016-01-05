package adm.gaia.events.indexer.managers;

import adm.gaia.events.indexer.conf.EventsIndexerConfiguration;
import adm.gaia.events.indexer.conf.RabbitmqConfiguration;
import com.rabbitmq.client.*;
import io.dropwizard.lifecycle.Managed;

import java.util.HashMap;
import java.util.Map;

/**
 * Taking care for the setup and tire down of RabbitMQ connections
 *
 * Created by tsadok on 27/11/2015.
 */
public class RabbitmqManager implements Managed {

    private Connection connection;
    RabbitmqConfiguration rabbitmqConf;

    public RabbitmqManager(EventsIndexerConfiguration configuration) {
        this.rabbitmqConf = configuration.getRabbitmqConfiguration();
    }

    public Connection getConnection() {
        return connection;
    }

    public RabbitmqConfiguration getRabbitmqConf() {
        return rabbitmqConf;
    }

    @Override
    public void start() throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitmqConf.getHost());
        factory.setPort(rabbitmqConf.getPort());
        factory.setUsername(rabbitmqConf.getUsername());
        factory.setPassword(rabbitmqConf.getPassword());

        factory.setAutomaticRecoveryEnabled(true); // connection that will recover automatically
        factory.setNetworkRecoveryInterval(10000); // attempt recovery to the max of 10 seconds

        factory.setRequestedHeartbeat(30); // Setting heartbeat to 30 sec instead the default of 10 min
                                           // that way the client will know that server is unreachable
                                           // and will try to reconnect
                                           // The same is true for the server (the broker) - after 30 sec
                                           // he will close the consumers.

        connection = factory.newConnection();

        Channel defineChannel = connection.createChannel();
        defineChannel.exchangeDeclare(rabbitmqConf.getExchangeName(), "topic", true);
        defineChannel.queueDeclare(rabbitmqConf.getQueueName(), true, false, false, null);
        defineChannel.queueBind(rabbitmqConf.getQueueName(), rabbitmqConf.getExchangeName(), rabbitmqConf.getRoutingKey());
    }

    @Override
    public void stop() throws Exception {

        // gracefully shutting down RabbitMQ connections
        // (Dropwizard will call the stop method when it shutting down)
        if (connection != null)
            connection.close();
    }
}
