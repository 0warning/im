package im.xgs.net.chat.channel;

import java.util.concurrent.ConcurrentHashMap;
/**
 * 会话机制
 * session的格式 为： 
 *   --------+-----------+------------------+---------
 *    roomId | userId    | type(web,mobile) | channel
 *           |           |     web          | channel
 *           |           |     mobile       | channel
 *   --------+-----------+------------------+---------
 *    roomId | userId    |     mobile       | channel
 *           |           |     web          | channel
 *   --------+-----------+------------------+---------
 * @author xgs
 *
 */
public class Session extends ConcurrentHashMap<String, Room>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
