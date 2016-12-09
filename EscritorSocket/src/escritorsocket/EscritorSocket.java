
package escritorsocket;

import java.net.*;
import java.io.*;

public class EscritorSocket {//este es el proceso productor

    public static void main(String[] args) throws FileNotFoundException, IOException {


//      Contador del proceso
        int contador = 0;
//Creo el server socket para la conexion de los consumidosres
        ServerSocket conexion = null; //Socket para aceptar conexiones
        Socket canal = null;   // Socket para establecer el canal
        PrintWriter salidaStream; //Stream de salida para el socket

        while (contador < 100) {
            try {
                conexion = new ServerSocket(12502);
                //Pido al SO que abra el puerto 12502 para la escucha de la conexion
            } catch (IOException e) {
                System.err.println("No se puede abrir el puerto");
                System.err.print(e.toString());
                System.exit(-1);
            }
            try {
                canal=conexion.accept();//se acepta conexión en el canal (esto lo hara tantas veces como se repita el while:100)
                salidaStream = new PrintWriter(canal.getOutputStream());
                salidaStream.println(contador);
                contador++;

                salidaStream.flush();
                salidaStream.close();
                canal.close(); //cierro canal, puede elevar IOException
                conexion.close();
            } //cierro el ServerSocket
            catch (IOException ex) {
                System.out.println(ex.toString());
            } catch (Exception e) {
                System.err.println("Error de conexión o al escribir en el canal");
                System.err.print(e.toString());
            }
        }

    }
}
