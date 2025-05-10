package com.jingineering.multithreading.abhayani.tcpserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SampleTCPServer {
    private static ExecutorService es = Executors.newFixedThreadPool(20);

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1729);
            System.out.println("Server Started");
            while (true) {
                System.out.println("Waiting for the client to connect,,,");
                Socket socket = serverSocket.accept();
                System.out.println("Client connected...");

                es.execute(() -> {
                    try {
                        doSomeWork(socket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            es.shutdown();
        }
    }

    private static void doSomeWork(Socket socket) throws IOException {
        // not reading for now..
        /*BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String input = br.readLine();
        System.out.println("came from client: " + input);
        br.close();*/

        // Simulate some fake delay as if server is processing the inputs
        System.out.println("Processing the request");
        try {
            Thread.sleep(8 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PrintWriter bw = new PrintWriter(socket.getOutputStream());
        bw.write("HTTP/1.1 200 OK\r\n\r\nthis message is coming from server...hello world\r\n");
        bw.flush();
        socket.close();
    }
}
