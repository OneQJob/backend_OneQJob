package com.backend.oneqjob;

//import com.backend.oneqjob.global.config.RedisSessionConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

//@Import(RedisSessionConfig.class)
//
//@EN
@SpringBootApplication
public class OneQJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneQJobApplication.class, args);
    }


}
