
package lanzador;

import java.io.IOException;

public class Lanzador {

    public static void main(String[] args) {


        try {
//          Ejecuta el Productor una vez
            Runtime.getRuntime().exec("java -jar EscritorSocket.jar");
            for (int i = 0; i < 100; i++) {
//          Ejecuta el Consumidor 100 veces con un identificador numerico como argumento 0
                Runtime.getRuntime().exec("java -jar LectorSocket.jar " + i);
            }
        } catch (IOException e) {
            System.err.println("Error. " + e.toString());
        }
    }
}
