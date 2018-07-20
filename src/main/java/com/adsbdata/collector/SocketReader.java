package com.adsbdata.collector;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
//import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class SocketReader {
	public static final String adsbRawRecordInitial = "M";
	
    public void connect(int port, String host) throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap client = new Bootstrap();
    	ChannelFuture future = null;
        try {
            client.group(group)
            		.channel(NioSocketChannel.class)
            		.option(ChannelOption.TCP_NODELAY, true)
            		.option(ChannelOption.SO_KEEPALIVE, true)
            		.handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                        	ByteBuf delimiter = Unpooled.copiedBuffer(adsbRawRecordInitial.getBytes());
                            //ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                        	ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                            ch.pipeline().addLast(new StringDecoder());
                        	ch.pipeline().addLast(new SocketHandler());
                        }
                    });
            future = client.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } finally {
            //group.shutdownGracefully();
            //System.out.println("Socket Close Gracefully.");          
        	if (null != future) {  
        		if (future.channel() != null && future.channel().isOpen()) {  
        			future.channel().close();  
        		}  
        	}
        	//System.out.println("Reconnceting...");  
        	connect(port, host);
        }
    }
}