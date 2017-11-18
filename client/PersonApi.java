package api;

//{"gender":0,"age":5,"hair":10,"color":28,"alcohol":41}

public class PersonApi {
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

    int gender;
    int age;
    int hair;
    int color;
    int alcohol;

    public PersonApi(int gender, int age, int hair, int color, int alcohol) {
        this.gender = gender;
        this.age = age;
        this.hair = hair;
        this.color = color;
        this.alcohol = alcohol;
    }
}
