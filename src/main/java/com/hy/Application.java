package com.hy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hy.**.mapper.**")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

//        NettyServer server = new NettyServer();
//        server.start(new InetSocketAddress("127.0.0.1", 8090));
    }
}
