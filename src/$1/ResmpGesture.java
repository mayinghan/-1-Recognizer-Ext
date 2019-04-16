package $1;

import java.util.*;

/*
    This class is for the purpose of saving the information for a gesture after the
    first step of preprocess --- resampling.
    After resampling, the total turning angle and the result point list will be saved as a ResmpGesture object
 */
public class ResmpGesture {
    private double totalAngle;
    private List<Point> points;
    private boolean is1D;

    public ResmpGesture(double totalAngle, List<Point> points) {
        /*
        need to improve: threshold
        current threshold: 3
         */

        if(totalAngle <= 3.0) {

            System.out.println("this is 1D");

            this.is1D = true;
        } else {
            this.is1D = false;
        }
        this.totalAngle = totalAngle;
        this.points = new ArrayList<>(points);
    }

    public boolean getIs1D() {
        return this.is1D;
    }

    public List<Point> getPointList() {
        return points;
    }

    public double getTotalAngle() {
        return this.totalAngle;
    }
}
