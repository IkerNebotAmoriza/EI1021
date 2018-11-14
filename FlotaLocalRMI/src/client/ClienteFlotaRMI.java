package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;

import common.IntServidorJuegoRMI;
import common.IntServidorPartidasRMI;

public class ClienteFlotaRMI {
	public static void main(String args[]) {
        try {
            int RMIPort;
            String hostName;
            InputStreamReader is = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(is);
            System.out.println("Enter the RMIRegistry host name:");
            hostName = br.readLine();
            System.out.println("Enter the RMIregistry port number:");
            String portNum = br.readLine();
            RMIPort = Integer.parseInt(portNum);

            // start a security manager - this is needed if stub
            // downloading is in use for this application.
            // The following sentence avoids the need to use
            // the option -DJava.security.policy=..." when launching the client
            System.setProperty("java.security.policy", "HelloWorldRMI/src/client/java.policy");
            System.setSecurityManager(new SecurityManager());

            String registryURL = "rmi://localhost:" + portNum + "/JuegoRMI";
            // find the remote object and cast it to an interface object
            IntServidorJuegoRMI juego = (IntServidorJuegoRMI) Naming.lookup(registryURL);

            System.out.println("Lookup completed ");
            // invoke the remote method
            IntServidorPartidasRMI partida = juego.nuevoServidorPartidas();
        } // end try
        catch (Exception e) {
            System.out.println(
                    "Exception in HelloClient: " + e);
        }
    } //end main
}//end class
