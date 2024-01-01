import java.io.*;
import java.net.*;
import java.lang.*;

public class Client {

    // Declare the port number
    int port = 5000;

    // Declare a socket
    Socket socket;

    // for reading from server
    BufferedReader br; // from java.io

    // for writing
    PrintWriter out; // from java.io

    // Constructor
    public Client() {

        try {

            // Print a message
            System.out.println("Sending request to server");

            // Instantiate the Socket
            socket = new Socket("localhost", port);

            // Print a message
            System.out.println("Connection done.");

            // for reading from server
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // for writing to server
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            // handle exception
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }

    // todo: start reading data from server
    public void startReading() {
        // thread - read data from server
        Runnable r1 = () -> {
            System.out.println("Reader started...");
            try {
                while (true) {
                    // read data from server
                   java.lang.String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        socket.close();
                        // inter thread communication
                        notify(); // to unlock startWriting()
                        break;
                    }
                    System.out.println("Server: " + msg);
                }

            } catch (Exception e) {
                // todo: handle exception
                // e.printStackTrace();
                System.out.println("Client reading connection is closed...!");
            }
        };
        // start the thread and call run() method
        new Thread(r1).start();
    }

    // todo: start writing data to server
    public void startWriting() {
        // thread - send data to server
        Runnable r2 = () -> {
            System.out.println("Writer started...");

            try {
                while (!socket.isClosed()) {
                    // read data from user
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    java.lang.String content = br1.readLine();

                    // send data to server
                    out.println(content);

                    // flush the data to server
                    out.flush();

                    // if user types exit then close the socket
                    if (content.equals("exit")) {
                        socket.close();
                        notify(); // to unlock startReading()
                        break;
                    }
                }

                // close the socket
                // System.out.println("Connection is closed");
            } catch (Exception e) {
                // todo: handle exception
                // e.printStackTrace();
                System.out.println("Client writing connection is closed...   (T_T)");
            }

        };

        // start the thread and call run() method
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("Client is running...");

        // call the constructor
        new Client();
    }

}
