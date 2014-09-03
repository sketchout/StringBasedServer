package org.javagamesfactory.nioservers;

import java.nio.channels.SocketChannel;
import java.util.LinkedList;

import com.macyoo.handler.KeyMessage;

public class ManageList {

	
	public static int defaultMaxUser = 5000;
	
	public LinkedList<SocketChannel> acceptedSocketChannelList;
	
	public  LinkedList<SocketChannel> connectedSocketChannelList;
	
	public ManageList() {
		
		acceptedSocketChannelList = new LinkedList<SocketChannel>();
		
		connectedSocketChannelList = new LinkedList<SocketChannel>();
	}
	
}
