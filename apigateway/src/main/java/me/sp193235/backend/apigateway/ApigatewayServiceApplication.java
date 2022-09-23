package me.sp193235.backend.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@EnableEurekaClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class ApigatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApigatewayServiceApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("users_route", r -> r.path("/api/user").or().path("/api/user/**")
                        .uri("http://vinr-usermgr:8081"))
                .route("interactions_route", r -> r.path("/api/post").or().path("/api/post/**")
                        .or().path("/api/reaction").or().path("/api/reaction/**")
                        .or().path("/api/vinr").or().path("/api/vinr/**")
                        .or().path("/api/circle").or().path("/api/circle/**")
                        .or().path("/api/comment").or().path("/api/comment/**")
                        .uri("http://vinr-interactionmgr:8082"))
                .route("notifications_route", r -> r.path("/api/notification").or().path("/api/notification/**")
                        .uri("http://vinr-notificationmgr:8083"))
                .build();
    }

}
