package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorFlotaRMI {
	public static void main(String args[]) {
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        String portNum, registryURL;


        try {
            System.out.println("Enter the RMIregistry port number:");
            portNum = (br.readLine()).trim();
            int RMIPortNum = Integer.parseInt(portNum);


            // start a security manager - this is needed if
            // stub downloading is in use for this application.
            // The following sentence avoids the need to use
            // the option -DJava.security.policy=..." when launching the clinet
            //System.setProperty("java.security.policy", "src/server/java.policy");
            //System.setSecurityManager(new SecurityManager());

            startRegistry(RMIPortNum);

            ImplServidorJuegoRMI exportedObj = new ImplServidorJuegoRMI();
            registryURL = "rmi://localhost:" + portNum + "/JuegoRMI";
            Naming.rebind(registryURL, exportedObj);
            System.out.println("Server registered. Registry contains:");
            // list names currently in the registry
            listRegistry(registryURL);
            System.out.println("JuegoRMI Server ready.");
        }// end try
        catch (Exception re) {
            System.out.println("Exception in ImplServidorJuegoRMI.main: " + re);
        } // end catch
    } // end main

    // This method starts a RMI registry on the local host, if
    // it doesn't already exists at the specified port number.
    private static void startRegistry(int RMIPortNum) throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(RMIPortNum);
            registry.list();  // This call will throw an
            //exception if the registry does not already exist
        } catch (RemoteException e) {
            // No valid registry at that port.
            System.out.println("RMI registry cannot be located at port " + RMIPortNum);
            Registry registry = LocateRegistry.createRegistry(RMIPortNum);
            System.out.println("RMI registry created at port " + RMIPortNum);
        }
    } // end startRegistry

    //This method lists the names registered with a Registry
    private static void listRegistry(String registryURL) throws RemoteException, MalformedURLException {
        System.out.println("Registry " + registryURL + " contains: ");
        String[] names = Naming.list(registryURL);
        for (int i = 0; i < names.length; i++)
            System.out.println(names[i]);
    } //end listRegistry

} // end class