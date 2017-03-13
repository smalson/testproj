package org.metamorarobotics.rov;

import java.net.UnknownHostException;
import java.util.Iterator;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.drafts.Draft_17;
import org.kettering.team1506.rp3.camservice.WSService;
import org.metamorarobotics.gamepad.ROVGamepad;

public class GamepadServiceMain {

	public static void main(String[] args) throws UnknownHostException {

		// start rxtx gamepad listener
		
		// start ws service
		WebSocketImpl.DEBUG = false;
		WSService service = new WSService( 9003, new Draft_17() );
		service.start();

		ROVGamepad.GamepadValues gamepad;
		boolean a = true;
		
		try {
			
			while(true) {
				
				// read gamepad value to state.
				
				if(a)
					a = false;
				else
					a = true;
				
				gamepad = ROVGamepad.GamepadValues.newBuilder()
						.setA(a)
						.setB(true)
						.setBack(false)
						.setDpadDown(false)
						.setDpadLeft(false)
						.setDpadRight(false)
						.setDpadUp(false)
						.setLeftBumper(false)
						.setLeftStickButton(false)
						.setLeftStickX(0.0F)
						.setLeftStickY(0.0F)
						.setLeftTrigger(0.0F)
						.setRightBumper(false)
						.setRightStickButton(false)
						.setRightStickX(0.0F)
						.setRightStickY(0.0F)
						.setRightTrigger(0.0F)
						.setStart(false)
						.setX(false)
						.setY(false)
						.build();
				
				try {					
					Iterator<WebSocket> eachWebSocket = service.connections().iterator();
			        while (eachWebSocket.hasNext()) {
			        	WebSocket ws = eachWebSocket.next();
//			        	System.out.println("Send Msg to Web Socket Client: "+ws.getRemoteSocketAddress());
			        	ws.send(gamepad.toByteArray());
			        }
					Thread.currentThread().sleep(100); // gamepad update period
				} catch(Exception ex) {
					System.out.println("Caught Ex: "+ex);
				}
			}		
		} catch ( Exception e1 ) {
			e1.printStackTrace();
		} finally {
			try {
				service.stop();			
			} catch(Exception ex) {
			
			}
		}
		
	}

}
