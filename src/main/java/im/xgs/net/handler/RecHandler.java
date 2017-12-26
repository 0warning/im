package im.xgs.net.handler;

import im.xgs.net.chat.channel.SessionManager;
import io.netty.channel.Channel;

public interface RecHandler {

	SessionManager manager = SessionManager.instance();
	
	void exec(Protocol p ,Channel channel,boolean isWeb);
}
