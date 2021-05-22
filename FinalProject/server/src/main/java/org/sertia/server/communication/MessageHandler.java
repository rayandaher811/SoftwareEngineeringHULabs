package org.sertia.server.communication;

import java.io.IOException;

public class MessageHandler extends AbstractServer {

	public MessageHandler(int port) {
		super(port);
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		System.out.println("Received Message: " + msg.toString());
		sendToAllClients(msg);
	}
	
	
	
	@Override
	protected synchronized void clientDisconnected(ConnectionToClient client) {
		// TODO Auto-generated method stub
		
		System.out.println("Client Disconnected.");
		super.clientDisconnected(client);
	}
	
	

	@Override
	protected void clientConnected(ConnectionToClient client) {
		super.clientConnected(client);
		System.out.println("Client connected: " + client.getInetAddress());
	}

	public void startListening() throws IOException {
		listen();
	}
//
//	public static void main(String[] args) throws IOException {
//		if (args.length != 1) {
//			System.out.println("Required argument: <port>");
//		} else {
//			MessageHandler server = new MessageHandler(Integer.parseInt(args[0]));
//			server.listen();
//		}
//	}
}
