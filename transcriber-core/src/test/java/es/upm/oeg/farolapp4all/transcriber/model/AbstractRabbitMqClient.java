package es.upm.oeg.farolapp4all.transcriber.model;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
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
public abstract class AbstractRabbitMqClient {

    static final String HOST = "localhost";
    static final String USERNAME = "farolapp";
    static final String PASSWORD = "oeg2016";
    static final String VIRTUAL_HOST = "/";

    static final String EXCHANGE = "farolapp";
    static final String EXCHANGE_TYPE = "topic";
    static final boolean DURABLE = true;

    public static Connection getConnection() throws IOException, TimeoutException {

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


}
