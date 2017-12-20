package server;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;

public class IOServer {
	
	private static SocketIOServer socketIOServer = null;
	
	public static void main (String args[]) {
		Configuration configuration = new Configuration();
        configuration.setHostname("localhost");
        configuration.setPort(80);
 
        socketIOServer = new SocketIOServer(configuration);
 
        socketIOServer.addEventListener("chatevent", ChatObject.class,
                new DataListener<ChatObject>() {
 
                    @Override
                    public void onData(SocketIOClient client, ChatObject data,
                            AckRequest ackRequest) throws Exception {
                        socketIOServer.getBroadcastOperations().sendEvent("chatevent", data);
                    }
                });
 
        socketIOServer.start();
	}

}
