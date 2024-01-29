package hilos;

public class T04 implements Runnable{

	public String mensaje;
	public T04(String mensaje ) {
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
		T04 h1 = new T04("soy el hilo 111111111111111");
		Thread t1 = new Thread(h1);
		t1.start();
		
		T04 h2 = new T04("soy el hilo 2222222");
		Thread t2 = new Thread(h2);
		t2.start();
		
//		T04 h3 = new T04("soy el hilo 33333333");
//		Thread t3 = new Thread(h3);
//		t3.start();
//		
		new Thread(new T04("SOY EL HILO 333333")).start();
	}
}
