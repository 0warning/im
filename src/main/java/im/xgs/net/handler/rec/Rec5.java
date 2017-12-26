package im.xgs.net.handler.rec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import im.xgs.net.chat.channel.Room;
import im.xgs.net.handler.MsgSender;
import im.xgs.net.handler.Protocol;
import im.xgs.net.handler.RecHandler;
import io.netty.channel.Channel;

public class Rec5 implements RecHandler{

	/**
	 * 所有用户信息
	 */
	public void exec(Protocol p, Channel channel,boolean isWeb) {
		if(p == null){
			p = new Protocol();
		}
		p.setHeader(String.valueOf(Protocol.Header.USERPHOTOS.ordinal()));
		Room room = manager.getRoomByRoomId(p.getRoomId());
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for(String userId: room.keySet()){
			Map<String,String> map = new HashMap<String,String>();
			map.put("userName", room.get(userId).getUserName());
			map.put("userId", userId);
			map.put("userPhoto", room.get(userId).getUserPhoto());
			list.add(map);
		}
		p.setContent(JSON.toJSONString(list));
		MsgSender.send(p);
	}

}
