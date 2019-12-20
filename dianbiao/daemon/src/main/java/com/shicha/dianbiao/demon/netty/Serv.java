package com.shicha.dianbiao.demon.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.shicha.dianbiao.demon.controller.INotifyHost;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Component
public class Serv implements  ApplicationListener<ApplicationReadyEvent>{

	private static final Logger log = LoggerFactory.getLogger(Serv.class);
	
	@Value("${serv.tcpport:8302}")
	int port;
	
	@Autowired
	INotifyHost notifyhost;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		
		log.info("start server");
		
		try {
			run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
       
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
        
            b.group(bossGroup, workerGroup)
            	.channel(NioServerSocketChannel.class) // (3)
            	.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
		                 @Override
		                 public void initChannel(SocketChannel ch) throws Exception {
		                	 
		                	 
		                     ch.pipeline().addLast(new DeviceMessageDecoder(notifyhost));
		                     
		                 }
		                 
	             	}
            	)
            	.option(ChannelOption.SO_BACKLOG, 128);         // (5)
            	//.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
    
            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)
            
            log.info("server is ready at:" + port);
    
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
