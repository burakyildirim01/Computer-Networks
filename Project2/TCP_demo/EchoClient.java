// package com.examples;

import java.io.*;
import java.net.*;

public class EchoClient {
        private static Socket echoSocket;
        private static PrintWriter out;
        private static BufferedReader in;
        private static BufferedReader stdIn;

        protected static String host;
        protected static int port;
  public static void main ( String... args ) throws IOException {

        System.out.println("Binding to port localhost:9999");
        host = "localhost";
        port = 9999;
      

        echoSocket = new Socket ( host, port );
                out =
                new PrintWriter ( echoSocket.getOutputStream (), true );
                in =
                new BufferedReader (
                        new InputStreamReader ( echoSocket.getInputStream () ) );
                stdIn =
                new BufferedReader (
                        new InputStreamReader ( System.in ) );
     {
        System.out.println("Type in some text please.");
        String userInput;
        userInput = stdIn.readLine();
        out.println ( userInput );
        String resultString = "";
        if (userInput.equals("1")){
                for(int i = 0; i < 16; i++){
                        String temp = in.readLine();
                        resultString += temp;
                        System.out.println ( temp );
                }
        }
        else if (userInput.equals("2")){
                for(int i = 0; i<16; i++){
                        String temp = in.readLine();
                        resultString += temp;
                        System.out.println(temp);
                        echoSocket.close();
                        out.close();
                        in.close();
                        stdIn.close();
                        echoSocket = new Socket ( host, port );
                        out =
                        new PrintWriter ( echoSocket.getOutputStream (), true );
                        in =
                        new BufferedReader (
                                new InputStreamReader ( echoSocket.getInputStream () ) );
                        stdIn =
                        new BufferedReader (
                                new InputStreamReader ( System.in ) );
                }

        }

        System.out.println(resultString);

        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
        
        
        
       
    }
  }



}
