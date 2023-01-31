/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author AbdulrazakZakieh
 */
public class MultithreadClient {

    /**
     * @param args the command line arguments
     * @throws NumberFormatException 
     */
    public static void main(String[] args) throws NumberFormatException, IOException {
        // TODO code application logic here
    	String response = "";
    	int N = 0;
    	int dataReceived = 0;
    	int messageType = 0;
        ConnectionToServer connectionToServer = new ConnectionToServer(ConnectionToServer.DEFAULT_SERVER_ADDRESS, ConnectionToServer.DEFAULT_SERVER_PORT);
        connectionToServer.Connect();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter 1 to get the list of coins or 2 with arguments to get prices of coins.\n"
        		+ "Sample Usage: '1' or '2 Bitcoin,Ethereum'. To quit just type '3'");
        String message = scanner.nextLine();
        messageType = Integer.parseInt(message.split(" ")[0]);
        while (messageType!=3)
        {
        	
        	if(messageType==1) {
        		response = message;
        		N = 0;
        		dataReceived = 1;
        		
        	}else if (messageType==2) {
        		response = message.split(" ")[1];
            	N = message.split(" ")[1].length();
            	dataReceived = 1;
        	}
        	
        			
        	byte[] packet = connectionToServer.toByteArray(response, messageType, N, dataReceived);
        	

        	
        	
        	System.out.println("Response from server: " + connectionToServer.parseFrom(connectionToServer.SendForAnswer(
        			packet)));
        	System.out.println("Enter 1 to get the list of coins or 2 with arguments to get prices of coins.\n"
            		+ "Sample Usage: '1' or '2 Bitcoin,Ethereum'. To quit just type '3''");
        	message = scanner.nextLine();
        	messageType = Integer.parseInt(message.split(" ")[0]);
        	
            
        }

        byte[] packet = connectionToServer.toByteArray("", messageType, 0, 1);
        connectionToServer.SendForAnswer(packet);
        connectionToServer.Disconnect();
        scanner.close();
        
        
    }
    
}
