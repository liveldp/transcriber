package es.upm.oeg.farolapp4all.transcriber.model;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.typesafe.config.ConfigException;
import es.upm.oeg.farolapp4all.transcriber.model.Consensus;
import es.upm.oeg.farolapp4all.transcriber.util.Vocab;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

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
public class ConsensusSender extends AbstractRabbitMqClient {

    private static final String ROUTING_KEY = "annotations.consensus";

    public static void main(String[] args) throws IOException, TimeoutException {

        //Setup the channel
        Connection connection = getConnection();
        Channel channel = createChannel(connection);

        //Create the message
        Consensus msg = newLampost(Vocab.A_COLOR, Color.RED);
        String id = msg.getId();
        channel.basicPublish(EXCHANGE, ROUTING_KEY, null, msg.toJSON().getBytes());

        Consensus msg2 = updateLampPost(id, Vocab.A_LAMP, Lamp.LED);
        channel.basicPublish(EXCHANGE, ROUTING_KEY, null, msg2.toJSON().getBytes());

        Consensus msg3 = updateLampPost(id, Vocab.A_HEADING, "0");
        channel.basicPublish(EXCHANGE, ROUTING_KEY, null, msg3.toJSON().getBytes());

        Consensus msg4 = updateLampPost(id, Vocab.A_PITCH, "0");
        channel.basicPublish(EXCHANGE, ROUTING_KEY, null, msg4.toJSON().getBytes());

        //Free the resources
        channel.close();
        connection.close();

    }

    public static Consensus newLampost(String key, String value) {

        String tempID = "temp" + UUID.randomUUID().toString();

        double latitude = 40 +  Math.random();
        double longitude = -84 + Math.random();

        Consensus msg = new Consensus();
        msg.setId(tempID);
        msg.setUri("http://farolas.linkeddata.es/tempID");
        msg.setAttribute(key);
        msg.setValue(value);
        msg.setLatitude(Double.toString(latitude));
        msg.setLongitude(Double.toString(longitude));

        return msg;

    }

    public static Consensus updateLampPost(String id, String key, String value) {

        Consensus msg = new Consensus();
        msg.setId(id);
        msg.setUri("http://farolas.linkeddata.es/resource/" + id);
        msg.setAttribute(key);
        msg.setValue(value);

        return msg;
    }

    public static Connection getConnection() throws IOException, TimeoutException{

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VIRTUAL_HOST);
        return  factory.newConnection();
    }

    public static Channel createChannel(Connection connection) throws IOException{
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE, EXCHANGE_TYPE, DURABLE);

        return channel;
    }

    class Color {
        public static final String RED = "red";
        public static final String ORANGE = "orange";
        public static final String YELLOW = "yellow";
        public static final String WHITE = "white";
        public static final String GREEN = "green";
        public static final String BLUE = "blue";
    }

    class Height {
        public static final String LOW = "low";
        public static final String MEDIUM = "medium";
        public static final String HIGH = "high";
    }

    class Light {
        public static final String P = "P";
        public static final String F = "F";
        public static final String E = "E";
        public static final String AA = "AA";
        public static final String AC = "AC";
        public static final String ER = "ER";
        public static final String O = "O";
    }

    class Lamp {
        public static final String VSAP = "VSAP";
        public static final String VMCC = "VMCC";
        public static final String VMAP = "VMAP";
        public static final String PAR = "PAR";
        public static final String MC = "MC";
        public static final String LED = "LED";
        public static final String I = "I";
        public static final String H = "H";
        public static final String VSBP = "VSBP";
        public static final String FCBC = "FCBC";
        public static final String HM = "HM";
        public static final String F = "F";
    }

    class Covered {
        public static final String True = "true";
        public static final String False = "false";
    }

    class Wattage {
        public static final String w35 = "35";
        public static final String w40 = "40";
        public static final String w60 = "60";
        public static final String w100 = "100";
    }

}
