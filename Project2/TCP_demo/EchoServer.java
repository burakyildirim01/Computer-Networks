// package com.examples;
import java.net.*;
import java.util.Random;
import java.io.*;

public class EchoServer {
        private static ServerSocket serverSocket;
        private static Socket clientSocket;
        private static PrintWriter out;
        private static BufferedReader in;
        protected static int port;
  public static void main(String... args) throws IOException {



        System.out.println("listening to port 9999");
        port = 9999;

             serverSocket =
                    new ServerSocket(port);
             clientSocket = serverSocket.accept();
             out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
             in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
     {
        String inputLine;
        inputLine = in.readLine();
        if (inputLine.equals("1")){
                String randomString = getRandomString();
                for(int i = 0; i < 16; i++){
                        out.println(randomString.charAt(i));
                }
        }
        else if(inputLine.equals("2")){
                String randomString = getRandomString();
                for(int i = 0; i < 16; i++){
                out.println(randomString.charAt(i));
                out.close();
                in.close();
                clientSocket.close();
                serverSocket.close();
                serverSocket =
                    new ServerSocket(port);
             clientSocket = serverSocket.accept();
             out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
             in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
                }
                
        }


        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();

    }
  }
  private static String getRandomString() {
    String charpool = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    while (sb.length() < 16) {
        int index = (int) (random.nextFloat() * charpool.length());
        sb.append(charpool.charAt(index));
    }
    return sb.toString();
        }
}
