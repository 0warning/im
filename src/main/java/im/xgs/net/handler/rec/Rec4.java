package im.xgs.net.handler.rec;

import im.xgs.net.handler.MsgSender;
import im.xgs.net.handler.Protocol;
import im.xgs.net.handler.RecHandler;
import io.netty.channel.Channel;

public class Rec4 implements RecHandler{

	/**
	 * 下线信息
	 */
	public void exec(Protocol p, Channel channel,boolean isWeb) {
		if(p == null){
			p = new Protocol();
		}
		if(manager.getRoomByRoomId(p.getRoomId()) != null && manager.getRoomByRoomId(p.getRoomId()).get(p.getUserId()) != null){
			return;
		}
		p.setHeader(String.valueOf(Protocol.Header.DOWN.ordinal()));
		MsgSender.send(p);
	}

}
