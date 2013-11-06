package br.feevale.peter;

import br.feevale.peter.log.Logger;

public class ServerStarter {

	public static void main( String[] args ) {
		try {
			Logger.info( "Iniciando Servidor..." );
			
			Server server = new Server();
			server.start();
			
			Logger.info( "Servidor iniciado!" );
			Logger.info( "Servidor escutando na porta %d...", server.getPort() );
		} catch ( Exception e ) {
			Logger.error( e );
		}
	}
}