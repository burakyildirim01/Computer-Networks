import java.io.*;
import java.net.*;
public class Client {
	public static void main(String[] args) throws IOException {
		String host = "localhost";
		//Getting the port value as argument
	    int port = Integer.parseInt(args[0]);
	    int timeoutVal;
	    Socket socket = null;
	    PrintWriter out = null;
	    BufferedReader in = null;
	    BufferedReader reader = null;
	    System.out.println("Binding to port localhost:" + port);
	    try {
	    	socket = new Socket ( host, port );
		    out = new PrintWriter ( socket.getOutputStream (), true );
		    in =
	                new BufferedReader (
	                        new InputStreamReader ( socket.getInputStream () ) );
		    reader =
	                new BufferedReader (
	                        new InputStreamReader ( System.in ) );
	    	System.out.println("Connection established. The socket address: "+socket.getRemoteSocketAddress());
	    	String userInput;
	    	String inputLine;
	    	//Get timeout value from the server
	    	timeoutVal = Integer.parseInt(in.readLine());
	        socket.setSoTimeout(timeoutVal);
	    	System.out.println("Timeout settings is: "+socket.getSoTimeout());
	        System.out.println("Type in some text please. Type quit to terminate");
	        while(true) {
	        	//Gets the input of client
		        userInput = reader.readLine();
		        //Sends the input to server
		        out.println ( userInput );
		        if(userInput.equals("quit")) {
		        	break;
		        }
		        //Gets the message of server
		        inputLine = in.readLine();
				System.out.println ("Server's message: " + inputLine);
				if(inputLine.equals("quit")) {
		        	break;
		        }
				System.out.println("Type in some text please. Type quit to terminate");
	        }
	    } catch (Exception e) {
	    	//Handling the socket timeout exception
	    	System.out.println(e);
	    	socket.close();
		    out.close();
		    in.close();
		    reader.close();
	    }
	    //Close the sockets, readers and writers
	    socket.close();
	    out.close();
	    in.close();
	    reader.close();
	}
}