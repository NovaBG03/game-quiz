import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        startListening(8085);
    }

    public static void startListening(int port) throws IOException {
        try (var server = new ServerSocket(port)) {
            while (true) {
                System.out.println("Waiting for connection...");
                var socket = server.accept();
                System.out.println("Client connected " + socket.getInetAddress());
                var scanner = new Scanner(socket.getInputStream());
                var writer = new PrintStream(socket.getOutputStream());

                new Thread(() -> startGame(scanner, writer)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startGame(Scanner scanner, PrintStream writer) {
        writer.println("Добре дошли в играта!");
        writer.println("Въведете име на герой:");
        String heroName = scanner.next();
        Hero hero = new Hero(heroName);
        GameServer gameServer = new GameServer(hero, scanner, writer);
        gameServer.start();
    }
}
