package propio;

import java.io.IOException;
import java.util.Scanner;

import utils.SODetector;

public class MenuCifrado {
	
	private static String tipo;
	
	public static void main(String[] args) {
		
		tipo = args[0];
		
		Scanner t = new Scanner(System.in);
				
		String opcion;
		
		SODetector detector = new SODetector();
		
		String sistema = detector.detectarSO();
		
		do {
			
			System.out.println("=============================");
			System.out.println("=            " + sistema + "            =");
			System.out.println("=============================");
			System.out.println("=  1) Servidor              =");
			System.out.println("=  2) Cliente               =");
			System.out.println("=  3) Servidor y Cliente    =");
			System.out.println("=  0) Salir                 =");
			System.out.println("=============================");
			
			System.out.println("Introduzca una opcion");
			
			opcion = t.nextLine();
			
			switch (opcion) {
			
			case "0": 
				System.out.println("Adios!!");
				t.close();
				System.gc();
				System.exit(0);
				break;
				
			case "1": 
				iniciar(sistema, 1);
				break;
				
			case "2": 
				iniciar(sistema, 2);
				break;
			
			case "3": 
				iniciar(sistema, 1);
				iniciar(sistema, 2);
				break;
				
			default:
				System.out.println("Introduce un valor válido");
			}
			
		} while (opcion != "0");
	}
	
	private static void iniciar(String _sistema, int _dist) {
		
		String comando = "";
		
		if (_dist == 1) {
			comando = String.format("java -cp bin propio.Servidor %s", tipo);	
		}
		else {
			comando = String.format("java -cp bin propio.Cliente %s", tipo);	
		}

		try {
					
			if (_sistema.equals("win")) {
	            Runtime.getRuntime().exec(new String[] { "cmd.exe", "/K start cmd.exe /K "+comando});		
			} else if (_sistema.equals("lin")) {
												
				Runtime.getRuntime().exec(new String[] { "sh", "-c", "gnome-terminal -- " +comando});		
								
			} else if (_sistema.equals("mac")) {
				
				System.out.println("mac");
				
			} else {
				System.out.println("Error!!!");
			}
            
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
