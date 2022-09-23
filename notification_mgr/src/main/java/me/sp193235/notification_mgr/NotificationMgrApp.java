package me.sp193235.notification_mgr;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan(basePackages={"me.sp193235"})
public class NotificationMgrApp {

	public static final String exchangeName = "vinr-user-exchange";

	public static final String queueName = "notifications-new-user";

	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	@Bean
	FanoutExchange exchange() {
		return new FanoutExchange(exchangeName);
	}

	@Bean
	Binding binding(Queue queue, FanoutExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange);
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
											 MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(MessageReceiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	public static void main(String[] args) {
		System.out.println("Echo-ing db config:");
		System.out.print("jdbc:");
		System.out.print(System.getenv("SPRING_DB_TYPE"));
		System.out.print("://");
		System.out.print(System.getenv("SPRING_DB_HOST"));
		System.out.print(":");
		System.out.print(System.getenv("SPRING_DB_PORT"));
		System.out.print("/");
		System.out.println(System.getenv("SPRING_DATABASE"));
		System.out.println(System.getenv("DB_USER"));
		System.out.println(System.getenv("DB_PASSWORD"));
		SpringApplication.run(NotificationMgrApp.class, args);
	}

}
