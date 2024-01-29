package hilos.servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server1 {

	private int puerto;
	
	public Server1(int puerto) {
		this.puerto = puerto;
		iniciar();
		
	}
	private void iniciar() {
		try(ServerSocket server = new ServerSocket(puerto)){
			System.out.println("servidor arrancando puerto: " + puerto);
			while(true) {
				Socket cliente = server.accept();
				new AtiendeCliente(cliente);
//				AtiendeCliente cliente = new AtiendeCliente(server.accept());
			}
		}catch (IOException e) {
			e.printStackTrace();
			System.err.println("no se puedo abrir puerto: "+ puerto);
		}
	}
	
	private class AtiendeCliente extends Thread{
		private Socket socket;
		
		public AtiendeCliente(Socket socket) {
			this.socket=socket;
			start();
		}
		public void run() {
			System.out.println("nuevo cliente conectado");
			
			try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					PrintWriter out = new PrintWriter(socket.getOutputStream(),true))
			{
				out.println("bienvenido al servidor");
				
				boolean sesion= true;
				String leido;
				while(sesion && (leido = in.readLine()) !=null) {
					out.println("SRV: " + leido);
					
					if(leido.equals("-q")) {
						out.println("ADIOS");
						sesion = false;
						System.out.println("cliente desconectado");
						
					}
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}
	}
	
	public static void main(String[] args) {
		new Server1(21);//21 es el puerto se puede elegir cualquiera 
	}
}
