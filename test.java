import java.io.*;
import java.lang.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class test
{
  public static void main(String[] args) throws Exception
  {
    // String[] finalCommand = new String[] {"grep", "-nrEi", "--include=\\*.log", "Chelsea", "./"};
    String arg1 = "Chelsea";
    String grepWord = "grep -nrEi --include=\\*.log '" + arg1 + "' ./";
    System.out.println(grepWord);
    String[] finalCommand = new String[] {"/bin/sh", "-c", grepWord};
    Process process=null;
    try {
        final ProcessBuilder processBuilder = new ProcessBuilder(finalCommand);
        processBuilder.redirectErrorStream(true);
        process = processBuilder.start();
        // stdout+stderr
        InputStreamReader isr = new InputStreamReader( process.getInputStream() );
        BufferedReader br = new BufferedReader(isr);
        String line;
        StringBuilder total = new StringBuilder();
        while ((line = br.readLine()) != null) {
          // System.out.println(line.substring(3, line.indexOf('l', 3)));
          //System.out.println(line);
          total.append(line.substring(3, line.indexOf(':', 11)) + "\n");
        }
        System.out.println(total.toString());
        System.out.println("Program terminated!");
        process.destroy();
        br.close();
        isr.close();
    }
    catch (Exception e) {
        e.printStackTrace();
    }

    // String temp = ".//vm1.log:53:";
    // System.out.println(temp.substring(0, temp.indexOf('l', 3)));
  }
}
