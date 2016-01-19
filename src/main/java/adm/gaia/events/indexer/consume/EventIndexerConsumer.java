package adm.gaia.events.indexer.consume;

import adm.gaia.events.indexer.managers.EsManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tsadok on 27/11/2015.
 */
public class EventIndexerConsumer extends DefaultConsumer {

    final private static String RABBIT_TENANTID_PARAM_NAME = "GAIA_TENANTID";
    final private static String RABBIT_DATASOURCE_PARAM_NAME = "GAIA_DATASOURCE";
    final private static String RABBIT_DATATYPE_PARAM_NAME = "GAIA_DATATYPE";
    final private static String ES_TYPE_DEFAULT_NAME = "data";

    EsManager esManager;

    public EventIndexerConsumer(Channel channel, EsManager esManager) {
        super(channel);
        this.esManager = esManager;
    }

    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties props, byte[] body)
    {
        try {
            /*System.out.println(" [*] Channel " + getChannel().toString() +
                    Thread.currentThread().toString() + ", Received: " + body.length + " bytes, payload starts with: " + new String(body, "UTF-8")+"...");*/

            HttpPost httpPost = new HttpPost(esManager.getEsBaseUrl());

            byte[] esBulkBody = prepareEsBulkAPIPayload(envelope,body);
            httpPost.setEntity(new ByteArrayEntity(esBulkBody));
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
            System.err.println("Exception occurred while trying to send event to ElasticSearch.");
            e.printStackTrace();
            try {
                getChannel().basicNack(envelope.getDeliveryTag(), false, false);
            } catch (Exception e1) {
                System.err.println("Exception occurred while trying to send Nack to RabbitMQ due to any Exception");
                e1.printStackTrace();
            }
        }
    }

    //ES _bulk API composed of "{action json}\n{event json}\n"
    //More info here: https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-bulk.html
    private byte[] prepareEsBulkAPIPayload(Envelope envelope, byte[] rabbitBody) throws JsonProcessingException {
        byte[] esAction = getEsAction(envelope);

        byte[] lineSeparator = "\n".getBytes();
        int size = esAction.length + rabbitBody.length + (lineSeparator.length * 2);

        byte[] allTogether = new byte[size];
        ByteBuffer bf = ByteBuffer.wrap(allTogether);
        bf.put(esAction);
        bf.put(lineSeparator);
        bf.put(rabbitBody);
        bf.put(lineSeparator);

        return allTogether;
    }

    //Prepare the action json for ES _bulk API
    private byte[] getEsAction(Envelope envelope) throws JsonProcessingException {

        /*StringBuilder esIndexName = new StringBuilder();
        try {
            String tenantId = rabbitProps.getHeaders().get(RABBIT_TENANTID_PARAM_NAME).toString();
            String dataSource = rabbitProps.getHeaders().get(RABBIT_DATASOURCE_PARAM_NAME).toString();
            String dataType = rabbitProps.getHeaders().get(RABBIT_DATATYPE_PARAM_NAME).toString();

            esIndexName.append("gaia.").append(tenantId).append(".").
                    append(dataSource).append(".").append(dataType);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw new RuntimeException("Failed to read RabbitMQ Headers", ex);
        } */
        String indexName = envelope.getRoutingKey().replaceAll("event", "gaia");

        Map actionMap = new HashMap<String, String>();
        Map actionPropsMap = new HashMap<String, String>();
        actionPropsMap.put("_index", indexName);
        actionPropsMap.put("_type", ES_TYPE_DEFAULT_NAME);
        actionMap.put("index", actionPropsMap);
        ObjectMapper actionMapper = new ObjectMapper();
        return actionMapper.writeValueAsBytes(actionMap);
    }
}
