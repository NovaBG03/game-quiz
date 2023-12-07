import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        try (var socket = new Socket("127.0.0.1", 8085)) {
            var writeThread = new Thread(() -> {
                try {
                    var scanner = new Scanner(socket.getInputStream());
                    while (scanner.hasNext()) {
                        System.out.println(scanner.nextLine());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            var readThread = new Thread(() -> {
                try {
                    var writer = new PrintStream(socket.getOutputStream());
                    var scanner = new Scanner(System.in);
                    while (scanner.hasNext()) {
                        writer.println(scanner.nextLine());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });


            writeThread.start();
            readThread.start();

            readThread.join();
            writeThread.join();
        }
    }
}
