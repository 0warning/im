package im.xgs.net.handler.rec;

import im.xgs.net.handler.MsgSender;
import im.xgs.net.handler.Protocol;
import im.xgs.net.handler.RecHandler;
import io.netty.channel.Channel;

public class Rec3 implements RecHandler{

	/**
	 * 错误信息
	 */
	public void exec(Protocol p, Channel channel,boolean isWeb) {
		if(p == null){
			p = new Protocol();
		}
		p.setHeader(String.valueOf(Protocol.Header.ERROR.ordinal()));
		MsgSender.send(p);
	}

}
