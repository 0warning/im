package im.xgs.net.handler;

import java.util.Collection;
import java.util.Date;

import com.alibaba.fastjson.JSON;

import im.xgs.net.chat.channel.SessionManager;
import im.xgs.net.chat.channel.User;
import im.xgs.net.util.DateStyle;
import im.xgs.net.util.DateUtil;
import im.xgs.net.util.Utility;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class MsgSender {
	static SessionManager manager = SessionManager.instance();
	/**
	 * 该方法应该用异步来执行
	 * @param p
	 */
	public static void send(Protocol p){
		Collection<User> users = manager.getUserByRoomId(p.getRoomId());
		if(users == null || users.size() == 0)  return;
		p.setTime(DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
		String msg = JSON.toJSONString(p);
		for(User user:users){
			for(String key : user.keySet()){
				Channel channel = user.get(key);
				Object obj = msg;
				if(key.equals(String.valueOf(SessionManager.ter.WEB.ordinal()))){
					obj = new TextWebSocketFrame(msg);
				}else{
					 obj = pakaging((String)obj);
				}
				channel.writeAndFlush(obj);
			}
		}
	}
	
	public static ByteBuf pakaging(String str){
		byte[] bytes = str.getBytes();
		byte[] size_b = Utility.int2Byte(bytes.length);
		byte[] newbytes = new byte[bytes.length+4];
		
		System.arraycopy(size_b, 0, newbytes, 0, 4);
		System.arraycopy(bytes, 0, newbytes, 4, bytes.length);
		return Unpooled.copiedBuffer(newbytes);
	}
}
