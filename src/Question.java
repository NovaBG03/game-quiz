import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

class Question {
    private String question;
    private String answer;
    private String[] otherAnswers;
    private HashMap<String, String> answersWithNumbers = new HashMap<>();

    public Question(String question, String answer, String... otherAnswers) {
        this.question = question;
        this.answer = answer;
        this.otherAnswers = otherAnswers;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswers() {
        ArrayList<String> answers = new ArrayList<>();
        for (String answer : otherAnswers) {
            answers.add(answer);
        }
        answers.add(answer);
        answers.sort(Comparator.comparingDouble(a -> Math.random()));

        for (int i = 0; i < answers.size(); i++) {
            answersWithNumbers.put(String.valueOf(i + 1), answers.get(i));
        }

        answers = new ArrayList<>();
        for (String key : answersWithNumbers.keySet()) {
            answers.add("\t" + key + ") " + answersWithNumbers.get(key));
        }

        return String.join("\n", answers);
    }

    public boolean isCorrect(int response) {

        return answersWithNumbers.get(String.valueOf(response)).equals(answer);
    }
}
