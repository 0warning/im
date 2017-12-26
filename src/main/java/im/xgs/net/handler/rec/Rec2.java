package im.xgs.net.handler.rec;

import im.xgs.net.handler.MsgSender;
import im.xgs.net.handler.Protocol;
import im.xgs.net.handler.RecHandler;
import io.netty.channel.Channel;

public class Rec2 implements RecHandler{

	/**
	 * 在线用户数
	 */
	public void exec(Protocol p, Channel channel,boolean isWeb) {
		p.setHeader(String.valueOf(Protocol.Header.COUNT.ordinal()));
		p.setCount(manager.roomUsersCount(p.getRoomId()));
		MsgSender.send(p);
	}

}
