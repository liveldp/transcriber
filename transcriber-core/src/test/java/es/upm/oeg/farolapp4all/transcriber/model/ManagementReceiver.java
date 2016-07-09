package es.upm.oeg.farolapp4all.transcriber.model;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import com.sun.javafx.runtime.async.AbstractAsyncOperation;

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
public class ManagementReceiver extends AbstractRabbitMqClient {

    private static final String ROUTING_KEY = "annotations.management";
    private static final String QUEUE_NAME = "new_farolas";

    public static void main(String[] args) throws Exception {

        //Setup the channel
        Connection connection = getConnection();
        Channel channel = createChannel(connection);

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE, ROUTING_KEY);

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery =
                    consumer.nextDelivery();
            String msg = new String(delivery.getBody());

            Lamppost message = Lamppost.fromJSON(msg);

            System.out.println("Received: " +  message.toJSON());

        }




    }
}
