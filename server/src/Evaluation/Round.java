package Evaluation;

public class Round {
    private Boolean[] answers; // right := true; false:= false


    public Round(int size){
        this.answers = new Boolean[size];
    }

    public Round(){
        this(10);
    }





    // ----------------------------------------------- getter & setter -------------------------------------------------

    public void setAnswer(int questionNumber, boolean right){
        if(questionNumber<0 || questionNumber>answers.length) throw new IllegalArgumentException("Wrong question number");
        answers[questionNumber] = right;
    }


    public Boolean[] getAnswers(){
        return answers;
    }

    public long getPoints(){
        int right = 0;
        for(boolean b:answers) {
            if(b) right++;
        }
        return right;
    }

}
