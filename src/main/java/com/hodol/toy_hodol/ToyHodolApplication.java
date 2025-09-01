package com.hodol.toy_hodol;

import com.hodol.toy_hodol.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class ToyHodolApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToyHodolApplication.class, args);
	}

}
