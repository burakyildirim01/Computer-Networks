import java.net.*;
import java.io.*;
public class Server {
	public static void main(String[] args) throws IOException {
		//Get the port and timeout values as argument
		int port = Integer.parseInt(args[0]);
		int timeoutVal = Integer.parseInt(args[1]);
        System.out.println("listening to port " + port);
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        BufferedReader reader = null;
    try {
    	serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out =
                new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
		reader =
				new BufferedReader(
						new InputStreamReader(System.in));
    	clientSocket.setSoTimeout(timeoutVal);
    	//Send timeout value to client
    	out.println(timeoutVal);
    	System.out.println("Connection established. The socket address: "+clientSocket.getRemoteSocketAddress());
    	System.out.println("Timeout setting is: "+clientSocket.getSoTimeout());
        String inputLine;
        String serverInput;
        while(true) {
        	//Gets the message of client
        	inputLine = in.readLine();
        	System.out.println ( "Client's message: " + inputLine );    
        	if(inputLine.equals("quit")) {
            	break;
            }
            System.out.println("Type in some text please. Type quit to terminate");
            //Gets the input from server
			serverInput = reader.readLine();
			//Sends the input to client
			out.println(serverInput);
			if(serverInput.equals("quit")) {
				break;
			}
    	}
        }catch (Exception e){
        	//Handling the socket timeout exception
        	System.out.println(e);
        	serverSocket.close();
        	clientSocket.close();
        	out.close();
        	reader.close();
        	in.close();
        }
    	//Close the sockets, readers and writers
    	serverSocket.close();
    	clientSocket.close();
    	out.close();
    	reader.close();
    	in.close();
	
    }
}