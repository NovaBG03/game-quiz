import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameServer {
    Hero hero;
    private File saveFile = new File("save.txt");
    private final Scanner scanner;
    private final PrintStream writer;

    public GameServer(Hero hero1, Scanner scanner, PrintStream writer) {
        this.scanner = scanner;
        this.writer = writer;
        loadHero(hero1);
        this.start();
    }

    public void start() {
        System.out.print("\033[H\033[2J");
        writer.println("Изберете игра:");
        writer.println("1. Битка");
        writer.println("2. Викторина");
        writer.println("\n\n0. Изход");
        int gameChoice = scanner.nextInt();
        IGame game;
        if (gameChoice == 1) {
            game = new BattleGame(hero, this);
        } else if (gameChoice == 2) {
            game = new TriviaGame(hero, this, scanner, writer);
        } else if (gameChoice == 0) {
            return;
        } else {
            writer.println("Невалиден избор!");
            start();
            return;
        }
        play(game);
        save();
    }

    public void play(IGame game) {
        game.start();
        if (game instanceof BattleGame) {
            ((BattleGame) game).fight(hero);
        }
        hero = game.getHero();
    }

    private void checkForSaveFile() {
        if (!saveFile.exists()) {
            try {
                saveFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadHero(Hero hero1) {
        try {
            checkForSaveFile();
            Scanner fileScanner = new Scanner(saveFile);
            String heroLine;
            while (fileScanner.hasNextLine()) {
                heroLine = fileScanner.nextLine();
                String heroName = heroLine.split(",")[0];
                if (heroName.equals(hero1.name)) {
                    hero1.deserialize(heroLine);
                    writer.println("Зареден герой " + hero1.name);
                    break;
                }
            }
            hero = hero1;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void save() {
        try {
            Scanner fileScanner = new Scanner(saveFile);
            String heroLine;
            int heroExists = -1;
            int lineIndex = 0;
            while (fileScanner.hasNextLine()) {
                heroLine = fileScanner.nextLine();
                String heroName = heroLine.split(",")[0];
                if (heroName.equals(hero.name)) {
                    heroExists = lineIndex;
                }
                lineIndex++;
            }
            fileScanner.close();

            if (heroExists != -1) {
                List<String> fileContent = new ArrayList<>(java.nio.file.Files.readAllLines(saveFile.toPath()));
                fileContent.set(heroExists, hero.serialize());
                java.nio.file.Files.write(saveFile.toPath(), fileContent);
            } else {
                FileWriter writer = new FileWriter(saveFile, true);
                writer.write(hero.serialize() + "\n");
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Hero getHero() {
        return hero;
    }
}
