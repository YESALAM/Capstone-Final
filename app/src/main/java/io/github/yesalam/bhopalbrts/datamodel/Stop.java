package io.github.yesalam.bhopalbrts.datamodel;


/**
 * Created by Sadar-e- on 7/14/2015.
 */

/**
 * This class help in dealing with the stop .
 * The main purpose of this class is finding nearest stop from many stops .
 */
public class Stop implements Comparable<Stop> {
    private String stop ;

    private String vicinity ;
    private float dist ;
    private double lattitude ;
    private double longitude ;
    private boolean isJunction  ;


    public Stop(){
        this.isJunction = false ;
    }

    public Stop(String stop,float dist){
        this.stop = stop ;
        this.dist = dist ;
        this.isJunction = false ;
    }

    public Stop(String stop,float dist,boolean isJunction){
        this.stop = stop ;
        this.dist = dist ;
        this.isJunction = isJunction ;
    }


    public float getDist() {
        return dist;
    }

    public void setDist(float dist) {
        this.dist = dist;
    }

    public String getStop() {       return stop;    }

    public void setStop(String stop) {        this.stop = stop;    }

    public boolean isJunction() {
        return isJunction;
    }

    public void setIsJunction(boolean isJunction) {
        this.isJunction = isJunction;
    }

    public double getLongitude() {        return longitude;    }

    public void setLongitude(double longitude) {        this.longitude = longitude;    }

    public double getLattitude() {        return lattitude;    }

    public void setLattitude(double lattitude) {        this.lattitude = lattitude;    }
    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }








    @Override
    public int compareTo(Stop another) {
        if(this.dist<another.dist){
            return -1 ;
        } else if(this.dist>another.dist){
            return 1;
        } else {
            return 0 ;
        }

    }
}
