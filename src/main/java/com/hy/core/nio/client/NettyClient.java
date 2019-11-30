package com.hy.core.nio.client;

import org.springframework.stereotype.Component;

import java.util.Scanner;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NettyClient {
    public static void main(String[] args)  {
        start();
    }

    public static void start() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                //该参数的作用就是禁止使用Nagle算法，使用于小数据即时传输
                .option(ChannelOption.TCP_NODELAY, true)
                .channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());

        try {
            ChannelFuture future = bootstrap.connect("127.0.0.1", 8090).sync();
            log.info("客户端成功....");
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                }
            });
//            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//            String str = null;
//            System.out.println("please enter a value:");
//            str = br.readLine();
//            System.out.println("value is: " + str);

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()){
                //发送消息
                String msg = scanner.nextLine();
                future.channel().writeAndFlush(msg + "\r\n");
                log.info("客户端发送: {}", msg);
            }
            // 等待连接被关闭
            log.info("客户端等待关闭.......");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
            log.info("客户端关闭");
        }
    }
}
