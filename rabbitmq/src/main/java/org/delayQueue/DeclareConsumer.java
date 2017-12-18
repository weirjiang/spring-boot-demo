package org.delayQueue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class DeclareConsumer {
	public static String EXCHANGE_NAME = "notifyExchange";
	public static String QU_declare_15S = "Qu_declare_15s";
	public static String EX_declare_15S = "EX_declare_15s";
	public static String ROUTINGKEY = "AliPaynotify";
	public static Connection connection = null;
	public static Channel channel = null;
	public static Channel DECLARE_15S_CHANNEL = null;
	public static String declare_queue = "init";
	public static String originalExpiration = "0";
	public static void init() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setPort(5672);
		connection = factory.newConnection();
		channel = connection.createChannel();
		DECLARE_15S_CHANNEL = connection.createChannel();
	}

	public static void consume() {
		try {
			channel.exchangeDeclare(EXCHANGE_NAME, "topic");
			final String queueName = channel.queueDeclare().getQueue();

			channel.queueBind(queueName, EXCHANGE_NAME, ROUTINGKEY);
			System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
			final Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					Map<String, Object> headers = properties.getHeaders();
					if (headers != null) {
						List<Map<String, Object>> xDeath = (List<Map<String, Object>>) headers.get("x-death");
						System.out.println("xDeath--- > " + xDeath);
						if (xDeath != null && !xDeath.isEmpty()) {
							Map<String, Object> entrys = xDeath.get(0);
							// for(Entry<String, Object>
							// entry:entrys.entrySet()){
							// System.out.println(entry.getKey()+":"+entry.getValue());
							// }
							originalExpiration = entrys.get("original-expiration").toString();
						}
					}
					System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'" + "time" + System.currentTimeMillis());
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost post = new HttpPost(message);
					HttpResponse response = httpClient.execute(post);
					BufferedReader inreader = null;
					if (response.getStatusLine().getStatusCode() == 200) {
						inreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
						StringBuffer responseBody = new StringBuffer();
						String line = null;
						while ((line = inreader.readLine()) != null) {
							responseBody.append(line);
						}
						if (!responseBody.equals("success")) {
							// putDeclre15s(message);
							if (originalExpiration.equals("0")) {
								putDeclreQueue(message, 3000, QU_declare_15S);
							}
							if (originalExpiration.equals("3000")) {
								putDeclreQueue(message, 30000, QU_declare_15S);
							}
							if (originalExpiration.equals("30000")) {
								putDeclreQueue(message, 60000, QU_declare_15S);
							}
							if (originalExpiration.equals("60000")) {
								putDeclreQueue(message, 120000, QU_declare_15S);
							}
							if (originalExpiration.equals("120000")) {
								putDeclreQueue(message, 180000, QU_declare_15S);
							}
							if (originalExpiration.equals("180000")) {
								putDeclreQueue(message, 300000, QU_declare_15S);
							}
							if (originalExpiration.equals("300000")) {
//								channel.basicConsume(QU_declare_300S,true, this);
								System.out.println("finish notify");
							}
						}
					} else {
						System.out.println(response.getStatusLine().getStatusCode());
					}
				}
			};

			channel.basicConsume(queueName, true, consumer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public static void testHttp() {
		String uri = "http://localhost:8080/BossCenter/payGateway/jiayuan/alipayPCBindByRechargeRecv.jsp?buyer_email=xx02%40163.com&buyer_id=2088102114562585&exterface=alipay.acquire.page.createandpay&gmt_payment=2014-08-29+16%3A21%3A13&is_success=T&notify_id=4ab9bed148d043d0bf75460706f7774a&notify_time=2014-08-29+16%3A22%3A02&notify_type=trade_status_sync&out_trade_no=14217121201098626&seller_email=sinom051501%40alitest.com&seller_id=2088101139767023&subject=%E9%9D%9E%E7%A8%8E%E7%BC%B4%E6%AC%BE&total_fee=424.42&trade_no=14217121201098611&trade_status=TRADE_SUCCESS&sign=b10993d2a4fa9f8dbaec51fb7d0904c2&sign_type=MD5";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(uri);
		HttpResponse response;
		StringBuffer responseBody = new StringBuffer();
		try {
			response = httpClient.execute(post);
			BufferedReader inreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			String line = null;
			while ((line = inreader.readLine()) != null) {
				responseBody.append(line);
			}
			if (response.getStatusLine().getStatusCode() == 200) {
				System.out.println(responseBody.toString());
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static Map<String, Object> xdeathMap = new HashMap<String, Object>();
	static List<Map<String, Object>> xDeath = new ArrayList<Map<String, Object>>();
	static Map<String, Object> xdeathParam = new HashMap<String, Object>();

	public static void putDeclre15s(String message) throws IOException {
		channel.exchangeDeclare(EX_declare_15S, "topic");
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-dead-letter-exchange", EXCHANGE_NAME);// 死信exchange
		AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
		builder.expiration("3000").deliveryMode(2);// 设置消息TTL
		AMQP.BasicProperties properties = builder.build();
		channel.queueDeclare(QU_declare_15S, false, false, false, args);
		channel.queueBind(QU_declare_15S, EX_declare_15S, ROUTINGKEY);
		channel.basicPublish(EX_declare_15S, ROUTINGKEY, properties, message.getBytes());
		System.out.println("send message in QA_DEFERRED_15S" + message + "time" + System.currentTimeMillis());
	}

	public static void putDeclreQueue(String message, int mis, String queue) throws IOException {
		channel.exchangeDeclare(EX_declare_15S, "topic");
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-dead-letter-exchange", EXCHANGE_NAME);// 死信exchange
		AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
		builder.expiration(String.valueOf(mis)).deliveryMode(2);// 设置消息TTL
		AMQP.BasicProperties properties = builder.build();
		channel.queueDeclare(queue, false, false, false, args);
		channel.queueBind(queue, EX_declare_15S, ROUTINGKEY);
		channel.basicPublish(EX_declare_15S, ROUTINGKEY, properties, message.getBytes());
		System.out.println("send message in " + queue + message + "time============" + System.currentTimeMillis());
	}

	public static void main(String args[]) throws Exception {
		init();
		consume();
	}
}
