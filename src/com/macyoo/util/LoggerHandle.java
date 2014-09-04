package com.macyoo.util;

import org.apache.log4j.Logger;

public class LoggerHandle {
	
	Logger logger ;
	Thread thread;
	int port;

	public LoggerHandle(Logger logger) {
		
		this.logger = logger;
		this.thread = null;
		this.port = 0;
	}
	public void SetThread(Thread thread) {
		this.thread = thread;
	}
	public void SetPort(int port) {
		this.port = port;
	}
	
	/**
	 * Works out the thread and port and prepends them to the log message before logging it to the
	 * standard logger
	 * 
	 * @param s
	 */
	public void error( String s ) {
		logger.error( "[" + ((thread == null) ? "NULL" : thread.getName()) 
				+ ":" + port + "] " + s );
	}
	
	/**
	 * Works out the thread and port and prepends them to the log message before logging it to the
	 * standard logger
	 * 
	 * @param s
	 */
	public void debug( String s )
	{
		logger.debug( "[" + ((thread == null) ? "NULL" : thread.getName()) 
				+ ":" + port + "] " + s );
	}
	

	/**
	 * Works out the thread and port and prepends them to the log message before logging it to the
	 * standard logger
	 * 
	 * @param s
	 */
	public void info( String s )
	{
		logger.info( "[" + ((thread == null) ? "NULL" : thread.getName()) 
				+ ":" + port + "] " + s );
	}
	
	/**
	 * Works out the thread and port and prepends them to the log message before logging it to the
	 * standard logger
	 * 
	 * @param s
	 */
	/*
	protected void debugVerbose( String s )
	{
		verboseLogger.debug( "[" + ((thread == null) ? "NULL" : thread.getName())
		 + ":" + port + "] " + s );
	}
	*/	
	/**
	 * Works out the thread and port and prepends them to the log message before logging it to the
	 * standard logger
	 * 
	 * @param s
	 */
	/*
	protected void infoVerbose( String s )
	{
		verboseLogger.info( "[" + ((thread == null) ? "NULL" : thread.getName()) 
		+ ":" + port + "] " + s );
	}
	*/
	/**
	 * Works out the thread and port and prepends them to the log message before logging it to the
	 * standard logger
	 * 
	 * @param s
	 */
	public void info( String s, Throwable t )
	{
		logger.info( "[" + ((thread == null) ? "NULL" : thread.getName())
				+ ":" + port + "] " + s, t );
	}
	

	/**
	 * Works out the thread and port and prepends them to the log message before logging it to the
	 * standard logger
	 * 
	 * @param s
	 */
	public void error( String s, Throwable t )
	{
		/*
		logger.error( "[" + ((thread == null) ? "NULL" : thread.getName()) 
			+ ":" + port + "] " + s, t );
		*/
		logger.error(  s, t );
	}
}
