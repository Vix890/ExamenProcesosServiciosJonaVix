package propio;

import java.io.*;
import java.net.*;
import javax.crypto.*;
import java.util.*;


public class Servidor {
    private static final int PUERTO = 5000;
    private static SecretKey clave;
    private static List<ClienteHandler> clientes = new ArrayList<>();
    private static String tipo;

    public static void main(String[] args) {
    	
    	tipo = args[0];
    	
        try (ServerSocket servidor = new ServerSocket(PUERTO);) {
            // Generar clave para cifrado
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            clave = keyGen.generateKey();

            // Crear socket del servidor
            
            System.out.println("Servidor iniciado. Esperando clientes...");

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado desde: " + cliente.getInetAddress().getHostName());

                // Cada cliente en un hilo separado
                ClienteHandler handler = new ClienteHandler(cliente, clave, tipo);
                clientes.add(handler); // Agregar a la lista de clientes
                handler.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para enviar mensaje a todos los clientes
    public static synchronized void enviarMensajeATodos(String mensaje, ClienteHandler origen) {
        for (ClienteHandler cliente : clientes) {
            if (cliente != origen) {
                try {
					cliente.enviarMensaje(mensaje);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
    }

    // Método para eliminar un cliente de la lista
    public static synchronized void removerCliente(ClienteHandler cliente) {
        clientes.remove(cliente);
    }
}
