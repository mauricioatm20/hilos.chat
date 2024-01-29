package hilos;

public class T03 extends Thread {

	public String mensaje;
	public T03(String mensaje ) {
		this.mensaje= mensaje;
	}
//	 todo lo que yo quiero que se ejecute se coloca en la clase run
	public void run() {
		while(true) {
			System.out.println(mensaje);
		}
	}
//	start() crea un nuevo hilo y se ejecuta en run
	
	public static void main(String[] args) {
		T03 h1 = new T03("soy el hilo 111111111111111");
		h1.start();
		T03 h2 = new T03("soy el hilo 2222222");
		h2.start();
		T03 h3 = new T03("soy el hilo 33333333");
		h3.start();
	}
}
