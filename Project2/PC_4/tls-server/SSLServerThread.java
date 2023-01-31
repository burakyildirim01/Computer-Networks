import javax.net.ssl.SSLSocket;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Random;

/**
 * Copyright [Yahya Hassanzadeh-Nazarabadi]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */


public class SSLServerThread extends Thread
{

    private final String SERVER_ACK_MESSAGE = "server_ack";
    private SSLSocket sslSocket;
    private String line = new String();
    private BufferedReader is;
    private BufferedWriter os;
    private SSLSocket sCopy;
    public SSLServerThread(SSLSocket s)
    {
        sslSocket = s;
        sCopy = s;
    }

    public void run()
    {
        try
        {
            is = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            os = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));

        }
        catch (IOException e)
        {
            System.out.println("Server Thread. Run. IO error in server thread");
        }

        try
        {
            line = is.readLine();
            System.out.println("Client " + sslSocket.getRemoteSocketAddress() + " sent : " + line);
            if (line.equals("1")){
                String randomString = getRandomString();
            for(int i = 0; i<16; i++){
                    char c = randomString.charAt(i);
    				os.write(c);
    				os.flush();
    			
            }
            } else if (line.equals("2")){
                String randomString = getRandomString();
                for(int i = 0; i<16; i++){
                        char c = randomString.charAt(i);
                        os.write(c);
                        os.flush();
                        is.close();
                        os.close();
                        sslSocket.close();
                        sslSocket = sCopy;
                        is = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
                        os = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));
                }
        }
            

        }
        catch (IOException e)
        {
            line = this.getClass().toString(); //reused String line for getting thread name
            System.out.println("Server Thread. Run. IO Error/ Client " + line + " terminated abruptly");
        }
        catch (NullPointerException e)
        {
            line = this.getClass().toString(); //reused String line for getting thread name
            System.out.println("Server Thread. Run.Client " + line + " Closed");
        } finally
        {
            try
            {
                System.out.println("Closing the connection");
                if (is != null)
                {
                    is.close();
                    System.out.println(" Socket Input Stream Closed");
                }

                if (os != null)
                {
                    os.close();
                    System.out.println("Socket Out Closed");
                }
                if (sslSocket != null)
                {
                    sslSocket.close();
                    System.out.println("Socket Closed");
                }

            }
            catch (IOException ie)
            {
                System.out.println("Socket Close Error");
            }
        }//end finally
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
