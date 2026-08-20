package kr.co.sist;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("kr.co.sist")
public class SpringThymleafApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringThymleafApplication.class, args);
	}

}
