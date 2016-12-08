/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lectorsocket;

import java.io.*;
import java.net.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * * @author
 */
public class LectorSocket {
//

    public static void main(String[] args) throws IOException, InterruptedException {

        //      Fichero de logging
        String fichlog = "javalogcons.txt";
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

        //      Fichero para buffer
        String fichruta = "buffer.txt";
//      Indicador de valor de contador leido
        int leido = 0;    // Inicialmente el contador no ha sido leído
        int contador;// valor que es leido delfichero
        boolean acceso = false;
//      Validar argumentos de entrada
        if (!validarArgs(args)) {
            System.out.println("Error en los parámetros de entrada");
            ayuda();
            System.exit(1);
        }
        //      Identificador del proceso
        int idConsumidor = Integer.parseInt(args[0]);

//El consumidor intenta conectar con el canal
        Socket canal = null; //Socket para el canal de conexión con el escritor
        
        while (!acceso) {//mientras que no haya acceso intenta la conexión de nuevo
            try {
                //Pido conexión al equipo a través del puerto 12500, donde escucha el escritor
                canal = new Socket("localhost", 12500);
                acceso = true;
            } catch (Exception e) {
                System.err.println("Error. El consumidor " + idConsumidor + " no ha podido establecer la conexión. Se intenta de nuevo ");
                Thread.sleep(500);// el consumidor 
                System.err.print(e.toString());
            }
        }

//      Bucle dormir a intervalos regulares a la espera de poder leer el valor del contador
// El consumidor 
        while (leido == 0) {
            try {
//          Abrimos el fichero buffer   
                File fbuffer = new File(fichruta);
                RandomAccessFile raf = new RandomAccessFile(fbuffer, "rwd");
                FileChannel channel = raf.getChannel();
                
                System.out.println("Consumidor " + idConsumidor + " --> Entro en Región Crítica");
//          Se hace una lectura segura del fichero para comprobar si podemos
//          leer o no el valor del contador
                if (raf.length() == 0) { // Si el fichero está vacío me pongo a dormir 
                    //esperando un que se escriba en el fichero
//                  System.out.println("Consumidor " + idConsumidor + " --> No puedo leer. Buffer vacio. Espero ...");
                    try {
//                        Se duerme el proceso un segundo
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        System.out.println(ex.toString());
                    }
                } else {      // Si el fichero no está vacío leo el valor del contador
//                  Se posiciona al principio del fichero
                    raf.seek(0);
                    contador = raf.readInt();
                    raf.setLength(0);
//                    Finalizacion del bloqueo  
                   
                    System.out.println("Consumidor " + idConsumidor + " --> Leído un nuevo valor del contador (" + contador + ").");
//                  Cambiamos el estado del indicador "leido"
                    leido = 1;
                }
                channel.close();
                raf.close();
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
        }
        try { 
                canal.close();
            }catch (IOException e){
                System.err.println ("Error de socket");
                System.err.print (e.toString());
            }
    }

    /*
     * Método para validar los argumentos de entrada de main
     */

    private static boolean validarArgs(String[] a) {
        if (a.length == 0 || a.length > 1) // Si el número de argumentos no es 1
        {
            return false;
        }
        try {
            int i;
            i = Integer.parseInt(a[0]);  // Si el argumento no es entero
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static void ayuda() {
        System.out.println("Para invocar este programa: java -jar consumidor.jar <valor_entero>");
    }
}
