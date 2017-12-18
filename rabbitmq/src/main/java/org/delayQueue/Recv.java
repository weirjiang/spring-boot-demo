package org.delayQueue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class Recv {

	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException,
			InterruptedException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setPort(5672);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare("exchangeA", "direct");
		channel.exchangeDeclare("exchangeB", "direct");

		Map<String, Object> queueArgs = new HashMap<String, Object>();
		queueArgs.put("x-dead-letter-exchange", "exchangeB");
		channel.queueDeclare("queueA", false, false, false, queueArgs);
		channel.queueDeclare("queueB", false, false, false, null);

		// 绑定路由
		channel.queueBind("queueA", "exchangeA", "");
		channel.queueBind("queueB", "exchangeB", "");
		System.out.println("ready to receive message");
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println("received message:" + message + ",date:" + System.currentTimeMillis());

				Map<String, Object> headers = properties.getHeaders();
				if (headers != null) {
					List<Map<String, Object>> xDeath = (List<Map<String, Object>>) headers.get("x-death");
					System.out.println("xDeath--- > " + xDeath);
					if (xDeath != null && !xDeath.isEmpty()) {
						Map<String, Object> entrys = xDeath.get(0);
						for(Entry<String, Object> entry:entrys.entrySet()){
							System.out.println(entry.getKey()+":"+entry.getValue());
						}
					}
				}
			}
		};
		channel.basicConsume("queueB", true, consumer);
	}
}
