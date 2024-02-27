package propio;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Cliente {
    private static final int PUERTO = 5000;
    private static String tipo;
    
    private static final String[] ABECEDARIO = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", 
            "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    
    static String[] ArrayCifrar = new String[ABECEDARIO.length];  
    
    public static void selectArryEncriptar(String _op) {
        
        
        final String[] Nums = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
                "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26"};
        
        final String[] Simbols = {"y", "@", "#", "$", "w", "^", "&", "*", "(", ")", "-", "_", "=", "+",
                "{", "}", "[", "]", ":", ";", "<", ">", ",", ".", "?", "0"};


        System.out.println(ABECEDARIO.length);
        System.out.println(Nums.length);
        System.out.println(Simbols.length);
        
        if(_op.equalsIgnoreCase("1")) {

            ArrayCifrar =Arrays.copyOf(Nums, Nums.length);
            
        }if(_op.equalsIgnoreCase("2")) {
            ArrayCifrar =Arrays.copyOf(Simbols, Simbols.length);
        }
        
        
        
    }

    private static final HashMap<String, String> cifradoMap = new HashMap<>();
    private static final HashMap<String, String> descifradoMap = new HashMap<>();
    
    static {
        // Llenar los mapas de cifrado y descifrado
        for (int i = 0; i < ABECEDARIO.length; i++) {
            cifradoMap.put(ABECEDARIO[i], ArrayCifrar[i]);
            descifradoMap.put(ArrayCifrar[i], ABECEDARIO[i]);
        }
    }

    
    public static void main(String[] args) {
    	
    	tipo = args[0];
    	System.out.println(tipo);
    	selectArryEncriptar(tipo);
    	
        try (
        		Scanner t = new Scanner(System.in);
    		) {
        	
        	System.out.println("Introduce la Ip"); 
        	String ip = t.nextLine();
            try (// Conectar al servidor
            		Socket socket = new Socket(ip, PUERTO);
            		Scanner scanner = new Scanner(System.in);
        		) {
				System.out.println("Conectado al servidor.");
				

				// Configurar flujos de entrada/salida
				DataInputStream entrada = new DataInputStream(socket.getInputStream());
				DataOutputStream salida = new DataOutputStream(socket.getOutputStream());

				// Recibir clave del servidor
				int longitudClave = entrada.readInt();
				byte[] claveEncriptada = new byte[longitudClave];
				entrada.readFully(claveEncriptada);

				// Hilo para recibir mensajes del servidor
				Thread thread = new Thread(() -> {
				    try {
				        while (true) {
				            // Recibir mensaje cifrado
				            int longitud = entrada.readInt();
				            byte[] buffer = new byte[longitud];
				            entrada.readFully(buffer);
				            String mensajeCifrado = new String(buffer);
				            
				            // Descifrar mensaje según la regla personalizada
				            String mensaje = descifrarMensajePersonalizado(mensajeCifrado);

				            System.out.println("Mensaje recibido -> " + mensaje);
				            // Mostrar mensaje recibido

				            mensajeCifrado = cifrarMensajePersonalizado(mensaje);
				        }
				    } catch (IOException e) {
				    	System.out.println("Se ha cerrado la sesion ");
				    	e.printStackTrace();
				    }
				});
				thread.start();
				
				while (true) {
					System.out.println("Escribe mensaje:");
				    String mensaje = scanner.nextLine();

				    // Cifrar mensaje según la regla personalizada
				    String mensajeCifrado = cifrarMensajePersonalizado(mensaje);

				    // Enviar mensaje cifrado al servidor
				    salida.writeInt(mensajeCifrado.length());
				    salida.writeBytes(mensajeCifrado);
				}
			}
        } catch (Exception e) {
        	System.out.println("Se ha cerrado la sesion ");
        }
    }

    private static String cifrarMensajePersonalizado(String mensaje) {
        StringBuilder mensajeCifrado = new StringBuilder();
        for (char c : mensaje.toCharArray()) {
            if (c == ' ') {
                mensajeCifrado.append(" ");
            } else {
                // Buscar la posición del carácter en el arreglo de símbolos
                int index = Arrays.asList(ABECEDARIO).indexOf(Character.toString(c).toLowerCase());
                if (index != -1) {
                    mensajeCifrado.append(ArrayCifrar[index]).append(" ");
                } else {
                    // Si el carácter no está en el alfabeto, simplemente lo agregamos tal cual
                    mensajeCifrado.append(c).append(" ");
                }
            }
        }
        return mensajeCifrado.toString().trim();
    }

    private static String descifrarMensajePersonalizado(String mensajeCifrado) {
        StringBuilder mensajeDescifrado = new StringBuilder();

        String[] partes = mensajeCifrado.split(" ");
        for (String parte : partes) {
            if (parte.equals("")) {
                mensajeDescifrado.append(" ");
            } else {
                // Buscar la posición del carácter cifrado en el arreglo de símbolos
                int index = Arrays.asList(ArrayCifrar).indexOf(parte);
                if (index != -1) {
                    mensajeDescifrado.append(ABECEDARIO[index]);
                } else {
                    // Si el carácter cifrado no está en el arreglo de símbolos, lo agregamos tal cual
                    mensajeDescifrado.append(parte);
                }
            }
        }
        return mensajeDescifrado.toString();
    }
    

}

