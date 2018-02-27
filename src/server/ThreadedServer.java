package ser321.http.server;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Copyright 2015 Tim Lindquist,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * A class for client-server connections with a threaded server.
 * The echo server creates a server socket. Once a client arrives, a new
 * thread is created to service all client requests for the connection.
 * The example includes a java client and a C# client. If C# weren't involved,
 * the server and client could use a bufferedreader, which allows readln to be
 * used, and printwriter, which allows println to be used. These avoid
 * playing with byte arrays and encodings. See the Java Tutorial for an
 * example using buffered reader and printwriter.
 *
 * Ser321 Foundations of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, ASU Poly
 * @version August 2015
 */
/**
 * @author Tim Lindquist ASU Polytechnic Department of Engineering
 * @version October 2009
 */
public class ThreadedServer extends Thread {
  private Socket conn;
  private int id;

  public ThreadedServer (Socket sock, int id) {
    this.conn = sock;
    this.id = id;
  }

   public void run() {
      try {
          OutputStream outSock = conn.getOutputStream();
          InputStream inSock = conn.getInputStream();
          byte clientInput[] = new byte[1024]; // up to 1024 bytes in a message.
          int numr = inSock.read(clientInput,0,1024);
          while (numr != -1) {
              //System.out.println("read "+numr+" bytes");
              String clientString = new String(clientInput,0,numr);
              System.out.println("read from client: "+id+" the string: "
                               +clientString);
	             outSock.write(clientInput,0,numr);
              numr = inSock.read(clientInput,0,1024);

              
          }
          inSock.close();
          outSock.close();
          conn.close();
      } catch (IOException e) {
          System.out.println("Can't get I/O for the connection.");
      }
   }
    
   public static void main (String args[]) {
    Socket sock;
    int id=0;
    try {
      if (args.length != 1) {
        System.out.println("Usage: java package ser321.http.server"+
                           " [portNum]");
        System.exit(0);
      }
      int portNo = Integer.parseInt(args[0]);
      if (portNo <= 1024) portNo=8888;
      ServerSocket serv = new ServerSocket(portNo);
      while (true) {
        System.out.println("Echo server waiting for connects on port "
                            +portNo);
        sock = serv.accept();
        System.out.println("Echo server connected to client: "+id);
        ThreadedServer myServerThread = new ThreadedServer(sock,id++);
        myServerThread.start();
      }
    } catch(Exception e) {e.printStackTrace();}
  }
}
