/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escritorsocket;

/**
 * * @author
 */
import java.net.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Random;

public class EscritorSocket {//este es el proceso productor

    public static void main(String[] args) throws FileNotFoundException, IOException {

//Creo el server socket para la conexion de los consumidosres
//      Fichero para buffer
        
//      Contador del proceso
        int contador = 0;
        ServerSocket conexion = null; //Socket para aceptar conexiones
        Socket canal = null;   // Socket para establecer el canal
        PrintWriter salidaStream; //Stream de salida para el socket

        while (contador < 100) {
            try {
                conexion = new ServerSocket(12502);
                //Pido al SO que abra el puerto 12500 para la escucha de la conexion
            } catch (IOException e) {
                System.err.println("No se puede abrir el puerto");
                System.err.print(e.toString());
                System.exit(-1);
            }
            try {

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
