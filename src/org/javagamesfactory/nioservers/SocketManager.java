package org.javagamesfactory.nioservers;

import java.nio.channels.SocketChannel;
import java.util.LinkedList;

import com.macyoo.servers.SelectionKeyMessage;

public class SocketManager {

	
	public static int defaultMaxUser = 5000;
	
	public LinkedList<SocketChannel> acceptedSocketChannelList;
	
	public  LinkedList<SocketChannel> connectedSocketChannelList;
	
	public SocketManager() {
		
		acceptedSocketChannelList = new LinkedList<SocketChannel>();
		
		connectedSocketChannelList = new LinkedList<SocketChannel>();
	}
	
}
