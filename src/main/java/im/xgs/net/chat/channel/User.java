package im.xgs.net.chat.channel;

import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

public class User extends ConcurrentHashMap<String, Channel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userPhoto;
	
	private String userName;

	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	
}
