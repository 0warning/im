package im.xgs.net.handler.rec;

import com.alibaba.fastjson.JSON;

import im.xgs.net.handler.MsgSender;
import im.xgs.net.handler.Protocol;
import im.xgs.net.handler.RecHandler;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class Rec0 implements RecHandler{

	/**
	 * 心跳
	 */
	public void exec(Protocol p, Channel channel,boolean isWeb) {
		//如果为心跳包，则原路返回
		if(p.getRoomId() != null && !"".equals(p.getRoomId()) && p.getUserId() != null && !"".equals(p.getUserId())){
			manager.setAttribute(p.getRoomId(), p.getUserId(), p.getDevice(),p.getUserPhoto(), p.getUserName(),channel);
		}
		p.setCount(manager.roomUsersCount(p.getRoomId()));
		Object obj = JSON.toJSONString(p);
		if(isWeb){
			obj = new TextWebSocketFrame((String) obj);
		}else{
			 obj = MsgSender.pakaging((String)obj);
		}
		channel.writeAndFlush(obj);
	}

}
