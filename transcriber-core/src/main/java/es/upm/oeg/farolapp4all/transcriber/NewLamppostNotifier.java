package es.upm.oeg.farolapp4all.transcriber;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import es.upm.oeg.farolapp4all.transcriber.model.Lamppost;
import es.upm.oeg.farolapp4all.transcriber.util.QueryCollection;
import org.apache.jena.query.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Queue;
import java.util.TimerTask;

/**
 * Copyright 2014-2016 Ontology Engineering Group, Universidad Politécnica de Madrid, Spain
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Nandana Mihindukulasooriya
 * @since 1.0.0
 */
public class NewLamppostNotifier extends TimerTask {

    private static final Logger logger = LoggerFactory.getLogger(NewLamppostNotifier.class);

    private Channel channel;

    public NewLamppostNotifier(Channel channel){
        this.channel = channel;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        final Queue<String> newLamppostQueue = AppConfig.getNewLamppostQueue();
        logger.debug("Checking new lamppost queue - '{}' uris found.", newLamppostQueue.size());

        newLamppostQueue.forEach(uri ->
        {
            Lamppost lamppost = checkForMinumumProperties(uri);
            if(lamppost != null) {
                String exchange = AppConfig.getString(AppConfig.RABBIT_EXCHANGE);
                String routingKey = AppConfig.getString(AppConfig.RABBIT_MANAGEMENT);
                try {
                    channel.basicPublish(exchange, routingKey, null, lamppost.toJSON().getBytes());
                    logger.info("Published message:\n" + lamppost.toJSON());
                    newLamppostQueue.remove(uri);
                } catch (IOException e) {
                    logger.error("An error occurred when publishing message:\n" + lamppost.toJSON(), e);
                }
            }
        }
        );
    }

    public static Lamppost checkForMinumumProperties(String uri) {

        ParameterizedSparqlString pss = new ParameterizedSparqlString();
        pss.setCommandText(QueryCollection.minPropertiesQuery);
        pss.setIri("?uri", uri);
        String minPropQuery = pss.toString();

        logger.debug("Minimum Props Query:\n " + minPropQuery );

        Query query = QueryFactory.create(minPropQuery);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(AppConfig.getString(AppConfig.SPARQL_ENDPOINT), query)) {
            {
                ResultSet results = qexec.execSelect();

                // collect all the values
                if (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();
                    String id = soln.get("id").asLiteral().getString();
                    double geolat = soln.get("lat").asLiteral().getDouble();
                    double geolong = soln.get("long").asLiteral().getDouble();
                    double heading = soln.get("heading").asLiteral().getDouble();
                    double pitch = soln.get("pitch").asLiteral().getDouble();

                    return new Lamppost(uri, id, geolat, geolong,heading, pitch);

                } else {
                    return null;
                }
            }
        }
    }
}
