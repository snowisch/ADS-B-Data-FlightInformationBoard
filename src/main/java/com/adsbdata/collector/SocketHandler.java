package com.adsbdata.collector;

import com.adsbdata.executer.AdsbMain;

//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SocketHandler extends ChannelInboundHandlerAdapter{
//    private final ByteBuf firstMSG;
//
//    public SocketHandler() {
//        byte[] req = "QUERY TIME ORDER".getBytes();
//        firstMSG = Unpooled.buffer(req.length);
//        firstMSG.writeBytes(req);
//    }
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush(firstMSG);
//    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String rawRecord = (String) msg;
        if (rawRecord.length() >= 50) {
        	rawRecord = rawRecord.replaceAll("\r|\n", "");
        	rawRecord = SocketReader.adsbRawRecordInitial + rawRecord;
        	//System.out.println(rawRecord);
        	AdsbMain.adsbRecordProccessing(rawRecord);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}