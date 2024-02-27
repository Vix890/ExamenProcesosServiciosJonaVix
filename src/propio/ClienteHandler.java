package propio;

import java.io.*;
import java.net.*;
import javax.crypto.*;
import java.util.*;

public class ClienteHandler extends Thread {

	private final String COMANDO_TERMINACION = "cortaya";
	private String tipo;
    private Socket cliente;
    private SecretKey clave;
    private DataInputStream entrada;
    private DataOutputStream salida;
    private static final String[] ABECEDARIO = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", 
            "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    
    static String[] ArrayCifrar = new String[ABECEDARIO.length];  
    
    public static void selectArryEncriptar(String _op) {
        
        
        final String[] Nums = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
                "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28"};
        
        final String[] Simbols = {"y", "@", "#", "$", "w", "^", "&", "*", "(", ")", "-", "_", "=", "+",
                "{", "}", "[", "]", ":", ";", "<", ">", ",", ".", "?", "0"};



        
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

    private static List<ClienteHandler> clientesConectados = new ArrayList<>();

    public ClienteHandler(Socket socket, SecretKey clave, String _tipo) {
        this.cliente = socket;
        this.clave = clave;
        this.tipo = _tipo;
    }

    public void run() {
    	
    	System.out.println(tipo);
    	selectArryEncriptar(tipo);
        try {
            // Configurar flujos de entrada/salida
            entrada = new DataInputStream(cliente.getInputStream());
            salida = new DataOutputStream(cliente.getOutputStream());

            // Enviar clave al cliente
            byte[] claveEncriptada = clave.getEncoded();
            salida.writeInt(claveEncriptada.length);
            salida.write(claveEncriptada);

            // Agregar este cliente a la lista de clientes conectados
            clientesConectados.add(this);

            while (true) {
                // Recibir mensaje cifrado
                int longitud = entrada.readInt();
                byte[] buffer = new byte[longitud];
                entrada.readFully(buffer);
                String mensajeCifrado = new String(buffer);

                // Descifrar mensaje según la regla personalizada
                String mensaje = descifrarMensajePersonalizado(mensajeCifrado);
                System.out.println("JONA "+mensaje);
                if(mensaje.equalsIgnoreCase(COMANDO_TERMINACION)) {
                	System.out.println("VIX");
                	entrada.close();
                	salida.close();
                	cliente.close();
                }else {
                	
	                // Mostrar mensaje recibido
                	System.out.println("Mensaje CIFRADO " + cliente.getInetAddress().getHostName() + ": " + mensajeCifrado);
	                System.out.println("Mensaje DESCIFRADO " + cliente.getInetAddress().getHostName() + ": " + mensaje);
	
	                // Enviar mensaje a todos los clientes excepto al que lo envió
	                enviarMensajeATodos(mensaje);
	                }
                }
        } catch (Exception e) {
            // Manejar la desconexión del cliente
            System.err.println("Cliente desconectado: " + cliente.getInetAddress().getHostName());
        } finally {
            try {
                cliente.close();
            } catch (IOException e) {
                
            }
            clientesConectados.remove(this);
            
        }
        
    }

    // Método para enviar un mensaje cifrado al cliente
    public void enviarMensaje(String mensaje) throws IOException {
        // Cifrar mensaje según la regla personalizada
        String mensajeCifrado = cifrarMensajePersonalizado(mensaje);

        // Enviar mensaje cifrado al cliente
        salida.writeInt(mensajeCifrado.length());
        salida.writeBytes(mensajeCifrado);
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



    // Método para enviar un mensaje a todos los clientes conectados excepto a este cliente
    private void enviarMensajeATodos(String mensaje) throws IOException {
        for (ClienteHandler cliente : clientesConectados) {
            if (cliente != this) {
                cliente.enviarMensaje(mensaje);
            }
        }
    }
}
