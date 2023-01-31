import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.io.*;

/**
 * Created by Yahya Hassanzadeh on 20/09/2017.
 */

public class ConnectionToServer
{
    public static final String DEFAULT_SERVER_ADDRESS = "localhost";
    public static final int DEFAULT_SERVER_PORT = 4444;
    private Socket s;
    //private BufferedReader br;
    protected BufferedReader is;
    protected PrintWriter os;

    protected String serverAddress;
    protected int serverPort;
    
    protected DataInputStream dis;
    protected DataOutputStream dos;
    

    /**
     *
     * @param address IP address of the server, if you are running the server on the same computer as client, put the address as "localhost"
     * @param port port number of the server
     */
    public ConnectionToServer(String address, int port)
    {
        serverAddress = address;
        serverPort    = port;
    }

    /**
     * Establishes a socket connection to the server that is identified by the serverAddress and the serverPort
     */
    public void Connect()
    {
        try
        {
            s=new Socket(serverAddress, serverPort);
            //br= new BufferedReader(new InputStreamReader(System.in));
            /*
            Read and write buffers on the socket
             */
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = new PrintWriter(s.getOutputStream());
            
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            
	        s.setSoTimeout(30000);

            System.out.println("Successfully connected to " + serverAddress + " on port " + serverPort);
        }
        catch (IOException e)
        {
            //e.printStackTrace();
            System.err.println("Error: no server has been found on " + serverAddress + "/" + serverPort);
        }
    }

    /**
     * sends the message String to the server and retrives the answer
     * @param message input message string to the server
     * @return the received server answer
     * @throws IOException 
     */
    public byte[] SendForAnswer(byte[] message) throws IOException
    {
    	dos.write(message);
        dos.flush();
    	
    	int maxSize = dis.available();
        while (maxSize < 1)
        {
        	maxSize = dis.available();
        }
        
        byte[] response = new byte[maxSize];
        dis.readFully(response);
        return response;
    	
    }


    /**
     * Disconnects the socket and closes the buffers
     */
    public void Disconnect()
    {
        try
        {
            is.close();
            os.close();
            dis.close();
            dos.close();
            //br.close();
            s.close();
            System.out.println("ConnectionToServer. SendForAnswer. Connection Closed");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public byte[] toByteArray(String data, int messageType, int N, int dataReceived) {
		byte[] stringBytes = data.getBytes();
    	int sizeOfProtocol = N+3;
    	byte[] result = new byte[sizeOfProtocol];
    	result[0] = (byte) messageType;
    	result[1] = (byte) N;
    	result[2] = (byte) dataReceived;
    	for(int i = 0; i<N; i++) {
    		result[i+3] = stringBytes[i];
    	}
    	return result;
    }
    
    public String parseFrom(byte[] byteArray) {
    	String s = new String(Arrays.copyOfRange(byteArray, 3, byteArray.length));
    	return s;
    }
}
