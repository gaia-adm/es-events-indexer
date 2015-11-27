package adm.gaia.events.indexer.consume;

import adm.gaia.events.indexer.managers.EsManager;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by tsadok on 27/11/2015.
 */
public class EventIndexerConsumer extends DefaultConsumer {

    EsManager esManager;

    public EventIndexerConsumer(Channel channel, EsManager esManager) {
        super(channel);
        this.esManager = esManager;
    }

    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties props, byte[] body)
    {
        try {
            String s = new String(body, "UTF-8");
            byte [] subArray = Arrays.copyOfRange(body, 0, 30);
            System.out.println(" [*] Channel " + getChannel().toString() +
                    Thread.currentThread().toString() + ", Received: " + body.length + " bytes, payload starts with: " + new String(body, "UTF-8")+"...");

            HttpPost httpPost = new HttpPost(esManager.getEsBaseUrl());

            httpPost.setEntity(new ByteArrayEntity(body));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = esManager.getHttpClient().execute(httpPost);
            try {
                if (response.getStatusLine().getStatusCode() == 200) {
                    getChannel().basicAck(envelope.getDeliveryTag(), false);
                } else
                {
                    System.err.println("Failed to post to ElasticSearch: " + response.toString() + ", Sending Nack to RabbitMQ");
                    getChannel().basicNack(envelope.getDeliveryTag(), false, false);
                }

            } finally {
                response.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
            try {
                getChannel().basicNack(envelope.getDeliveryTag(), false, false);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
