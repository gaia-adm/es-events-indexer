package adm.gaia.events.indexer.managers;

import adm.gaia.events.indexer.conf.EsConfiguration;
import adm.gaia.events.indexer.conf.EventsIndexerConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Environment;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;


import javax.ws.rs.client.Client;

/**
 * Created by tsadok on 27/11/2015.
 */
public class EsManager implements Managed {

    EventsIndexerConfiguration configuration;
    String esBaseUrl;
    Environment environment;
    CloseableHttpClient httpClient;


    public EsManager(EventsIndexerConfiguration eventsIndexerConfiguration, Environment environment) {
        this.configuration = eventsIndexerConfiguration;
        this.environment = environment;

        EsConfiguration conf = eventsIndexerConfiguration.getEsConfiguration();
        StringBuilder baseBuilder = new StringBuilder();

        esBaseUrl = baseBuilder.append(conf.getProtocol()).append("://").append(conf.getHost()).append(":").
                append(conf.getPort()).append("/_bulk").toString();
    }



    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public String getEsBaseUrl() {
        return esBaseUrl;
    }

    @Override
    public void start() throws Exception {

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();
    }

    @Override
    public void stop() throws Exception {
        if (httpClient != null)
            httpClient.close();
    }
}
