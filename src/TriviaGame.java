import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class TriviaGame implements IGame {
    private List<Question> questions;
    private Hero hero;
    private final Scanner scanner;
    private final PrintStream writer;
    private final GameServer gameServer;
    private static final String TRIVIA_URL = "https://opentdb.com/api.php?amount=10&type=multiple&category=18&difficulty=easy";

    public TriviaGame(Hero hero, GameServer gameServer, Scanner scanner, PrintStream writer) {
        this.hero = hero;
        this.gameServer = gameServer;
        this.scanner = scanner;
        this.writer = writer;
        loadQuestions();
    }

    private void loadQuestions() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TRIVIA_URL))
                .build();
        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            parseTriviaResponse(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseTriviaResponse(String responseString) throws JSONException {
        JSONObject response = new JSONObject(responseString);
        JSONArray questionsArray = response.getJSONArray("results");
        questions = new ArrayList<>();

        for (int i = 0; i < questionsArray.length(); i++) {
            JSONObject questionObject = questionsArray.getJSONObject(i);
            String questionText = questionObject.getString("question");
            String correctAnswer = questionObject.getString("correct_answer");
            JSONArray incorrectAnswers = questionObject.getJSONArray("incorrect_answers");

            questions.add(new Question(questionText, correctAnswer, incorrectAnswers.getString(0), incorrectAnswers.getString(1), incorrectAnswers.getString(2)));
        }
    }

    public void start() {
        int questionNumber = 1;
        for (Question question : questions) {
            writer.println("Въпрос " + questionNumber++ + ":");
            writer.println("За да отговорите, въведете номера на отговора");
            writer.println(question.getQuestion());
            writer.println(question.getAnswers());
            writer.println();
            writer.println("\t9) За да подобрите героя си");
            writer.println("\t0) За да спрете играта");
            int answer = scanner.nextInt();
            if (answer == 0) {
                writer.println("Играта приключи.");
                gameServer.save();
                break;
            }
            if (answer == 9) {
                writer.println("Използвайте точките за подобрения.");
                writer.println("Точки: " + hero.points);
                for (int i = 0; i < hero.points; i++) {
                    writer.println("Изберете атрибут за подобрение: strength, dexterity, vitality, energy");
                    String attribute = scanner.next();
                    while (!attribute.equals("strength") && !attribute.equals("dexterity") && !attribute.equals("vitality") && !attribute.equals("energy") && !attribute.equals("exit")) {
                        writer.println("Невалиден атрибут. Опитайте отново.");
                        attribute = scanner.next();
                    }
                    if (attribute.equals("exit")) {
                        break;
                    }
                    hero.improveAttribute(attribute);
                    hero.printStats();
                }
                continue;
            }
            if (question.isCorrect(answer)) {
                hero.points++;
                writer.println("Правилно! Точки: " + hero.points);
            } else {
                writer.println("Грешка!");
            }
        }

        hero.improveAttribute("strength");
    }

    @Override
    public Hero getHero() {
        return hero;
    }
}
