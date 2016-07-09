package es.upm.oeg.farolapp4all.transcriber;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class DatasetUpdater {

    private static final Logger logger = LoggerFactory.getLogger(DatasetUpdater.class);

    private final static String QUEUE_NAME = "consensus_updates";

    private Channel channel;

    public DatasetUpdater (Channel channel) {
        this.channel = channel;
    }

    public void init() throws IOException, ShutdownSignalException,
            ConsumerCancelledException, InterruptedException, TimeoutException {

        String exchange = AppConfig.getString(AppConfig.RABBIT_EXCHANGE);
        String routingPattern = "annotations.consensus";

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, exchange, routingPattern);
        logger.debug("RabbitMQ - Queue created: {}:{}:{}:{}", exchange, "topic", QUEUE_NAME, routingPattern);

        ConsensusConsumer consumer = new ConsensusConsumer(channel);

        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, "consumerTag-transcriber", consumer);

        logger.info("Dataset uptater initialized ... ");

    }


}
