package Evaluation;


public class Points {

    private final int questionsPerRound;
    private Round[] rounds;




    // --------------------------------------------- constructor -------------------------------------------------------
    public Points(Round[] rounds){
        this.rounds = rounds;
        this.questionsPerRound = rounds.length;
    }

    public Points(int number_Rounds, int questionsPerRound){
        this.rounds = new Round[number_Rounds];
        this.questionsPerRound = questionsPerRound;
        for(int i = 0; i<number_Rounds; i++) rounds[i] = new Round(questionsPerRound);
    }






    // ----------------------------------------------- setter & getter -------------------------------------------------

    public void setAnswer(int round, int questionNumber, boolean right){
        if(round<0 || round > this.rounds.length) throw new IllegalArgumentException("No such round: "+round);
        if(questionNumber<0|| questionNumber>questionsPerRound) throw new IllegalArgumentException("no such question number: "+questionNumber);
        rounds[round].setAnswer(questionNumber, right);
    }

    public Boolean[] getRounds(int round){
        if(round<0 || round> rounds.length) throw new IllegalArgumentException("No such round: "+round);
        return rounds[round].getAnswers();
    }

    public long getPoints(int round){
        if(round<0 || round> rounds.length) throw new IllegalArgumentException("No such round: "+round);
        return rounds[round].getPoints();
    }
}
