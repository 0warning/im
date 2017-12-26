package im.xgs.net.handler;



import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import im.xgs.net.chat.channel.SessionEntity;
import im.xgs.net.chat.channel.SessionManager;
import im.xgs.net.util.RecHandlePool;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;


public class MsgHandler {


	private Logger logger = LoggerFactory.getLogger(getClass());
	private SessionManager manager = SessionManager.instance();
	private static String packageStr = "im.xgs.net.handler.rec.Rec";
	/**
	 * 该方法是记录一个socket的连接已断开，
	 * 断开时，如果是已登陆用户，应通知该用户的其它设备，还有其好友，
	 * 群组（如果有组织机构时，则还应通知其所在的组织机构）
	 * @param isWeb
	 * @param ctx
	 * @throws Exception
	 */
	public void handlerRemoved(boolean isWeb, ChannelHandlerContext ctx)
			throws Exception {
		logger.debug("离线=============="+ctx.channel().remoteAddress());
		Channel channel = ctx.channel();
		//将给用户的session管道里相关删除
		SessionEntity entity = manager.destroyUserType(channel);
		if(entity == null){
			channel.close();
			return;
		}
		// 告诉 同房间的小伙伴们，该用户已下线
		Protocol p = new Protocol();
		p.setHeader(String.valueOf(Protocol.Header.DOWN.ordinal()));
		p.setRoomId(entity.getRoomId());
		p.setUserId(entity.getUserId());
		p.setDevice(entity.getType());
		p.setCount(manager.roomUsersCount(p.getRoomId()));
		RecHandler rec = RecHandlePool.get(packageStr+p.getHeader());
		rec.exec(p, channel,isWeb);
		channel.close();
		
	}


	public void channelRead0(boolean isWeb, ChannelHandlerContext ctx,String msg) throws Exception {
		Channel incoming = ctx.channel();
		try {
			Protocol p = JSON.parseObject(msg, Protocol.class);
			//客户端表示退出房间
			if(p!=null && "-1".equals(p.getHeader())){
				manager.removeAttribute(p.getRoomId(), p.getUserId(), p.getDevice());
				p.setHeader(String.valueOf(Protocol.Header.DOWN.ordinal()));
				p.setCount(manager.roomUsersCount(p.getRoomId()));
				RecHandler rec = RecHandlePool.get(packageStr+p.getHeader());
				rec.exec(p, incoming,isWeb);
				return ;
			}
			RecHandler rech = RecHandlePool.get(packageStr+p.getHeader());
			rech.exec(p, incoming,isWeb);
			incoming.flush();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error(msg);
			Protocol p = new Protocol();
			p.setHeader(String.valueOf(Protocol.Header.ERROR.ordinal()));
			incoming.write(JSON.toJSONString(p));
			incoming.flush();
		}
		incoming.flush();
	}

	/**
	 * 该用户在线
	 * @param isWeb
	 * @param ctx
	 * @throws Exception
	 */
	public void channelActive(boolean isWeb, ChannelHandlerContext ctx)
			throws Exception {
		Channel incoming = ctx.channel();
		System.out.println((isWeb ? "web" : "other") + incoming.remoteAddress()
				+ "在线\n在线时间为："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}

	/**
	 * 该用户下线
	 * @param isWeb
	 * @param ctx
	 * @throws Exception
	 */
	public void channelInactive(boolean isWeb, ChannelHandlerContext ctx)
			throws Exception {
		Channel incoming = ctx.channel();
		System.out.println((isWeb ? "web" : "other") + incoming.remoteAddress()
				+ "掉线\n掉线时间为："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}

	/**
	 * 在处理消息时或其它地方的异常，这里根据情况进行处理，或关闭该
	 * channel，或发消息通知,etc...
	 * @param isWeb
	 * @param ctx
	 * @param cause
	 */
	public void exceptionCaught(boolean isWeb, ChannelHandlerContext ctx,Throwable cause) {
		Channel incoming = ctx.channel();
		System.out.println((isWeb ? "web" : "other") + incoming.remoteAddress()+ "异常，异常原因："+cause.getMessage());
		ctx.close();
	}
}
