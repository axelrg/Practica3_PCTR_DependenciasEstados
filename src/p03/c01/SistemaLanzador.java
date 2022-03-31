package src.p03.c01;

public class SistemaLanzador {
	public static void main(String[] args) {
		
		IParque parque = new Parque();
		char letra_puerta = 'A';
		
		System.out.println("¡Parque abierto!");


		for (int i = 0; i < Integer.parseInt(args[0]); i++) {
		//for (int i = 0; i < 5; i++) {
			
			String puerta = ""+((char) (letra_puerta++));
			
			// Creación de hilos de entrada
			ActividadEntradaPuerta entradas = new ActividadEntradaPuerta(puerta, parque);
			new Thread (entradas).start();

			//Hilos de Salida
			ActividadSalidaPuerta salidas = new ActividadSalidaPuerta(puerta, parque);
			new Thread (salidas).start();
		}
	}	
}