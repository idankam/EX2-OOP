package implementations;
import api.*;
public class GeoLoc implements GeoLocation {

    private double x;
    private double y;
    private double z;

    public GeoLoc(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double x(){
        return x;
    }
    public double y(){
        return y;
    }
    public double z(){
        return z;
    }

    @Override
    public double distance(GeoLocation g) {
        double dist = Math.sqrt(Math.pow(this.x - g.x(), 2) + Math.pow(this.y - g.y(), 2) + Math.pow(this.z - g.z(), 2));
        return dist;
    }

    public GeoLoc copy(){
        GeoLoc temp = new GeoLoc(x, y, z);
        return temp;
    }
}
