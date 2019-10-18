import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UnitTests{
  public static void main(String[] args){
      UnitTests trail = new UnitTests();
      trail.startTest();
  }

  public void startTest()
  {
    int failCount = 0;
    System.out.println("Started");
    clientTest cl = new clientTest();
    failCount += cl.testStringUtil_Bad();
    System.out.println("10 tests ran, " + String.valueOf(failCount) + "\u001B[31m failed \u001B[0m");
  }

  public class clientTest extends Multithread{
      public int requiredTest(){
        String ANSI_RESET = "\u001B[0m";
        String ANSI_BLACK = "\u001B[30m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_PURPLE = "\u001B[35m";
        String ANSI_CYAN = "\u001B[36m";
        String ANSI_WHITE = "\u001B[37m";
        PrintStream originalStream = System.out;
        PrintStream dummyStream = new PrintStream(new OutputStream(){
          public void write(int b) {}
        });
        int failCount = 0;
        System.out.println("Test6: create file and check result on of grep on all 10 vms");

        ExecutorService clients = Executors.newFixedThreadPool(10);
        for (int i=0; i<10; i++) {
            clients.execute(new fileMaker(i + 1));
        }
        clients.shutdown();

        String grepWord = "vm"; // "vm1" "sid"
        System.out.println("grep: " + grepWord);
        for(int a = 1; a <= 10; a++){
          client test5_1 = new client(a - 1, grepWord, true);;
          try
          {
            System.setOut(dummyStream);
            test5_1.run();
            System.setOut(originalStream);
            Process process=null;
            String tCommand = "grep -nE " + "'" + grepWord + "' testlog" + String.valueOf(a) + ".log" + " > localtest.txt && sed -i -e 's/^/vm" + String.valueOf(a) + ".log:/' localtest.txt && diff localtest.txt Unittest.txt";
            String[] finalCommand = new String[] {"/bin/sh", "-c", tCommand};
            final ProcessBuilder processBuilder = new ProcessBuilder(finalCommand);
            processBuilder.redirectErrorStream(true);
            process = processBuilder.start();
            InputStreamReader isr = new InputStreamReader( process.getInputStream() );
            BufferedReader br = new BufferedReader(isr);
            if(br.readLine() != null && br.readLine() != "vm" + String.valueOf(a) + ".log:") {System.out.println(ANSI_RED + "Fail - Results do not match for: " + grepWord + " (vm" + String.valueOf(a) + ANSI_RESET); failCount += 1;}
            else {System.out.println(ANSI_GREEN + "Pass - grep match for: " + grepWord + " (vm" + String.valueOf(a) + ANSI_RESET);}
          }
          catch(Exception e)
          {
            System.setOut(originalStream);
            System.out.println(ANSI_RED + "Fail - Cannot connect to VM"  + String.valueOf(a) + ANSI_RESET);
            // e.printStackTrace();
            failCount += 1;
          }
        }
        return failCount;
      }

      public class fileMaker implements Runnable{
        int macNo;
        public fileMaker(int i) {this.macNo = i;}

        public void run(){
          String ANSI_RESET = "\u001B[0m";
          String ANSI_BLACK = "\u001B[30m";
          String ANSI_RED = "\u001B[31m";
          String ANSI_GREEN = "\u001B[32m";
          String ANSI_YELLOW = "\u001B[33m";
          String ANSI_BLUE = "\u001B[34m";
          String ANSI_PURPLE = "\u001B[35m";
          String ANSI_CYAN = "\u001B[36m";
          String ANSI_WHITE = "\u001B[37m";
          String[] servers = new String[11];
          servers[1] = "172.22.152.2";
          servers[2] = "172.22.154.2";
          servers[3] = "172.22.156.2";
          servers[4] = "172.22.152.3";
          servers[5] = "172.22.154.3";
          servers[6] = "172.22.156.3";
          servers[7] = "172.22.152.4";
          servers[8] = "172.22.154.4";
          servers[9] = "172.22.156.4";
          servers[10] = "172.22.152.5";
          int port = 24686;
          // String[] sentences = new String[9];
          // sentences[0] = "at your service from ece428 group 1.";
          // sentences[1] = "The two students Ribhav and Jiyu are both super fan of Bayern.";
          // sentences[2] = "Bayern is absolutely the best soccer club in the world.";
          // sentences[3] = "We've win Bundesliga champion seven years in a row.";
          // sentences[4] = "Allianz Arena is world's most beautiful stadium.";
          // sentences[6] = "All Bayern fans miss Robben and Ribery a lot for their undeniable devotion to our fifth UEFA champion.";
          // sentences[7] = "But even without them, we as fans will support Bayern till the end.";
          // sentences[8] = "Mia san Mia!";

          try{
            Socket vm = new Socket(servers[this.macNo], port);
            OutputStream outToServer = vm.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            StringBuilder total = new StringBuilder();
            Process process=null;
            String tCommand = "python createlog.py testlog" + String.valueOf(macNo) + ".log " + String.valueOf(macNo);
            String[] finalCommand = new String[] {"/bin/sh", "-c", tCommand};
            final ProcessBuilder processBuilder = new ProcessBuilder(finalCommand);
            processBuilder.redirectErrorStream(true);
            process = processBuilder.start();
            process.destroy();

            //read file and send
            StringBuilder contentBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader("testlog" + String.valueOf(macNo) + ".log")))
            {

               String sCurrentLine;
               while ((sCurrentLine = br.readLine()) != null)
               {
                   contentBuilder.append(sCurrentLine).append("\n");
               }
            }
            catch (IOException e)
            {
               //e.printStackTrace();
               System.out.println(ANSI_RED + "No such file" + ANSI_RESET);
            }
            System.out.println("vm contents: " + contentBuilder.toString());
          }
          catch(Exception e){
            System.out.println(ANSI_RED + "Oops, Unit test crashed:( for " + String.valueOf(this.macNo) +  ANSI_RESET);
          }
        }
      }

      public int testStringUtil_Bad() {
          int passCount = 0;
          int failCount = 0;
          int i = 6;
          String testTerm = "a very specific String only used in Unittest urrrrrrrr";
          String ANSI_RESET = "\u001B[0m";
          String ANSI_BLACK = "\u001B[30m";
          String ANSI_RED = "\u001B[31m";
          String ANSI_GREEN = "\u001B[32m";
          String ANSI_YELLOW = "\u001B[33m";
          String ANSI_BLUE = "\u001B[34m";
          String ANSI_PURPLE = "\u001B[35m";
          String ANSI_CYAN = "\u001B[36m";
          String ANSI_WHITE = "\u001B[37m";
          // ExecutorService clients = Executors.newFixedThreadPool(1);
          // clients.execute(new client(i, term, true));
          client test1 = new client(i, testTerm, true);
          // test1.run();
          // Thread t = new Thread(test1);
          System.out.println("Test1: check VM Number on client");
          if(test1.whichVm() != i){
              System.out.println(ANSI_RED + "Fail - Wrong VM Number on client" + ANSI_RESET);
              System.out.println("Expected:" + String.valueOf(i) + " Actual:" + String.valueOf(test1.whichVm()));
              failCount += 1;
          } else{
              System.out.println(ANSI_GREEN + "Pass - Correct VM number" + ANSI_RESET);
          }
          System.out.println(ANSI_BLUE + "==================================" + ANSI_RESET);


          System.out.println("Test2: check term to grep on client");
          if(test1.whichTerm().compareTo(testTerm) != 0){
              System.out.println(ANSI_RED + "Fail - Wrong term to grep" + ANSI_RESET);
              System.out.println("Expected:" + testTerm + " Actual:" + test1.whichTerm());
              failCount += 1;
          } else{
              System.out.println(ANSI_GREEN + "Pass - Correct term" + ANSI_RESET);
          }
          System.out.println(ANSI_BLUE + "==================================" + ANSI_RESET);


          System.out.println("Test3: check socket connection to single vm");
          client test3 = new client(0, testTerm, true);
          try
          {
            test3.run();
            System.out.println(ANSI_GREEN + "Pass - Socket connected properly" + ANSI_RESET);
          }
          catch(Exception e)
          {
            System.out.println(ANSI_RED + "Fail - Cannot connect to VM" + ANSI_RESET);
            failCount += 1;
          }
          finally
          {
            System.out.println(ANSI_BLUE + "==================================" + ANSI_RESET);
          }

          System.out.println("Test4: check socket connection to multiple vms");
          client test4_1 = new client(0, testTerm, true);
          client test4_2 = new client(1, testTerm, true);
          client test4_3 = new client(2, testTerm, true);
          client test4_4 = new client(3, testTerm, true);
          try
          {
            test4_1.run();
            System.out.println(ANSI_GREEN + "Pass - VM1 connected" + ANSI_RESET);
          }
          catch(Exception e)
          {
            System.out.println(ANSI_RED + "Fail - Cannot connect to VM1" + ANSI_RESET);
            failCount += 1;
          }
          try
          {
            test4_2.run();
            System.out.println(ANSI_GREEN + "Pass - VM2 connected" + ANSI_RESET);
          }
          catch(Exception e)
          {
            System.out.println(ANSI_RED + "Fail - Cannot connect to VM2" + ANSI_RESET);
            failCount += 1;
          }
          try
          {
            test4_3.run();
            System.out.println(ANSI_GREEN + "Pass - VM3 connected" + ANSI_RESET);
          }
          catch(Exception e)
          {
            System.out.println(ANSI_RED + "Fail - Cannot connect to VM3" + ANSI_RESET);
            failCount += 1;
          }
          try
          {
            test4_4.run();
            System.out.println(ANSI_GREEN + "Pass - VM4 connected" + ANSI_RESET);
          }
          catch(Exception e)
          {
            System.out.println(ANSI_RED + "Fail - Cannot connect to VM4" + ANSI_RESET);
            failCount += 1;
          }
          System.out.println(ANSI_BLUE + "==================================" + ANSI_RESET);
          // Multithread exec = new Multithread(testTerm);
          // if(exec.whichTerm() != testTerm){
          //     System.out.println("Fail - Wrong Grep Term on client");
          //     failCount += 1;
          // } else{
          //     System.out.println("Pass - Correct Grep Term");
          // }
          System.out.println("Test5: check grep results on client end");
          String grepWord = "a very specific String only used in Unittest urrrrrrrr";
          client test5_0 = new client(0, grepWord, true);
          PrintStream originalStream = System.out;
          PrintStream dummyStream = new PrintStream(new OutputStream(){
            public void write(int b) {}
          });
          try
          {
            System.setOut(dummyStream);
            test5_0.run();
            System.setOut(originalStream);
            Process process=null;
            String tCommand = "grep -nE " + "'" + grepWord + "' vm1.log" + " > localtest.txt && sed -i -e 's/^/vm1.log:/' localtest.txt && diff localtest.txt Unittest.txt";
            String[] finalCommand = new String[] {"/bin/sh", "-c", tCommand};
            final ProcessBuilder processBuilder = new ProcessBuilder(finalCommand);
            processBuilder.redirectErrorStream(true);
            process = processBuilder.start();
            InputStreamReader isr = new InputStreamReader( process.getInputStream() );
            BufferedReader br = new BufferedReader(isr);
            if(br.readLine() != null && br.readLine() != "vm1.log:") {System.out.println(ANSI_RED + "Fail - Results do not match for: " + grepWord + ANSI_RESET); failCount += 1;}
            else {System.out.println(ANSI_GREEN + "Pass - grep match for: " + grepWord + ANSI_RESET);}
          }
          catch(Exception e)
          {
            System.setOut(originalStream);
            System.out.println(ANSI_RED + "Fail - Cannot connect to VM1" + ANSI_RESET);
            failCount += 1;
          }
          grepWord = "Chrome";
          client test5_2 = new client(0, grepWord, true);
          try
          {
            System.setOut(dummyStream);
            test5_2.run();
            System.setOut(originalStream);
            Process process=null;
            String tCommand = "grep -nE " + "'" + grepWord + "' vm1.log" + " > localtest.txt && sed -i -e 's/^/vm1.log:/' localtest.txt && diff localtest.txt Unittest.txt";
            String[] finalCommand = new String[] {"/bin/sh", "-c", tCommand};
            final ProcessBuilder processBuilder = new ProcessBuilder(finalCommand);
            processBuilder.redirectErrorStream(true);
            process = processBuilder.start();
            InputStreamReader isr = new InputStreamReader( process.getInputStream() );
            BufferedReader br = new BufferedReader(isr);
            if(br.readLine() != null && br.readLine() != "vm1.log:") {System.out.println(ANSI_RED + "Fail - Results do not match for: " + grepWord + ANSI_RESET); failCount += 1;}
            else {System.out.println(ANSI_GREEN + "Pass - grep match for: " + grepWord + ANSI_RESET);}
          }
          catch(Exception e)
          {
            System.setOut(originalStream);
            System.out.println(ANSI_RED + "Fail - Cannot connect to VM1" + ANSI_RESET);
            failCount += 1;
          }
          grepWord = "GET";
          client test5_1 = new client(0, grepWord, true);
          try
          {
            System.setOut(dummyStream);
            test5_1.run();
            System.setOut(originalStream);
            Process process=null;
            String tCommand = "grep -nE " + "'" + grepWord + "' vm1.log" + " > localtest.txt && sed -i -e 's/^/vm1.log:/' localtest.txt && diff localtest.txt Unittest.txt";
            String[] finalCommand = new String[] {"/bin/sh", "-c", tCommand};
            final ProcessBuilder processBuilder = new ProcessBuilder(finalCommand);
            processBuilder.redirectErrorStream(true);
            process = processBuilder.start();
            InputStreamReader isr = new InputStreamReader( process.getInputStream() );
            BufferedReader br = new BufferedReader(isr);
            if(br.readLine() != null && br.readLine() != "vm1.log:") {System.out.println(ANSI_RED + "Fail - Results do not match for: " + grepWord + ANSI_RESET); failCount += 1;}
            else {System.out.println(ANSI_GREEN + "Pass - grep match for: " + grepWord + ANSI_RESET);}
          }
          catch(Exception e)
          {
            System.out.println(ANSI_RED + "Fail - Cannot connect to VM1" + ANSI_RESET);
            e.printStackTrace();
            failCount += 1;
          }
          System.out.println(ANSI_BLUE + "==================================" + ANSI_RESET);
          // failCount += requiredTest();
      return failCount;
      }
  }

  public class serverTest extends server{

  }
}
