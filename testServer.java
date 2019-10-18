import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class testServer{
  public static void main(String[] args) throws Exception{
    int macNo = 0;
    String[] servers = new String[10];
    servers[0] = "172.22.152.2";
    servers[1] = "172.22.154.2";
    servers[2] = "172.22.156.2";
    servers[3] = "172.22.152.3";
    servers[4] = "172.22.154.3";
    servers[5] = "172.22.156.3";
    servers[6] = "172.22.152.4";
    servers[7] = "172.22.154.4";
    servers[8] = "172.22.156.4";
    servers[9] = "172.22.152.5";
    for(int i = 0; i < 10; i++)
    {
      // System.out.print(i);
      // System.out.print(":" + servers[i] + " " + IP + "\n");
      String IP = InetAddress.getLocalHost().getHostAddress();
      if(IP.compareTo(servers[i]) == 0)
      {
        macNo = i + 1;
        break;
      }
    }
    try(ServerSocket reception = new ServerSocket(24686)){
      System.out.println("waiting...");
      Socket receiver = reception.accept();
      InputStream inStream = receiver.getInputStream();
      DataInputStream in = new DataInputStream(inStream);
      String content = in.readUTF();
      // System.out.println(content);
      receiver.close();

      File file = new File("vm" + String.valueOf(macNo) +".log");
      FileWriter fr = new FileWriter(file, false);
      BufferedWriter br = new BufferedWriter(fr);
      br.write(content);
      br.close();
      fr.close();
    }
  }
}
