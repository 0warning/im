package im.xgs.net.chat.channel;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import im.xgs.net.handler.Protocol;
import im.xgs.net.handler.RecHandler;
import im.xgs.net.util.RecHandlePool;
import io.netty.channel.Channel;

/**
 * 单例模式，session管理需要
 * 
 * @author xgs
 *
 */
public class SessionManager {
	private static String packageStr = "im.xgs.net.handler.rec.Rec";
	public enum ter{
		PC,WEB,MOBILE;
	}
	
	private Session session = new Session();
	private Map<Channel,SessionEntity> channels = new ConcurrentHashMap<Channel,SessionEntity>();
	private static SessionManager manager = new SessionManager();
	
	private SessionManager(){}
	
	public static SessionManager instance(){
		return manager;
	}
	
	public Room getRoomByRoomId(String roomId){
		Room room = session.get(roomId);
		return room;
	}
	/**
	 * 客户端表示退出房间
	 * @param roomId
	 * @param userId
	 * @param type
	 */
	public void removeAttribute(String roomId,String userId,String type){
		Room room = session.get(roomId);
		if(room == null) return;
		User user = room.get(userId);
		if(user == null) return;
		Channel channel = user.get(type);
		if(channel == null) return;
		user.remove(type);
		/**
		 * 如果该用户的所有端都下线，则删除该用户
		 */
		if(user.values().size() == 0){
			room.remove(userId);
		}
		/**
		 * 如果该房间的所有用户都下线，则删除该房间
		 */
		if(room.values().size() == 0){
			session.remove(roomId);
		}
		channels.remove(channel);
	}
	
	/**
	 * 把新进来的用户压入session.如果该房间没有创建，则进行创建，否则直接压入新来的用户
	 * 如果该用户没有创建，则创建，否则对该用户进行压入，支持用户在不同的客户端（WEB和MOBILE）进行同时登陆
	 * @param roomId
	 * @param userId
	 * @param type
	 * @param channel
	 */
	public void setAttribute(String roomId,String userId,String type,String userPhoto,String userName,Channel channel){
		Room room = session.get(roomId);
		if(room == null){
			room = new Room();
		}
		User user = room.get(userId);
//		boolean isNew = false;
		if(user == null){
			user = new User();
//			isNew = true;
		}
		//如果是相同 的channel进来 ，则对channel进行替换
		user.put(type, channel);
		user.setUserPhoto(userPhoto);
		user.setUserName(userName);
		room.put(userId, user);
		session.put(roomId, room);
		SessionEntity entity = new SessionEntity();
		entity.setRoomId(roomId);
		entity.setUserId(userId);
		entity.setType(type);
		channels.put(channel, entity);
		
			//给这个房间所有的用户通知上线。
			Protocol p = new Protocol();
			p.setHeader(String.valueOf(Protocol.Header.USERPHOTOS.ordinal()));
			p.setRoomId(roomId);
			p.setUserId(userId);
			p.setDevice(type);
			p.setCount(this.roomUsersCount(p.getRoomId()));
			RecHandler rec;
			try {
				rec = RecHandlePool.get(packageStr+"5");
				boolean isWeb = String.valueOf(ter.WEB.ordinal()).equals(type)?true:false;
				rec.exec(p, channel,isWeb);
				//给这个上线的人通知在线的人数信息
				rec = RecHandlePool.get(packageStr+"2");
				rec.exec(p, channel,isWeb);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	/**
	 * 根据房间和用户标识查找该用户所有的Channel
	 * @param roomId
	 * @param userId
	 * @return
	 */
	public User getChannels(String roomId,String userId){
		Room room = session.get(roomId);
		if(room == null) return null;
		User user = room.get(userId);
		return user;
	}
	/**
	 * 获取该房间所有用户，及该用户所有设备的连接
	 * @param roomId
	 * @return
	 */
	public Collection<User> getUserByRoomId(String roomId){
		Room room = session.get(roomId);
		if(room == null) return null;
		return room.values();
	}
	/**
	 * 获取该房间的所有用户人数
	 * 
	 * 同一个用户在不同设备登陆，算一个用户数
	 * 
	 * @param roomId
	 * @return
	 */
	public int roomUsersCount(String roomId){
		Room room = session.get(roomId);
		if(room == null) return 0;
		return room.values().size();
	}
	/**
	 * 销毁一个客户端
	 * @param channel
	 */
	public SessionEntity destroyUserType(Channel channel){
		if(!channels.containsKey(channel)) return null;
		SessionEntity entity = channels.get(channel);
		Room room = session.get(entity.getRoomId());
		if(room == null) return null;
		User user = room.get(entity.getUserId());
		if(user == null) return null;
		Channel sc = user.get(entity.getType());
		if(sc.equals(channel)){
			user.remove(entity.getType());
		}
		/**
		 * 如果该用户的所有端都下线，则删除该用户
		 */
		if(user.values().size() == 0){
			room.remove(entity.getUserId());
		}
		/**
		 * 如果该房间的所有用户都下线，则删除该房间
		 */
		if(room.values().size() == 0){
			session.remove(entity.getRoomId());
		}
		channels.remove(channel);
		return entity;
	}
}
