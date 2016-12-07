/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lanzador;

import java.io.IOException;

/**
 *
 * @author usuario
 */
public class Lanzador {

    public static void main(String[] args) {


        try {
//          Ejecuta el Productor una vez
            Runtime.getRuntime().exec("java -jar Productor.jar");
            for (int i = 0; i < 100; i++) {
//          Ejecuta el Consumidor 100 veces con un identificador numerico como argumento 0
                Runtime.getRuntime().exec("java -jar Consumidor.jar " + i);
            }
        } catch (IOException e) {
            System.err.println("Error. " + e.toString());
        }
    }
}
