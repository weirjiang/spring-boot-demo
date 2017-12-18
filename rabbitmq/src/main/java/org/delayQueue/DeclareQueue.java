package org.delayQueue;
	
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class DeclareQueue {
	public static String EXCHANGE_NAME = "notifyExchange";

	public static void init() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setPort(5672);

		Connection connection = null;
		try {
			connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
			String routingKey = "AliPaynotify";
			String message = "http://localhost:8080/BossCenter/payGateway/notifyRecv.jsp?is_success=T&notify_id=4ab9bed148d043d0bf75460706f7774a&notify_time=2014-08-29+16%3A22%3A02&notify_type=trade_status_sync&out_trade_no=1421712120109862&total_fee=424.42&trade_no=14217121201098611&trade_status=TRADE_SUCCESS";
			channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
			System.out.println(" [x] Sent :" + message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception ignore) {
				}
			}
		}
	}

	public static void main(String args[]) {
		init();
	}

}
