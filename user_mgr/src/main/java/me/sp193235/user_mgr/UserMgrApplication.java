package me.sp193235.user_mgr;

import org.springframework.amqp.core.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class UserMgrApplication {

	public static final String exchangeName = "vinr-user-exchange";

	public static final String interactionsQueueName = "interactions-new-user";
	public static final String notificationsQueueName = "notifications-new-user";

	@Bean
	public List<Declarable> fanoutBindings() {
		Queue fanoutQueue1 = new Queue(interactionsQueueName, false);
		Queue fanoutQueue2 = new Queue(notificationsQueueName, false);
		FanoutExchange fanoutExchange = new FanoutExchange(exchangeName);
		return Arrays.asList(
				fanoutQueue1,
				fanoutQueue2,
				fanoutExchange,
				BindingBuilder.bind(fanoutQueue1).to(fanoutExchange),
				BindingBuilder.bind(fanoutQueue2).to(fanoutExchange));
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
		SpringApplication.run(UserMgrApplication.class, args);
	}

}
