package com.daoyintech.daoyin_release;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@EnableAsync
@EnableAutoConfiguration
@EnableTransactionManagement
@MapperScan("com.daoyintech.daoyin_release.mappers")
public class DaoyinReleaseApplication extends SpringBootServletInitializer {


	public static void main(String[] args) {
		SpringApplication.run(DaoyinReleaseApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DaoyinReleaseApplication.class);
	}

}
