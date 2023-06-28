package custom.thread.https;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class testBE implements Runnable{


    private final Socket client;
    private static String payload;

    public testBE(Socket client) {
        this.client = client;
    }
    public static void main(String[] args) {
        try {
            System.out.println("Back end started n12");
            // Get the port to listen on
            //curl https://localhost:7001 -k
            // Create a ServerSocket to listen on that port.
            payload=readFile();
            System.setProperty("javax.net.ssl.keyStore", "/Users/selakapiumal/My_tickets/CAREMORESUB/CAREMORESUB-186/wso2am-3.2.0/repository/resources/security/wso2carbon.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "wso2carbon");
            ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
            ServerSocket ss = ssf.createServerSocket(7001);
            ExecutorService executor = Executors.newCachedThreadPool();
            // Now enter an infinite loop, waiting for & handling connections.
            for (;;) {
                // Wait for a client to connect. The method will block;
                // when it returns the socket will be connected to the client
                Socket client = ss.accept();
                client.setKeepAlive(true);
                System.out.println("https test");

                //Thread thread = new Thread(new MyBackEnd(client));
                //thread.start();
                Runnable worker = new custom.thread.https.testBE(client); //execute a new thread
                executor.execute(worker);

            } // Now loop again, waiting for the next connection
        }
        // If anything goes wrong, print an error message
        catch (Exception e) {
            System.err.println(e);
            System.err.println("Usage: java HttpMirror <port>");
        }
    }

    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName()+" (Start) run()");



        // Get input and output streams to talk to the client
        try {
            //BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream());

            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            char[] buf = new char[200];

            System.out.println(payload.length());
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                if (line.isEmpty()) { //read header set
                    break;
                }
            }

            int read = in.read(buf);
            out.print("HTTP/1.1 200 OK\r\n");
            out.print("Content-Type: text/plain\r\n");
            //out.print("Content-Length: 2097155\r\n");
            out.print("Content-Length: 1594797\r\n");

            out.print("Date: Sun, 11 Sep 2022 17:39:46 GMT\r\n");

            out.print("\r\n"); // End of headers
            out.print("\r\n");
            out.flush();
            out.print(buf);
            out.flush();

            System.out.println("https test after headers");




            StringBuilder outt = new StringBuilder();
            System.out.println("https test before while");
            while (true) {
                try{
                    read = in.read(buf);
                    outt.append(buf, 0, read);
                    out.print(buf);
                    out.flush();

                    if (read < 10){
                        break;
                    }
                    //if (read < 100)

                }

                catch(Exception e){
                    e.printStackTrace();
                }
            }

            out.flush();
            System.out.println("https test end");

        } catch (IOException e) {
            e.printStackTrace();
        }

        //out.close(); // Flush and close the output stream
        /*
        try {
            in.close(); // Close the input stream
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try {
            client.close(); // Close the socket itself
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static String readFile() throws IOException {
        //BufferedReader br = new BufferedReader(new FileReader("src/main/resources/2097152B.json"));
        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/sample-1.6MB.txt"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            return everything;
        } finally {
            br.close();
        }
    }
}
