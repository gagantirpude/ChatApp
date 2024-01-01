import java.net.*;
import java.io.*;

class Server {

    // Declare the port number
    int port = 5000;

    // Declare the ServerSocket
    ServerSocket server;

    // Declare a socket
    java.net.Socket socket;

    // for reading from client
    BufferedReader br; // from java.io

    // for writing to client
    PrintWriter out; // from java.io

    // Constructor
    public Server() {

        try {
            // Instantiate the ServerSocket
            server = new ServerSocket(port);

            // Print a message
            System.out.println("Server is Ready to accept connection");

            // Print a message
            System.out.println("Waiting for the client to connect...");

            // Instantiate the Socket
            socket = server.accept();

            // Print a message
            System.out.println("Client connected");

            // for reading from client
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // for writing to client
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            // handle exception
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }

    public void startReading() {
        // thread - read data from client
        Runnable r1 = () -> {
            System.out.println("reader started...");
            try {
                while (true) {
                    java.lang.String msg = br.readLine();
                    // if client sends exit then close the socket
                    if (msg.equals("exit")) {
                        System.out.println("Client terminated the chat");
                        // close the socket
                        socket.close();
                        notify(); // to unlock startWriting()
                        break;
                    }
                    System.out.println("Client : " + msg);
                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Server reading connection is closed...!");
            }
        };
        // Create a Object of Thread Class and start the thread
        new Thread(r1).start();
    }

    public void startWriting() {
        // thread - data send to the client
        Runnable r2 = () -> {
            System.out.println("writer started...");
            try {
                while (!socket.isClosed()) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    java.lang.String content = br1.readLine();
                    // to send the data to the client
                    out.println(content);
                    // flush the data
                    out.flush();
                    // if user types exit then close the socket
                    if (content.equals("exit")) {
                        // close the socket
                        socket.close();
                        notify(); // to unlock startReading()
                        break;
                    }
                }
                // System.out.println("Connection is closed");
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Server writing connection is closed... (T_T)");
            }
        };
        // Create a Object of Thread Class and start the thread
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("Server is running...");

        // call the constructor
        new Server();
    }
}