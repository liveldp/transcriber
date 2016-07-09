package es.upm.oeg.farolapp4all.transcriber.cmd;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import es.upm.oeg.farolapp4all.transcriber.DatasetUpdater;
import es.upm.oeg.farolapp4all.transcriber.AppConfig;
import es.upm.oeg.farolapp4all.transcriber.NewLamppostNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Timer;
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
public class TranscriberApp {

    private static final Logger logger = LoggerFactory.getLogger(TranscriberApp.class);


    public static void main(String[] args) throws Exception {

        // Loading the application configuration
        AppConfig.init();
        logger.info("Application configuration loaded ...");

        ConnectionFactory factory = createConnectionFactory();
        Channel channel = createChannel(factory);
        logger.info("Connected to RabbitMq ...");

        DatasetUpdater updater = new DatasetUpdater(channel);
        updater.init();
        logger.info("Dataset updated initialized ...");

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new NewLamppostNotifier(channel),
                AppConfig.getInt(AppConfig.NOTIFIER_DELAY), AppConfig.getInt(AppConfig.NOTIFIER_PERIOD));
        logger.info("A timer is started for notifications ...");

        final Object forever = new Object();
        synchronized (forever) {
            try { forever.wait(); } catch (InterruptedException ignore) {}
        }


    }

    public static ConnectionFactory createConnectionFactory() {

        String rabbitMqHost = AppConfig.getString(AppConfig.RABBIT_HOST);
        String rabbitMqUser = AppConfig.getString(AppConfig.RABBIT_USER);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMqHost);
        factory.setUsername(rabbitMqUser);
        factory.setPassword(AppConfig.getString(AppConfig.RABBIT_PASSWORD));
        factory.setVirtualHost(AppConfig.getString(AppConfig.RABBIT_VIRTUAL_HOST));
        factory.setAutomaticRecoveryEnabled(true);
        factory.setTopologyRecoveryEnabled(true);
        factory.setRequestedHeartbeat(AppConfig.getInt(AppConfig.RABBIT_HEARTBEAT));
        factory.setConnectionTimeout(AppConfig.getInt(AppConfig.RABBIT_CONNECTION_TIMEOUT));

        logger.debug("RabbitMQ - Connection Factory initialized: {}@{}", rabbitMqUser, rabbitMqHost);

        return factory;
    }

    public static Channel createChannel(ConnectionFactory factory) throws IOException, TimeoutException {

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.basicQos(1, false);

        String exchange = AppConfig.getString(AppConfig.RABBIT_EXCHANGE);
        String exchangeType = "topic";
        boolean durable = true;

        channel.exchangeDeclare(exchange, exchangeType, durable);

        return channel;

    }

}
