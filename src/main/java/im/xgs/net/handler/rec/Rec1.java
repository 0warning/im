package im.xgs.net.handler.rec;

import im.xgs.net.handler.MsgSender;
import im.xgs.net.handler.Protocol;
import im.xgs.net.handler.RecHandler;
import io.netty.channel.Channel;

public class Rec1 implements RecHandler{

	/**
	 * 发送消息
	 */
	public void exec(Protocol p, Channel channel,boolean isWeb) {
		if(p.getRoomId() != null && !"".equals(p.getRoomId()) && p.getUserId() != null && !"".equals(p.getUserId())){
			manager.setAttribute(p.getRoomId(), p.getUserId(), p.getDevice(),p.getUserPhoto(), p.getUserName(),channel);
		}
		p.setHeader(String.valueOf(Protocol.Header.MSG.ordinal()));
//		manager.setAttribute(p.getRoomId(), p.getUserId(), p.getDevice(),p.getUserPhoto(),p.getUserName(), channel);
		MsgSender.send(p);
	}

}
