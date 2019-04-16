package $1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//$1 preprocess and comparision
public class Process {

    public static ResmpGesture resample(List<Point> p, int n) {
        //here we are using List from java.util
        //System.out.println("Size before resample: " + p.size());
        List<Point> result = new ArrayList<>(n);
        double seg = pathLength(p) / (n - 1);

        result.add(p.get(0));

        //resample
        double D = 0.0;

        for(int i = 1; i < p.size(); i++) {
            Point p1 = p.get(i - 1);
            Point p2 = p.get(i);

            double d = getDistance(p1, p2);
            if((D+ d) >= seg) {
                double newx = p1.getX() + ((seg - D) / d) * (p2.getX() - p1.getX());
                double newy = p1.getY() + ((seg - D) / d) * (p2.getY() - p1.getY());
                Point newP = new Point(newx, newy);
                result.add(newP);
                //System.out.println(i);
                p.add(i, newP);
                //reset init value
                D = 0.0;
            } else {
                D += d;
            }
        }

        //test the totalAngle value
        //System.out.println(totalAngle);

        if(result.size() == n - 1) {
            result.add(p.get(p.size() - 1));
        }

        //calculate the total turning angle based on the resampled gesture
        double totalAngle = 0.0;
        for(int i = 1; i < result.size() - 1; i++) {
            Point a = result.get(i - 1);
            Point b = result.get(i);
            Point c = result.get(i + 1);
            double pathAB = getDistance(a, b);
            double pathBC = getDistance(b, c);

            if (Math.abs(pathAB * pathBC) == 0.00) {
                totalAngle += 0.0;
            } else {
                //compute the cosine angle between two vectors using Law of Cosines
                double cosAngle = ((b.getX() - a.getX()) * (c.getX() - b.getX()) + (b.getY() - a.getY()) * (c.getY() - b.getY())) / (pathAB * pathBC);
                //System.out.println(cosAngle);
                //edge case
                if (cosAngle <= -1.0) {
                    totalAngle += Math.PI;
                } else if (cosAngle >= 1.0) {
                    totalAngle += 0;
                } else {
                    totalAngle += Math.acos(cosAngle);
                }
            }
        }

        //System.out.println("Size after resample: " + result.size());
        //Gesture processGesture = new Gesture(totalAngle, result);
        return new ResmpGesture(totalAngle, result);
    }

    public static double pathLength(List<Point> p) {
        double len = 0;
        for(int i = 1; i < p.size(); i++) {
            len += getDistance(p.get(i - 1), p.get(i));
        }
        return len;
    }

    public static List<Point> rotateToZero(List<Point> p) {
        Point center = center(p);
        Point first = p.get(0);
        double angle = Math.atan2(center.getY() - first.getY(), center.getX() - first.getX());

        return rotate(p, angle);
    }

    public static List<Point> scaleToSquare(List<Point> p, double size) {
        List<Point> result = new ArrayList<>(p.size());
        BoundBox bb = generateBox(p);

        for(int i = 0; i < p.size(); i++) {
            Point pt = p.get(i);
            double newX = pt.getX() * (size / (bb.getMaxX() - bb.getMinX()));
            double newY = pt.getY() * (size / (bb.getMaxY() - bb.getMinY()));
            result.add(new Point(newX, newY));
        }

        return result;
    }

    public static List<Point> translateToZero(List<Point> p) {
        Point c = center(p);
        List<Point> result = new ArrayList<>(p.size());
        for(Point a : p) {
            double newX = a.getX() - c.getX();
            double newY = a.getY() - c.getY();
            result.add(new Point(newX, newY));
        }

        return result;
    }


    //Helper functions and recognizing functions

    public static double pathDistance(List<Point> c, List<Point> t) {
        double dist = 0;
        //System.out.println("Size of cand: " + c.size());
        //System.out.println("Size of template: " + t.size());
        if(t == null) System.out.println("template is null");
        if(c == null) System.out.println("candidate is null");
        for(int i = 0; i < t.size(); i++) {
            dist += getDistance(t.get(i), c.get(i));
        }

        return dist / t.size();
    }

    public static double distanceAtAngle(List<Point> c, Template T, double angle) {
        List<Point> result = rotate(c, angle);
        return pathDistance(result, T.point);
    }

    /**
     * This function is for online/offline testing, it will calculate the euclidean distance between two point lists
     * @param c: point list for gesture input
     * @param T: template object for comparison
     * @param a angle1
     * @param b angle2
     * @param delta adjustment angle
     * @return the shortest euclidean distance between to point lists after slightly rotation(golden search)
     */
    public static double bestDistance(List<Point> c, Template T, double a, double b, double delta) {
        double phi = (-1 + Math.sqrt(5)) / 2;
        double x1=(phi*a)+((1.0-phi)*b);
        double r1=distanceAtAngle(c,T,x1);
        double x2=((1.0-phi)*a)+(phi*b);
        double r2=distanceAtAngle(c,T,x2);
        while(Math.abs(b-a)>delta){
            if(r1<r2){
                b=x2;
                x2=x1;
                r2=r1;
                x1=(phi*a)+((1.0-phi)*b);
                r1=distanceAtAngle(c,T,x1);
            }
            else{
                a=x1;
                x1=x2;
                r1=r2;
                x2=(1.0-phi)*a+(phi*b);
                r2=distanceAtAngle(c,T,x2);
            }
        }
        return Math.min(r1, r2);
    }


    public static BoundBox generateBox(List<Point> p) {
        double minx = Double.MAX_VALUE;
        double miny = Double.MAX_VALUE;
        double maxx = Double.MIN_VALUE;
        double maxy = Double.MIN_VALUE;

        for(int i = 0; i < p.size(); i++) {
            minx = minx > p.get(i).getX() ? p.get(i).getX() : minx;
            miny = miny > p.get(i).getY() ? p.get(i).getY() : miny;
            maxx = maxx < p.get(i).getX() ? p.get(i).getX() : maxx;
            maxy = maxy < p.get(i).getY() ? p.get(i).getY() : maxy;
        }

        return new BoundBox(minx, miny, maxx, maxy);
    }

    public static List<Point> rotate(List<Point> p, double angle) {
        List<Point> rotImg = new ArrayList<>(p.size());
        Point center = center(p);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        for(int i = 0; i < p.size(); i++) {
            Point curr = p.get(i);
            double dx = curr.getX() - center.getX();
            double dy = curr.getY() - center.getY();
            double newX = dx * cos - dy * sin + center.getX();
            double newY = dx * sin + dy * cos + center.getY();
            rotImg.add(curr);
        }

        return rotImg;
    }

    public static double getDistance(Point p1, Point p2) {
        double dx = p1.getX() - p2.getX();
        double dy = p1.getY() - p2.getY();
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

    public static Point center(List<Point> p) {
        double sumx = 0.0;
        double sumy = 0.0;

        for(int i = 0; i < p.size(); i++) {
            sumx += p.get(i).getX();
            sumy += p.get(i).getY();
        }

        double centerX = sumx / p.size();
        double centerY = sumy / p.size();

        return new Point(centerX, centerY);
    }
}


class BoundBox {
    private double minX, minY, maxX, maxY;

    public BoundBox(double minX, double minY, double maxX, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxY() {
        return maxY;
    }
}

