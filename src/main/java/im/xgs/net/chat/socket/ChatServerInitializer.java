package im.xgs.net.chat.socket;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

public class ChatServerInitializer extends ChannelInitializer<SocketChannel> {

	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		//添加心跳检测
        pipeline.addLast(new IdleStateHandler(10,10, 10, TimeUnit.SECONDS));
		pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//		pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 0));
//        pipeline.addLast(new StringDecoder());
		pipeline.addLast("handler", new BufHandler());

		System.out.println("ChatClient:" + ch.remoteAddress() + "连接上");
	}
}
