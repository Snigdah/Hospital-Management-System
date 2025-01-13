package com.example.EurecaServerDiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurecaServerDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurecaServerDiscoveryApplication.class, args);
	}

}
