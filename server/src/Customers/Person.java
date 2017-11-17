package Customers;

public class Person {

    public static final int FEMALE = 0;
    public static final int MALE = 1;
    public static final int GENDER_UNKNOWN = 2;

    public static final int AGE_0_19 = 5;
    public static final int AGE_20_25 = 6;
    public static final int AGE_26_30 = 7;
    public static final int AGE_30_DEAD = 8;

    public static final int BLONDE = 10;
    public static final int BROWN = 11;
    public static final int BLACK = 12;
    public static final int RED = 13;
    public static final int OTHER = 14;

    public static final int COLOR_YELLOW = 20;
    public static final int COLOR_ORANGE = 21;
    public static final int COLOR_RED = 22;
    public static final int COLOR_PINK = 23;
    public static final int COLOR_PURPLE = 24;
    public static final int COLOR_GREEN = 25;
    public static final int COLOR_BLUE = 26;
    public static final int COLOR_WHITE = 27;
    public static final int COLOR_BLACK = 28;

    public static final int RUM = 40;
    public static final int VODKA = 41;
    public static final int GIN = 42;
    public static final int WHISKEY = 43;
    public static final int BEER = 44;
    public static final int WINE = 45;



        public final int[] data;


        public Person(int gender, int age,int hair, int color, int alcohol){
            if(gender<0|| gender >2) throw new IllegalArgumentException("non-existing gender");
            if(age<5|| age >8) throw new IllegalArgumentException("non-existing age");
            if(hair<10|| hair >14) throw new IllegalArgumentException("non-existing hair");
            if(color<20|| color >28) throw new IllegalArgumentException("non-existing color");
            if(alcohol<40|| alcohol>45) throw new IllegalArgumentException("non-existing alcohol");

            data = new int[5];
            data[0] = gender;
            data[1] = age;
            data[2] = hair;
            data[3] = color;
            data[4] = alcohol;
        }

        // --------------------------------------------- getter --------------------------------------------------------

    public int getGender(){
            return data[1];
    }

    public int getAge(){
        return data[1];
    }

    public int getHair(){
        return data[2];
    }

    public int getColor(){
        return data[3];
    }

    public int getAlcohol(){
        return data[4];
    }
}
