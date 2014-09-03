package org.javagamesfactory.nioservers;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.macyoo.util.LogHandler;

public class ThreadAccept extends Thread {
	
	protected LogHandler logHandler;
	
	protected Selector selector;
	ServerSocketChannel ssc;
	
	int targetPort;
	ManageList manageSocket;
	Logger logger;
	
	public ThreadAccept(int targetPort , ManageList msc ) {
		
		this.targetPort = targetPort;
		this.manageSocket = msc;
		
		logger = Logger.getLogger( getClass() );
		
		// logHandler
		logHandler = new LogHandler(logger);
		logHandler.SetPort(targetPort);
	
	}

	public void run() {
		try {
			synchronized( this ) {
				doStart();
			}
		} 
		catch( BindException e )
		{
			logHandler.error( "Attempted to bind to port " 
					+ targetPort + " which is already in use; server going OFFLINE", e );
			return;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	void doStart() throws IOException {

		logHandler.error( "Binding to port " + targetPort + "..." );
		
		ssc = ServerSocketChannel.open();
		ssc.configureBlocking( false );	// Un-Blocking
		ssc.socket().bind( new InetSocketAddress( targetPort ) );
		
		// selector
		selector = Selector.open();
		ssc.register( selector, SelectionKey.OP_ACCEPT );
		
		logHandler.error( "selector, SelectionKey.OP_ACCEPT" );
		
		while ( true ) {
			
			selector.select( 1 );	// timeout : 1
			
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = keys.iterator();
			
			while( iterator.hasNext() ) {
				
				SelectionKey key = null;
				
				key = iterator.next();
				iterator.remove(); // 
				
				//-------------------------------------------------------------						
				if( key.isAcceptable() )  // ready to accept a new socket connection
				{
					// Check about the Max Connection
					if(! this.getShouldAllowNewConnection() ) {
						logHandler.error("Client wants to connect ... But already exceeds MAX");
						key.channel().close();
						continue; 
					}
					
					//infoVerbose( "  ACCEPTABLE key; accepting and adding new SocketChannel to selector for READ only" );
					//logHandler.error(" ACCEPTABLE key; accepting and adding new SocketChannel to selector for READ only" );
					
					// Accept 
					SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
					
					//sc.configureBlocking( false );
					//sc.socket().setTcpNoDelay( true ); // stop Nagling, send all data immediately
					//sc.register( selector, SelectionKey.OP_READ );
					
					
					// add to LinkedList Socket Channel
					synchronized(manageSocket.acceptedSocketChannelList ) {
						
						// ManageSocketChannel
						manageSocket.acceptedSocketChannelList.offer( sc );	
					}
					
				}
			}
		}
	}
	
	public boolean getShouldAllowNewConnection() {
		
		if ( this.getNumberOfConnectedChannels() > ManageList.defaultMaxUser ) {
			logHandler.error( "size: "+ this.getNumberOfConnectedChannels() );
			return false;
		}
		return true;
	}
	
	protected int getNumberOfConnectedChannels()
	{
		if ( ! manageSocket.acceptedSocketChannelList.isEmpty() ) {
			synchronized(manageSocket.acceptedSocketChannelList ) {
				return manageSocket.connectedSocketChannelList.size();
			}
			
		} else {
			return 0;
		}
	}
	

}
