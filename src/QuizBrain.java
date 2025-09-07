import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import org.jsoup.Jsoup;

public class QuizBrain {
    private final Data data;
    private int questionNumber;
    private int score;
    private String currentQuestion;
    private String correctAnswer;
    private String HtmlEntitiesContainingQuestion;
    private final JsonArray results;

    public QuizBrain() {
        this.data = new Data();
        this.questionNumber = 0;
        this.score = 0;

        try {
            String jsonResponse = this.data.dataset();
            Gson gson = new Gson();
            JsonObject jsonObj = gson.fromJson(jsonResponse, JsonObject.class);
            this.results = jsonObj.getAsJsonArray("results");

            if (this.results == null || this.results.size() == 0) {
                throw new IllegalStateException("No quiz questions available");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize quiz: " + e.getMessage());
        }
    }

    public boolean stillHasQuestion() {
        return this.questionNumber < this.results.size();
    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    public String nextQuestion() {
        this.HtmlEntitiesContainingQuestion = this.results.get(this.questionNumber).getAsJsonObject().get("question").getAsString();
        this.currentQuestion = html2text(this.HtmlEntitiesContainingQuestion);
        this.correctAnswer = this.results.get(this.questionNumber).getAsJsonObject().get("correct_answer").getAsString();
        this.questionNumber += 1;
        return "Q. " + this.questionNumber + ": " + this.currentQuestion;
    }

    public boolean check_answer(String userAnswer) {
        if (userAnswer.equalsIgnoreCase(correctAnswer)) {
            this.score++;
            return true;
        } else {
            return false;
        }
    }


    public int getScore() {
        return this.score;
    }

    public int getTotalQuestions() {
        return this.results.size();
    }

}