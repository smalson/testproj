package org.metamorarobotics.rov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.FrameBuilder;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

public class WSClient extends WebSocketClient {

	public WSClient( URI uri, Draft d ) {
		super(uri, d);
	}
	
	public static void main( String[] args ) {
		Draft d = new Draft_17();
		String clientname = "wsClient";
		
		
		WebSocketImpl.DEBUG = false;
		
		URI uri = URI.create( "ws://localhost:9003" + "?agent=" + clientname );
		
		WSClient c = new WSClient(uri,d);
		
		Thread t = new Thread( c );
		t.start();
		try {
	
				while(true) {
					try {
						c.send("TEST");
						System.out.println("Client Sending...");
						Thread.currentThread().sleep(1000);
					} catch(Exception ex) {
						System.out.println("Caught Ex: "+ex);
					}
				}
			
//			t.join();

		} catch ( Exception e1 ) {
			e1.printStackTrace();
		} finally {
			c.close();
		}
		
	}
		
	@Override
	public void onMessage( String message ) {
		System.out.println("Client receive: "+message);
//		send( message );
	}

	@Override
	public void onMessage( ByteBuffer blob ) {
//		getConnection().send( blob );
	}

	@Override
	public void onError( Exception ex ) {
		System.out.println( "Error: " );
		ex.printStackTrace();
	}

	@Override
	public void onOpen( ServerHandshake handshake ) {
	}

	@Override
	public void onClose( int code, String reason, boolean remote ) {
		System.out.println( "Closed: " + code + " " + reason );
	}

	public void onWebsocketMessageFragment( WebSocket conn, Framedata frame ) {
		FrameBuilder builder = (FrameBuilder) frame;
		builder.setTransferemasked( true );
		getConnection().sendFrame( frame );
	}
}
