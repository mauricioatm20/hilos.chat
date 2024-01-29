package hilos;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class T01Ventana extends JFrame{
	public T01Ventana() {
		setBounds(100,100, 400, 600);
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
		
			@Override
			public void windowClosing(WindowEvent e) {
			System.exit(0);

			}
		});
	}
	
	public static void main(String[] args) {
		T01Ventana v= new T01Ventana();
//		System.out.println("el main ha finalizado");
		
		int algo =0;
		while (true){
			
			System.out.println(++algo);
		}
	}

}
