
package lectorsocket;

import java.io.*;
import java.net.*;

public class LectorSocket {


    public static void main(String[] args) throws IOException, InterruptedException {
        String streamDeEntrada;
        //      Fichero de logging
        String fichlog = "javalogcons.txt";
        //      Se redirigen las salidas estandar
        PrintStream ps = null;
        BufferedReader bfr;
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
        ps.flush();
//      Indicador de valor de contador leido (mientras que no se lea en el puerto será 0)
        int leido = 0;   
        int contador;// valor que es leido del BufferedReader
        boolean acceso = false;
//      Validar argumentos de entrada
        if (!validarArgs(args)) {
            System.out.println("Error en los parámetros de entrada");
            ayuda();
            System.exit(1);
        }
//      Identificador del proceso que recoge el argumento del lanzador
        int idConsumidor = Integer.parseInt(args[0]);
//      El consumidor intenta conectar con el canal
        Socket canal = null; //Socket para el canal de conexión con el escritor
        while (leido == 0) {
            if (!acceso) {//mientras que no haya acceso intenta la conexión de nuevo
            try {
                //Pido conexión al equipo a través del puerto 12502, donde escucha el escritor
                canal = new Socket("localhost", 12502);
                acceso = true;
            } catch (Exception e) {
                System.err.println("Error. El consumidor " + idConsumidor + " no ha podido establecer la conexión. Se intenta de nuevo ");
                Thread.sleep(500);// el consumidor 
                System.err.print(e.toString());
            }
        }
            try {
                bfr = new BufferedReader(new InputStreamReader(canal.getInputStream()));
                streamDeEntrada = bfr.readLine();
                contador = Integer.parseInt(streamDeEntrada);
                System.out.println("Consumidor " + idConsumidor + " --> Lee el valor del contador (" + contador + ").");
//              Cambiamos el estado del indicador "leido"
                leido = 1;
                bfr.close();
            } catch (IOException ex) {
                System.out.println(ex.toString());
                leido=0;
                acceso=false;
            }
        }
        try {
            canal.close();
        } catch (IOException e) {
            System.err.println("Error de socket");
            System.err.print(e.toString());
        }
    }
  
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
