package es.upm.oeg.farolapp4all.transcriber;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import es.upm.oeg.farolapp4all.transcriber.model.Consensus;
import es.upm.oeg.farolapp4all.transcriber.model.Lamppost;
import es.upm.oeg.farolapp4all.transcriber.model.PropertyValue;
import es.upm.oeg.farolapp4all.transcriber.util.PropertyValueTransformer;
import es.upm.oeg.farolapp4all.transcriber.util.QueryCollection;
import es.upm.oeg.farolapp4all.transcriber.util.Vocab;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.query.*;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

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
public class ConsensusConsumer extends DefaultConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ConsensusConsumer.class);

    private final static String FAROLAS_URI_PREFIX = "http://farolas.linkeddata.es/resource/";

    public ConsensusConsumer(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag,
                               Envelope envelope,
                               AMQP.BasicProperties properties,
                               byte[] body)
            throws IOException
    {
        String routingKey = envelope.getRoutingKey();
        long deliveryTag = envelope.getDeliveryTag();

        String msg = new String(body);
        logger.debug("Routing key: {}\nDelivery Body:\n{}", routingKey, msg);

        Consensus consensus = Consensus.fromJSON(msg);
        String id = consensus.getId();
        String uri;

        // check if it's a new lamppost
        if (id != null && id.startsWith("tmp")) {

            logger.debug("A temp id found: '{}'", id);

            uri = AppConfig.getIdCache().get(id);
            if ( uri == null) {

                // Minting a new URI for the new lamppost
                String newID = UUID.randomUUID().toString();
                uri =  FAROLAS_URI_PREFIX + newID ;
                logger.debug("A new URI created: {}, {}", id, uri);

                //Adding to the id cache so we will not mint a URI next time for the same temp id
                AppConfig.getIdCache().put(id, uri);

                //Adding the new lamppost to the dataset
                String latitude = consensus.getLatitude();
                String longitude = consensus.getLongitude();

                if (latitude != null && longitude != null) {
                    addNewLamppost(uri, newID, longitude, latitude);
                } else {
                    // we can't add the new lamppost without latitude and longitude
                    getChannel().basicAck(deliveryTag, false);
                    logger.error("A new lamppost without latitude and longitude for id {}", id);
                    return;
                }
            }
        } else {
            // Not a temp uri, we can use the uri in the message as the correct one
            uri = consensus.getUri();
        }

        PropertyValue prop = PropertyValueTransformer.transform(consensus.getAttribute(), consensus.getValue());

        if(prop == null) {
            logger.error("Attribute '{}' is not mapped to RDF. Value was ignored ...", consensus.getAttribute());
            return;
        } else {
            logger.debug("Attribute '{}' is mapped to RDF property '{}'.", consensus.getAttribute(), prop);
        }

        Date lastModified = new Date();

        ParameterizedSparqlString pss = new ParameterizedSparqlString();
        pss.setCommandText(QueryCollection.updateQuery);
        pss.setIri("?uri", uri);
        pss.setIri("?prop", prop.getProperty());
        pss.setLiteral("?timeNew", lastModified.getTime());
        // URI value
        if (!prop.isLiteral()) {
            pss.setIri("?newValue", prop.getValue());
        // Literal value with datatype
        } else if (prop.getDatatype() == null) {
            pss.setLiteral("?newValue", prop.getValue());
        } else {
            pss.setLiteral("?newValue", prop.getValue(), prop.getDatatype());
        }
        String updateQuery = pss.toString();

        logger.debug("Update Query:\n " + updateQuery );

        UpdateRequest request = UpdateFactory.create() ;
        request.add(updateQuery);

        UpdateProcessor processor = UpdateExecutionFactory
                .createRemote(request, AppConfig.getString(AppConfig.SPARQL_ENDPOINT));
        processor.execute();

        getChannel().basicAck(deliveryTag, false);
        logger.info("Updated:\n (<{}> <{}> {} ) at {}", uri, prop.getProperty(), prop.getValue(),
                lastModified.getTime());

    }

    public void addNewLamppost(String uri, String id, String longitude, String latitude) {

        ParameterizedSparqlString pss = new ParameterizedSparqlString();
        pss.setCommandText(QueryCollection.insertGPSQuery);
        pss.setIri("?uri", uri);
        pss.setLiteral("?id", id);
        pss.setLiteral("?long", longitude, XSDDatatype.XSDdecimal);
        pss.setLiteral("?lat", latitude, XSDDatatype.XSDdecimal);
        pss.setLiteral("?timeNew", new Date().getTime());
        String insertQuery = pss.toString();

        logger.debug("Insert Query:\n " + insertQuery );

        UpdateRequest request = UpdateFactory.create() ;
        request.add(insertQuery);

        UpdateProcessor processor = UpdateExecutionFactory
                .createRemote(request, AppConfig.getString(AppConfig.SPARQL_ENDPOINT));
        processor.execute();

        AppConfig.getNewLamppostQueue().add(uri);
        logger.info("Added a new lamppost '{}' with coordinates (Lat - {}, Long - {})", uri, latitude, longitude);

    }





}
