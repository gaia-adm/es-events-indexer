package adm.gaia.events.indexer;

import adm.gaia.events.indexer.conf.EventsIndexerConfiguration;
import adm.gaia.events.indexer.consume.ConsumersRegistrar;
import adm.gaia.events.indexer.managers.EsManager;
import adm.gaia.events.indexer.managers.RabbitmqManager;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by tsadok on 27/11/2015.
 */
public class EventsIndexerApplication extends Application<EventsIndexerConfiguration>{


    public static void main(String[] args) throws Exception {
        new EventsIndexerApplication().run(args);
    }

    @Override
    public String getName() {
        return "adm-gaia-es-events-indexer";
    }

    @Override
    public void initialize(Bootstrap<EventsIndexerConfiguration> bootstrap) {

    }

    @Override
    public void run(EventsIndexerConfiguration configuration,
                    Environment environment)  throws Exception
    {

        RabbitmqManager rabbitmqManager = new RabbitmqManager(configuration);

        //For some reason Dropwizard do not call "stop" on shutting down.
        //Also it is not clear when "start" is being called.
        //For now we do it by ourselves instead of let Dropwizard do it
        rabbitmqManager.start();
        //environment.lifecycle().manage(rabbitmqManager);

        EsManager esManager = new EsManager(configuration, environment);
        esManager.start();
        //environment.lifecycle().manage(esManager);

        new ConsumersRegistrar(rabbitmqManager, esManager).register();
    }
}






