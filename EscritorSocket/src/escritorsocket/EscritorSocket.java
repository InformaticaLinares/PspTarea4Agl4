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

//      Fichero de logging
        String fichlog = "javalogsum.txt";

//      Se redirigen las salidas estandar
        PrintStream ps = null;
        try {
            ps = new PrintStream(
                    new BufferedOutputStream(new FileOutputStream(
                            new File(fichlog), true)), true);
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
        } finally {
            System.setOut(ps);
            System.setErr(ps);
        }
//Creo el server socket para la conexion de los consumidosres
//      Fichero para buffer
        String fichruta = "buffer.txt";
//      Contador del proceso
        int contador = 0;
        ServerSocket conexion = null; //Socket para aceptar conexiones
        Socket canal = null;   // Socket para establecer el canal
        PrintWriter salidaStream; //Stream de salida para el socket
        try {
            conexion = new ServerSocket(12502);
            //Pido al SO que abra el puerto 12500 para la escucha de la conexion
        } catch (IOException e) {
            System.err.println("No se puede abrir el puerto");
            System.err.print(e.toString());
            System.exit(-1);
        }

        while (contador < 100) {
            try {
                System.out.println("Productor esperando conexión de consumidor");
                canal = conexion.accept(); //productor espera que se conecte un consumidor
         //     salidaStream = new PrintWriter(canal.getOutputStream());
         //     Abrimos el fichero buffer   
                File fbuffer = new File(fichruta);
                RandomAccessFile raf = new RandomAccessFile(fbuffer, "rwd");
                FileChannel channel = raf.getChannel();
                FileLock lock = channel.lock();
                System.out.println("Suministrador--> Entro en Región Crítica");
//              Se sebreescribe el contador, para lo que nos ponemos al principio del fichero
                raf.seek(0);
                raf.writeInt(contador);
                //Finalizacion del bloqueo de escritura en el fichero  
                lock.release();
                System.out.println("Suministrador--> Nuevo valor del contador escrito (" + contador + ") grabado. Salgo de la R.C.");
//              Aumentamos el valor del contador
                contador++;
                channel.close();
                raf.close();
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
}
