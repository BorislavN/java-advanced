package streams_files_dirs.sandbox.multicast_chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class InterfaceLister {
    public static void main(String[] args) throws IOException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface currentInterface = interfaces.nextElement();

            System.out.println(currentInterface.supportsMulticast());
            System.out.println(currentInterface.getIndex());
            System.out.println(currentInterface.getName());
            System.out.println(currentInterface.getDisplayName());

            System.out.println("Addresses");

            Enumeration<InetAddress> inetAddresses = currentInterface.getInetAddresses();

            while (inetAddresses.hasMoreElements()) {
                InetAddress address = inetAddresses.nextElement();

                System.out.println(address.getHostName());
                System.out.println(address.getHostAddress());
            }

            System.out.println();
        }

        //Can be used to find devices connected to your lan ( those which IP starts with "192.168.0")
//        checkHosts("192.168.0");
    }

    public static void checkHosts(String subnet) throws IOException {
        int timeout = 1000;
        for (int i = 1; i < 255; i++) {
            String host = subnet + "." + i;
            if (InetAddress.getByName(host).isReachable(timeout)) {
                System.out.println(host + " is reachable");
            }
        }
    }
}