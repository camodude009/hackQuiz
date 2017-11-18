import com.google.gson.Gson;
import model.QuestionPacket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;


public class QuestionRetriever {

    private static Gson gson = new Gson();
    private ArrayList<QuestionPacket> questionPackets;
    private APIQuestionListPacket questionListPacket;
    private ArrayList<Integer> usedQuestions;

    public QuestionRetriever() {
        this(20);
    }

    public QuestionRetriever(int amount, boolean hard) {
        usedQuestions = new ArrayList<Integer>();
        retrieveNewQuestions(amount, hard);
    }

    public void retrieveNewQuestions(int amount, boolean hard) {
        usedQuestions.clear();
        questionListPacket = makeAPICall(amount, hard);
    }

    private APIQuestionListPacket makeAPICall(int amount, boolean hard) {
        try {
            URL questions = new URL("https://opentdb.com/api.php?amount=" + amount + "&difficulty=" + (hard ? "hard" : "medium") + "&category=18&type=multiple");
            URLConnection qc = questions.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(qc.getInputStream()));
            String inputLine;
            String json = "";
            while ((inputLine = in.readLine()) != null)
                json += inputLine;
            in.close();

            //json = StringEscapeUtils.unesvapeHtml4(json);

            return (APIQuestionListPacket) gson.fromJson(json, APIQuestionListPacket.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<QuestionPacket> getQuestionPackets(int amount, long ms) {
        ArrayList<QuestionPacket> questionList = new ArrayList<QuestionPacket>();

        for (int i = 0; i < amount; i++) {
            int rndIndex;
            if (usedQuestions.size() == questionListPacket.results.length)
                usedQuestions.clear();
            do {
                rndIndex = (int) (Math.random() * questionListPacket.results.length);
            } while (usedQuestions.contains(rndIndex));
            usedQuestions.add(rndIndex);
            APIQuestionListPacket.APIQuestionPacket rndPacket = questionListPacket.results[rndIndex];

            int corIndex = (int) (Math.random() * 4);
            ArrayList<String> answers = new ArrayList<String>(Arrays.asList(rndPacket.incorrect_answers));
            answers.add(corIndex, rndPacket.correct_answer);


            QuestionPacket question = new QuestionPacket(0, i, amount, rndPacket.question,
                    answers.get(0), answers.get(1), answers.get(2), answers.get(3), corIndex, ms);

            questionList.add(question);

        }
        return questionList;
    }

    public class APIQuestionListPacket {

        private int response_code;
        private APIQuestionPacket[] results;

        public APIQuestionListPacket(int resp, APIQuestionPacket[] resu) {
            response_code = resp;
            results = resu;
        }

        public class APIQuestionPacket {

            private String category, type, difficulty, question, correct_answer;
            private String[] incorrect_answers;

            public APIQuestionPacket(String cat, String typ, String dif, String que, String cor, String[] inc) {
                category = cat;
                type = typ;
                difficulty = dif;
                question = que;
                correct_answer = cor;
                incorrect_answers = inc;
            }
        }
    }
}
