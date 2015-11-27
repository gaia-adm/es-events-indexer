package adm.gaia.events.indexer.consume;

import adm.gaia.events.indexer.managers.EsManager;
import adm.gaia.events.indexer.managers.RabbitmqManager;
import com.rabbitmq.client.Channel;

/**
 * Created by tsadok on 27/11/2015.
 */
public class ConsumersRegistrar {

    RabbitmqManager rabbitmqManager;
    EsManager esManager;

    public ConsumersRegistrar(RabbitmqManager rabbitmqManager, EsManager esManager) {
        this.esManager = esManager;
        this.rabbitmqManager = rabbitmqManager;
    }

    /*
            Create consumers according to the number of processors available for the JVM
            DefaultConsumer in RabbitMQ is running on a thread coming for a ThreadPool
            RabbitMQ is managing the ThreadPool
            Also we allocate channel per consumer (This is the best practice according to RabbitMQ docs to avoid multi threading problems)

        */
    public void register() throws Exception
    {
            for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
                Channel consumerChannel = rabbitmqManager.getConnection().createChannel();
                consumerChannel.basicConsume(rabbitmqManager.getRabbitmqConf().getQueueName(), false,
                        new EventIndexerConsumer(consumerChannel, esManager));
            }
    }
}
