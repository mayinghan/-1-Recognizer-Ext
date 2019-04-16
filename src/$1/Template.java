package $1;

import java.util.List;

public class Template {
    public String name;
    public List<Point> point;
    private double totalAngle;
    public Template(String name, List<Point> point) {
        this.name = name;
        //preprocess

        ResmpGesture resampledGes = Process.resample(point, 64);
        this.totalAngle = resampledGes.getTotalAngle();

        System.out.println(name + " " + this.totalAngle);
        //if the gesture is not recognized as a 1D gesture, then continue the rest 3 preprocess steps
        if(!resampledGes.getIs1D()) {
            this.point = resampledGes.getPointList();
            if(this.point == null) System.out.println("null for templates");
            this.point = Process.rotateToZero(this.point);
            this.point = Process.scaleToSquare(this.point, 300.0);
            this.point = Process.translateToZero(this.point);
        }
    }

    public double getTotalAngle() {
        return this.totalAngle;
    }
}







