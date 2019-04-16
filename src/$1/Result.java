package $1;

import tests.UserInput;

public class Result {
    public double score;
    public String result;
    public UserInput ui;

    public Result(double score, String result) {
        this.score = score;
        this.result = result;
    }

    public Result(double score, UserInput ui) {
        this.score = score;
        this.ui = ui;
    }

}
