package org.rabbitmq.workqueue;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Worker {
	private static final String TASK_QUEUE_NAME = "task_queue";

	  public static void main(String[] argv) throws Exception {
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    factory.setPort(5673);
	    final Connection connection = factory.newConnection();
	    final Channel channel = connection.createChannel();
	    
	    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
	    System.out.println("worker thread:"+Thread.currentThread().hashCode()+" [*] Waiting for messages. To exit press CTRL+C");

	    channel.basicQos(3);

	    final Consumer consumer = new DefaultConsumer(channel) {
	      @Override
	      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
	        String message = new String(body, "UTF-8");
	        System.out.println(" [x] Received '" + message + "'");
	        try {
	          doWork(message);
	        } finally {
	        	System.out.println("[isredeliver]"+envelope.isRedeliver()+" [x] Done");
	          //是否确认消息已被消费
//	          channel.basicAck(envelope.getDeliveryTag(), false);
	        }
	      }
	    };
	    boolean autoAck = false;
	    //启动一个消费者进程 是否自动确认消息被消费
	    channel.basicConsume(TASK_QUEUE_NAME, autoAck, consumer);
	  }

	  private static void doWork(String task) {
	    for (char ch : task.toCharArray()) {
	      if (ch == '.') {
	        try {
	          Thread.sleep(1000);
	        } catch (InterruptedException _ignored) {
	          Thread.currentThread().interrupt();
	        }
	      }
	    }
	  }
}
