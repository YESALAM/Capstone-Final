package io.github.yesalam.bhopalbrts.datamodel;

/**
 * Created by Sadar-e- on 7/11/2015.
 */

/**
 * This is datamodel for the card shown in the bus_route seatch result .
 */
public class CardData  implements  Comparable<CardData> {
    private String buses ;
    private String from ;
    private String to ;
    private float fare;
    private float dist ;
    private String junctions ;



    public CardData(String buses,String from,String to,float dist,float fare){
        this.from = from ;
        this.buses = buses ;
        this.to = to ;
        this.dist = dist ;
        this.fare = fare ;
        this.junctions = null ;
    }
    public CardData(String buses,String from,String to,float dist,float fare,String junctions){
        this.from = from ;
        this.buses = buses ;
        this.to = to ;
        this.dist = dist ;
        this.fare = fare ;
        this.junctions = junctions ;

    }


    public String getJunctions() {
        return junctions;
    }

    public void setJunctions(String junctions) {
        this.junctions = junctions;
    }

    public String getBuses() {
        return buses;
    }

    public void setBuses(String buses) {
        this.buses = buses;
    }

    public float getDist() {
        return dist;
    }

    public void setDist(float dist) {
        this.dist = dist;
    }

    public float getFare() {

        return fare;
    }

    public void setFare(float fare) {
        this.fare = fare;
    }

    public String getTo() {

        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {

        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }


    @Override
    public int compareTo(CardData another) {

        if(dist<another.dist )
        {
            if(fare<another.fare) {
                return -1;
            } else if (fare>another.fare)
            {
                return 1;
            } else {
                return -1;
            }
        } else if(dist>another.dist){
            if(fare<another.fare){
                return -1;
            } else if(fare>another.fare){
                return 1;
            } else {
                return 1;
            }
        } else {
            if(fare<another.fare){
                return -1;
            } else if(fare>another.fare){
                return 1;
            } else {
                return 0;
            }
        }


    }
}
