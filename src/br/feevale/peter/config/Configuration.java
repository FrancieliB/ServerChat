package br.feevale.peter.config;

import java.io.InputStream;
import java.util.Properties;

public class Configuration {

	private static Configuration INSTANCE;
	private static final String INTERNAL_SRC_FOLDER = "../../../../..";
	
	private int port;
	private String rootFolder;
	
	private Configuration(){
		new Configuration();
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getRootFolder() {
		return rootFolder;
	}
	
	public void setRootFolder( String rootFolder ) {
		this.rootFolder = rootFolder;
	}
	
	public static Configuration getInstance() throws Exception {
		if (INSTANCE == null) {
			try {
				InputStream internalProperties = getInternalResourceAsStream( "config.properties" );
				
				Properties properties = new Properties();
				properties.load( internalProperties );
				
				INSTANCE = new Configuration();
				INSTANCE.setPort( Integer.parseInt( properties.getProperty( "server.port", "4000" ) ) );
				INSTANCE.setRootFolder( properties.getProperty( "server.rootFolder", "C:/Temp" ) );
			} catch ( Exception e ) {
				throw new Exception( e );
			}
		}
		
		return INSTANCE;
	}

	private static InputStream getInternalResourceAsStream( String name ) {
		return Configuration.class.getResourceAsStream( String.format( "%s/%s", INTERNAL_SRC_FOLDER, name ) );
	}
}