package im.xgs.net.chat.websocket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import im.xgs.net.chat.channel.SessionManager;
import im.xgs.net.handler.MsgHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;


/**
 * TextWebSocketFrameHandler 继承自SimpleChannelInboundHandler，
 * 这个类实现了ChannelInboundHandler接口，ChannelInboundHandler 
 * 提供了许多事件处理的接口方法，然后你可以覆盖这些方法。现在仅仅只需要继承 SimpleChannelInboundHandler 
 * 类而不是你自己去实现接口方法。
 */
public class TextWebSocketFrameHandler extends
		SimpleChannelInboundHandler<TextWebSocketFrame> {

	private static Logger logger = LoggerFactory.getLogger("xgs.net.im.websocket.handle");
	
	private MsgHandler msgHandler = new MsgHandler();
	private SessionManager manager = SessionManager.instance();
	private int i = 0;
	
	/**
	 * 覆盖了 channelRead0() 事件处理方法。每当从服务端读到客户端写入信息时，将信息转发给其他客户端的 Channel。
	 * 其中如果是 Netty 5.x 版本时，需要把 channelRead0() 重命名为messageReceived()
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			TextWebSocketFrame msg) throws Exception {
		String arg = msg.text();
		msgHandler.channelRead0(true, ctx, arg);
	}
	/**
	 * 覆盖了 handlerRemoved() 事件处理方法。每当从服务端收到客户端断开时，
	 * 客户端的 Channel 移除 ChannelGroup 列表中，并通知列表中的其他客户端 Channel
	 */
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception{
		msgHandler.handlerRemoved(true, ctx);
	}
	/**
	 * 覆盖了 channelActive() 事件处理方法。服务端监听到客户端活动
	 */
	public void channelActive(ChannelHandlerContext ctx) throws Exception{
		msgHandler.channelActive(true, ctx);
	}
	/**
	 * 覆盖了 channelInactive() 事件处理方法。服务端监听到客户端不活动
	 */
	public void channelInactive(ChannelHandlerContext ctx) throws Exception{
		msgHandler.channelInactive(true, ctx);
	}
	/**
	 * exceptionCaught() 事件处理方法是当出现 Throwable 对象才会被调用，
	 * 即当 Netty 由于 IO 错误或者处理器在处理事件时抛出的异常时。
	 * 在大部分情况下，捕获的异常应该被记录下来并且把关联的 channel 给关闭掉。
	 * 然而这个方法的处理方式会在遇到不同异常的情况下有不同的实现，比如你可能想在关闭连接之前发送一个错误码的响应消息。
	 */
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
		msgHandler.exceptionCaught(true, ctx, cause);
	}
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			String type = "";
			if (event.state() == IdleState.READER_IDLE) {
				i++;
				if(i==3){
					manager.destroyUserType(ctx.channel());
					ctx.channel().close();
					logger.error("{}超时,类型{}",ctx.channel().remoteAddress(),type);
					return;
				}
				type = "read idle";
			} else if (event.state() == IdleState.WRITER_IDLE) {
				type = "write idle";
			} else if (event.state() == IdleState.ALL_IDLE) {
				type = "all idle";
			}
			logger.info("{}超时,类型{}",ctx.channel().remoteAddress(),type);
		} else {
			logger.debug(evt.toString());
			super.userEventTriggered(ctx, evt);
		}
	}
}
