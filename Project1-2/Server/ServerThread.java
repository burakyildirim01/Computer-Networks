import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

class ServerThread extends Thread
{
    protected BufferedReader is;
    protected PrintWriter os;
    protected DataInputStream dis;
    protected DataOutputStream dos;
    protected Socket s;
    private String line = new String();
    private String lines = new String();

    /**
     * Creates a server thread on the input socket
     *
     * @param s input socket to create a thread on
     */
    public ServerThread(Socket s)
    {
        this.s = s;
    }

    /**
     * The server thread, echos the client until it receives the QUIT string from the client
     */
    public void run()
    {
        try
        {
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = new PrintWriter(s.getOutputStream());
            
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            s.setSoTimeout(30000);

        }
        catch (IOException e)
        {
            System.err.println("Server Thread. Run. IO error in server thread");
        }

        try
        {
        	String response = "";
        	byte[] packet = null;
        	int maxSize = dis.available();
            while (maxSize < 1)
            {
            	maxSize = dis.available();
            }
        	byte[] data = new byte[maxSize];
            dis.readFully(data);
            while(data[0]!= 3) {
            	if(data[0]==2) {
            		response = getPrice(parseFrom(data));
            		packet = toByteArray(response, 2, response.length(), 1);
            	} else if(data[0]==1) {
            		response = getList();
            		packet = toByteArray(response, 1, response.length(), 1);
            	}
            	lines = "Client messaged : " + line + " at  : " + Thread.currentThread().getId() + " " + parseFrom(data); 
                dos.write(packet);
                dos.flush();
                System.out.println("Client " + s.getRemoteSocketAddress() + " sent :  " +lines);
                maxSize = dis.available();
                while (maxSize < 1)
                {
                	maxSize = dis.available();
                }
                data = new byte[maxSize];
                dis.readFully(data);
            	
            }
            
            lines = "Client messaged : " + line + " at  : " + Thread.currentThread().getId() + " " + parseFrom(data); 
            packet = toByteArray("", 3, 0, 1);
            dos.write(packet);
            dos.flush();
            System.out.println("Client " + s.getRemoteSocketAddress() + " sent :  " +lines);
            
            
        }
        catch (IOException e)
        {
            line = this.getName(); //reused String line for getting thread name
            System.err.println("Server Thread. Run. IO Error/ Client " + line + " terminated abruptly");
        }
        catch (NullPointerException e)
        {
            line = this.getName(); //reused String line for getting thread name
            System.err.println("Server Thread. Run.Client " + line + " Closed");
        } finally
        {
            try
            {
                System.out.println("Closing the connection");
                if (is != null)
                {
                    is.close();
                    System.err.println(" Socket Input Stream Closed");
                }

                if (os != null)
                {
                    os.close();
                    System.err.println("Socket Out Closed");
                }
                if (dis != null)
                {
                    dis.close();
                    System.err.println("Data Input Socket Out Closed");
                }
                if (dos != null)
                {
                    dos.close();
                    System.err.println("Data Output Socket Out Closed");
                }
                if (s != null)
                {
                    s.close();
                    System.err.println("Socket Closed");
                }

            }
            catch (IOException ie)
            {
                System.err.println("Socket Close Error");
            }
        }//end finally
    }
    
    private static String getPrice(String inputCoinNames) throws MalformedURLException, IOException {
    	List<String> requestedCoins = Arrays.asList(inputCoinNames.trim().split(","));
    	ArrayList<String> coinIDs = new ArrayList<String>();
    	ArrayList<String> coinNames = new ArrayList<String>();
    	HashMap<String, Double> resultNamePrice = new HashMap<String, Double>();
    	
        URL url = new URL("https://api.coingecko.com/api/v3/coins/list");
        String baseCurrencyUrl = "https://api.coingecko.com/api/v3/simple/price?vs_currencies=try&ids=";

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        String inline = "";
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            inline += scanner.nextLine();
        }
        //Close the scanner
        scanner.close();
        conn.disconnect();
        
        
        JSONArray jsonObject = new JSONArray(inline);
        for (int i = 0; i < jsonObject.length(); i++)
        {
            String id = jsonObject.getJSONObject(i).getString("id");
            coinIDs.add(id);
            String name = jsonObject.getJSONObject(i).getString("name");
            coinNames.add(name);
        }
        
        
        for(int i = 0; i<requestedCoins.size(); i++) {
        	int coinIndex = coinNames.indexOf(requestedCoins.get(i));
            String coinID = coinIDs.get(coinIndex);
            String coinName = coinNames.get(coinIndex);
            
        	String finalUrl = baseCurrencyUrl.concat(coinID);
        	URL currencyUrl = new URL(finalUrl);
        	conn = (HttpURLConnection) currencyUrl.openConnection();
        	conn.setRequestMethod("GET");
            conn.connect();
            inline = "";
            scanner = new Scanner(currencyUrl.openStream());
            while (scanner.hasNext()) {
                inline += scanner.nextLine();
            }
            
            JSONObject jsonObjectPrice = new JSONObject(inline);

            double price = jsonObjectPrice.getJSONObject(coinID).getFloat("try");
            resultNamePrice.put(coinName, price);
        }
        
        String resultString = "";
        
        Set<String> resultNames = resultNamePrice.keySet();
        
        for (String s : resultNames) {
        	resultString += "The price of " + s + " = " +resultNamePrice.get(s) + ", ";
        }
        

        scanner.close();
        conn.disconnect();
        return resultString;
    }
    
    private static String getList() throws MalformedURLException, IOException {
    	ArrayList<String> coinIDs = new ArrayList<String>();
    	ArrayList<String> coinNames = new ArrayList<String>();
    	ArrayList<String> coinSymbols = new ArrayList<String>();
    	
        URL url = new URL("https://api.coingecko.com/api/v3/coins/list");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        String inline = "";
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            inline += scanner.nextLine();
        }
        //Close the scanner
        scanner.close();
        conn.disconnect();
        
        
        JSONArray jsonObject = new JSONArray(inline);
        for (int i = 0; i < jsonObject.length(); i++)
        {
            String id = jsonObject.getJSONObject(i).getString("id");
            coinIDs.add(id);
            String name = jsonObject.getJSONObject(i).getString("name");
            coinNames.add(name);
            String symbol = jsonObject.getJSONObject(i).getString("symbol");
            coinSymbols.add(symbol);
        }
        
        
        String resultString = "";
        
        
        for (int i = 0; i<coinIDs.size(); i++) {
        	resultString += " ID: " + coinIDs.get(i) + " Name: " + coinNames.get(i) + " Symbol: " + coinSymbols.get(i) + "\t,";
        }
        

        scanner.close();
        conn.disconnect();
        return resultString;
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
