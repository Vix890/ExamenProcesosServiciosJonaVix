package main;

import java.io.IOException;
import java.util.Scanner;

import utils.SODetector;


public class Main {

    public static void main(String[] args) {

        Scanner t = new Scanner(System.in);

        int opcion;

        SODetector detector = new SODetector();

        String sistema = detector.detectarSO();

        do {

            System.out.println("=======================");
            System.out.println("=         " + sistema + "         =");
            System.out.println("=======================");
            System.out.println("=  1) Cifrado 1       =");
            System.out.println("=  2) Cifrado 2       =");
            System.out.println("=  0) Salir           =");
            System.out.println("=======================");

            System.out.println("Introduzca una opcion");

            opcion = t.nextInt();
            switch (opcion) {

                case 0:
                    System.out.println("Adios!!");
                    t.close();
                    System.gc();
                    System.exit(0);
                    break;

                case 1:
                    iniciar(sistema, 1);
                    break;

                case 2:
                    iniciar(sistema, 2);
                    break;

                default:
                    System.out.println("Introduce un valor válido");
            }

        } while (opcion != 0);
    }

    private static void iniciar(String _sistema, int _dist) {

        String comando = "";

        if (_dist == 1) {
        	comando = String.format("java -cp bin propio.MenuCifrado %s", 1);
        } else {
            comando = String.format("java -cp bin propio.MenuCifrado %s", 2);
        }

        try {

            if (_sistema.equals("win")) {
                Runtime.getRuntime().exec(new String[] { "cmd.exe", "/K start cmd.exe /K " + comando });
            } else if (_sistema.equals("lin")) {

                Runtime.getRuntime().exec(new String[] { "sh", "-c", "gnome-terminal -- " + comando });

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
