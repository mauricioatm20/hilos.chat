package hilos.servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerChat {

	private int puerto;
	
	public ServerChat(int puerto) {
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
		private BufferedReader in;
		private PrintWriter out;
		private String nick;
		private boolean sesion;
		 							 // es lo mismo que = new ConcurrentHashMap<>();
		private static Map<String, AtiendeCliente> sala = new ConcurrentHashMap<String, ServerChat.AtiendeCliente>();
		private static int cant;	  	
		
		public AtiendeCliente(Socket socket) {
			this.socket=socket;
			start();
		}
		
		
		public void run() {
			System.out.println("nuevo cliente conectado");
			 
			try(BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					PrintWriter pw = new PrintWriter(socket.getOutputStream(),true))
			{
				in = br;
				out = pw;
				
				nuevoCliente();
		
				boolean sesion= true;
				String leido;
				
				while(sesion && (leido = in.readLine()) !=null) {
					
					if(leido.length() >0 ) {
					switch(leido.charAt(0)) {
					case '-':
						//analizar comando
						comando(leido);
						break;
					case '@':
						//enviar mensaje privado
						mensajePrivado(leido);
						break;
					default :
						//mensaje de difusion 
						difusion(nick + ": " + leido);
						break;

					}
				}
			}
				
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}


		private void mensajePrivado(String leido) {
			if(leido.contains(" ")) {
				String nickDest = leido.substring(1, leido.indexOf(" "));
				String mnsj = leido.substring(leido.indexOf(" " + 1 ));
				if(sala.containsKey(nick)) {
					sala.get(nickDest).out.println("privado de " + nick + ": "+ mnsj );//esto llama al objeto AtiendeCliente
				}else {
					out.println("SVR: "+ nickDest + "destino no conectado");
				}
			}else {
				out.println("SVR: comando incorrecto" );
				
			}
		}


		private void comando(String leido ) {

			switch(leido) {
			case "-w":
				for(String usr : sala.keySet()) {
					out.println(usr);
				}
				break;
			case "-q":
				out.println("ADIOS");
				sesion = false;
				System.out.println("cliente desconectado");
				difusion(nick + " se ha desconectado");
				sala.remove(nick);
				cant--;
				break;
			default:
				out.println("comando erroneo");				
			case "-h":
				help();
				break;
				
			}

			
		}


		private void help() {

			out.println("Ayuda Chat");
			out.println("-q: salir");
			out.println("-w: usuarios en sala");
			out.println("-h ayuda");
			out.println("@usuario: mensaje privado");
			
		}


		private void nuevoCliente() throws IOException {
			out.println("hola......welcome to new chat");
			
			do {
				out.println("por favor dime tu nick");
				nick = in.readLine();
			} while(sala.containsKey(nick));
			out.println("ya estas conectado ");
			sala.put(nick, this);
			cant++;
			
			System.out.println(nick + " se ha incorporado");
			System.out.println(cant + " usuarios en la sala");
			
			difusion("SRV: " + nick + " se ha incorporado a la sala");
		}
		
		private void difusion(String msj) {
			for(AtiendeCliente cli : sala.values()) {
				cli.out.println(msj);
			}
		}
	}
	
	
	public static void main(String[] args) {
		new ServerChat(21);//21 es el puerto se puede elegir cualquiera 
	}
}
