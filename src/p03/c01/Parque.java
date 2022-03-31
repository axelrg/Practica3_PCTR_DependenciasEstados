package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;

public class Parque implements IParque{

	private int contadorPersonasTotales;
	private Hashtable<String, Integer> contadoresPersonasPuerta;
	private static final int AFOROMAX = 50;
	
	
	public Parque() {
		contadorPersonasTotales = 0;
		contadoresPersonasPuerta = new Hashtable<String, Integer>();
	}


	@Override
    public synchronized void entrarAlParque(String puerta){

        // Si no hay entradas por esa puerta, inicializamos
        contadoresPersonasPuerta.putIfAbsent(puerta, 0);

        comprobarAntesDeEntrar();


        // Aumentamos el contador total y el individual
        contadorPersonasTotales++;
        contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)+1);

        checkInvariante();


        // Imprimimos el estado del parque
        imprimirInfo(puerta, "Entrada");

        //No utilizamos all ya que solamente va a entrar o salir uno despues de entrar o salir uno
        this.notify();

    }


	@Override
	public synchronized void salirDelParque(String puerta){
		// Si no hay entradas por esa puerta, inicializamos
		contadoresPersonasPuerta.putIfAbsent(puerta, 0);

		comprobarAntesDeSalir();


		// Decrementamos el contador total y el individual
		contadorPersonasTotales--;
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)-1);

		checkInvariante();


		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Salida");

		this.notify();

	}
	
	private void imprimirInfo (String puerta, String movimiento){
		System.out.println(movimiento + " por puerta " + puerta);
		System.out.println("--> Personas en el parque " + contadorPersonasTotales); //+ " tiempo medio de estancia: "  + tmedio);
		
		// Iteramos por todas las puertas e imprimimos sus entradas
		for(String p: contadoresPersonasPuerta.keySet()){
			System.out.println("----> Por puerta " + p + " " + contadoresPersonasPuerta.get(p));
		}
		System.out.println(" ");
	}
	
	private int sumarContadoresPuerta() {
		int sumaContadoresPuerta = 0;
			Enumeration<Integer> iterPuertas = contadoresPersonasPuerta.elements();
			while (iterPuertas.hasMoreElements()) {
				sumaContadoresPuerta += iterPuertas.nextElement();
			}
		return sumaContadoresPuerta;
	}
	
	protected void checkInvariante() {
		assert sumarContadoresPuerta() == contadorPersonasTotales : "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";
		assert sumarContadoresPuerta() <= AFOROMAX : "Se ha superado el aforo de 50 personas";
		assert contadorPersonasTotales >= 0: "No puede haber personas negativas en el parque";
	}

	protected void comprobarAntesDeEntrar(){
        while (contadorPersonasTotales >= AFOROMAX){
            try {
                wait();
            }catch (InterruptedException exc){
                exc.printStackTrace();
            }
        }
    }

	protected void comprobarAntesDeSalir(){
		while (contadorPersonasTotales <= 0){
			try {
				wait();
			}catch (InterruptedException exc){
				exc.printStackTrace();
			}
		}
	}


}
