package br.feevale.peter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import br.feevale.peter.log.Logger;

import comm.Message;

import server.ServerRMI;
import client.ClientRMI;
import exeception.PeterException;

public class Server implements ServerRMI {

	private Integer port;
	private BufferedReader br;
	private Registry r;

	public  void start() {
		prepareToReadConsole();
		
		try{
			readPort();
			registryServer();
			
		}catch(Exception e){
			e.printStackTrace();
		};
	}

	private void registryServer() throws RemoteException, MalformedURLException {
		r = LocateRegistry.createRegistry( port );
		r.rebind( String.format( ServerRMI.FORMAT_URL_SERVER, "localhost", port ), this );
	}

	private void readPort() throws Exception {
		System.out.println( "Informe a porta:" );
		port = Integer.parseInt( ( br.readLine() ).trim() );
	}

	private void prepareToReadConsole() {
		InputStreamReader is = new InputStreamReader( System.in );
		br = new BufferedReader( is );
	}

	@Override
	public void registryClientForCallBack( ClientRMI client ) throws PeterException {
		
		try{
			String url = String.format( ClientRMI.FORMAT_URL_CLIENT, client.getHostname(), client.getPort(), client.getName());
			Naming.rebind( url, client );
		}catch(Exception e){
			throw new PeterException( e.getMessage(), e.getCause() );
		}
	}

	@Override
	public void disconnectClient( ClientRMI client ) throws PeterException {
		try{
			String url = String.format( ClientRMI.FORMAT_URL_CLIENT, client.getName());
			Naming.unbind( url );
		}catch(Exception e){
			throw new PeterException( e.getMessage(), e.getCause() );
		}
	}

	public Integer getPort() {
		return port;
	}

	@Override
	public String[] list() throws PeterException {
		try {
			return r.list();
		} catch( RemoteException e ) {
			Logger.error( e );
			return null;
		}
	}

	@Override
	public void sendMessage( Message msg ) throws PeterException {
		try {
			ClientRMI c = (ClientRMI) Naming.lookup( String.format( ClientRMI.FORMAT_URL_CLIENT, msg.getSender() ) );
			c.receiveMessage( msg );
		} catch( MalformedURLException | RemoteException | NotBoundException e ) {
			Logger.error( e );
		}
	}
}
