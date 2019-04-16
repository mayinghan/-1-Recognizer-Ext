package tests;

import $1.Point;
import $1.Process;
import $1.ResmpGesture;

import java.util.List;

public class UserInput {
    public String name;
    public List<Point> point;
    public int user;
    public String speed;
    public int id;
    private double totalAngle;
    public UserInput(String name, List<Point> point, int user, String speed, int id) {
        this.name = name;
        this.user = user;
        this.speed = speed;
        this.id = id;
        //preprocess
        ResmpGesture gesture = Process.resample(point, 64);
        this.totalAngle = gesture.getTotalAngle();
        if(!gesture.getIs1D()) {
            this.point = gesture.getPointList();
            this.point = Process.rotateToZero(this.point);
            this.point = Process.scaleToSquare(this.point, 300.0);
            this.point = Process.translateToZero(this.point);
        }
    }

    public UserInput(String name, List<Point> userPoint, int user, String speed) {}

    public double getTotalAngle() {
        return this.totalAngle;
    }
}
