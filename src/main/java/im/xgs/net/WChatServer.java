package im.xgs.net;


import im.xgs.net.chat.AbstractChatServer;
import im.xgs.net.chat.socket.ChatServer;
import im.xgs.net.chat.websocket.WebsocketChatServer;
import im.xgs.net.kit.Prop;
import im.xgs.net.kit.PropKit;



public class WChatServer {

	static Prop prop = PropKit.use("cnf.txt");
	private static int tcpSocketPort = 8122;
	private static int webSocketPort = 8888;
	
	public void start(){
		tcpSocketPort = prop.getInt("socket.tcp.port",tcpSocketPort);
		webSocketPort = prop.getInt("socket.web.port",webSocketPort);
		new Thread(new chatStart(new WebsocketChatServer(webSocketPort))).start();
		new Thread(new chatStart(new ChatServer(tcpSocketPort))).start();
	}
	
	class chatStart implements Runnable{
		private AbstractChatServer server;
		public chatStart(AbstractChatServer server){
			this.server = server;
		}
		public void run() {
			try {
				this.server.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
