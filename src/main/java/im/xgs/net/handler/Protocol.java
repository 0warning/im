package im.xgs.net.handler;

 /**
  * 
  *	消息简单协议制定
  *	发送消息：json格式,在客户端保障用户最大输入不超过140个字符
  * 其中 header 1表示发送消息，2表示该房间在线用户数，0表示心跳包,3为错误信息,4为通知一个用户下线
  *	----------+-----------+--------------+----------+---------+-------------
  *	 header   |  roomId   | userId       | device   | content | count(在线人数)
  * @author hasee
  *
  */
public class Protocol {

	public static enum Header{
		IDLE,MSG,COUNT,ERROR,DOWN,USERPHOTOS,UP
	}
	
	private String header;
	private String roomId;
	private String userId;
	private String device;
	private String content;
	private String time;
	private String userPhoto;
	private String userName;
	private int count;
	
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
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
