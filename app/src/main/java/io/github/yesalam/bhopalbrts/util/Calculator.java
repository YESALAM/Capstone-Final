package io.github.yesalam.bhopalbrts.util;

/**
 * Created by yesalam on 20-08-2015.
 */

import android.util.Log;

import io.github.yesalam.bhopalbrts.data.AssetDatabaseHelper;
import io.github.yesalam.bhopalbrts.datamodel.CardData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Calculate the route and fare for the different routes . This class is
 * responsible for any algorithmic defect in this application .
 */
public class Calculator {

    private final String LOG_TAG = Calculator.class.getSimpleName();
    private String origin;
    private String destination;
    AssetDatabaseHelper dataBaseHelper;
    private float fare = 0 ;
    private String route = null ;


    public Calculator(String origin,String destination,AssetDatabaseHelper dataBaseHelper){
        this.origin = origin;
        this.destination = destination;
        this.dataBaseHelper = dataBaseHelper ;
    }

    /**
     * Perform the calculation over the specified set of data in constructor of this class .
     * This function need to be called First when minimum fare and shortest route is required .
     * @return list of CardData set .
     */
    public List<CardData> calc(){
        String[] buses_at_origin = dataBaseHelper.getBuses(origin).split("-");
        String[] buses_at_destination = dataBaseHelper.getBuses(destination).split("-");
        List<String> direct_buses = new ArrayList();

        //Check if there is any bus that go to both the places .
        //add them all to direct_buses list .
        for(int i=0;i<buses_at_origin.length;i++){
            for(int j=0;j<buses_at_destination.length;j++){
                if(buses_at_origin[i].equalsIgnoreCase(buses_at_destination[j])) {
                    direct_buses.add(buses_at_destination[j]);
                }
            }
        }

        if(direct_buses.size()>0){
            //There is/are direct bus available .
            CardData[] cardData = new CardData[direct_buses.size()];

            for(int i=0;i<direct_buses.size();i++){
                float int_distance = dataBaseHelper.getDistance(origin, destination, direct_buses.get(i));
                float distance = Util.twoDigitPrecision(int_distance);
                String bus = direct_buses.get(i);
                if(bus.equalsIgnoreCase("TR4AC")){
                    fare = calcFareAc(distance) ;
                } else {
                    fare = calcFare(distance) ;
                }
                cardData[i] = new CardData(bus, origin, destination,distance,fare);
            }
            //Insert the array of cardData into ArrayList of CardData .
            ArrayList<CardData> list_of_cardData = new ArrayList<CardData>();
            for (int i=0; i<direct_buses.size(); i++) {
                list_of_cardData.add(cardData[i]);
            }

            Collections.sort(list_of_cardData);
            fare = list_of_cardData.get(0).getFare() ;
            route = list_of_cardData.get(0).getBuses();
            return list_of_cardData ;

        } else {
            //No direct bus found
            //try to find connecting buses
            //only one bus change is possible through this algorithm
            ArrayList<CardData> list_of_cardData = new ArrayList<CardData>();

            for (String bus_at_origin : buses_at_origin) {
                List junctionList_of_bus_at_origin = dataBaseHelper.getJunctionList(bus_at_origin);
                for (String bus_at_destination : buses_at_destination) {
                    List junctionList_of_bus_at_destination = dataBaseHelper.getJunctionList(bus_at_destination);
                    List<String> list_of_common_Junction = commonJunction(junctionList_of_bus_at_origin, junctionList_of_bus_at_destination);
                    if (list_of_common_Junction.size() == 1) {
                        // THINGS ARE RIGHT Only one Junction is found
                        String junction = list_of_common_Junction.get(0);
                        Log.e(LOG_TAG, junction);
                        String connected_buses = bus_at_origin + " + " + bus_at_destination;
                        float integer_distance_junction = dataBaseHelper.getDistance(origin, junction, bus_at_origin);
                        float distance_of_juntion = Util.twoDigitPrecision(integer_distance_junction);
                        int fare_till_junction ;
                        if(bus_at_origin.equalsIgnoreCase("TR4AC")){
                            fare_till_junction = calcFareAc(distance_of_juntion);
                        }else {
                            fare_till_junction = calcFare(distance_of_juntion);
                        }

                        float integer_distance_destination = dataBaseHelper.getDistance(junction, destination, bus_at_destination);
                        float distance_of_destination =Util.twoDigitPrecision(integer_distance_destination);
                        int fare_till_destination ;
                        if(bus_at_destination.equalsIgnoreCase("TR4AC")){
                            fare_till_destination = calcFareAc(distance_of_destination);
                        } else {
                            fare_till_destination = calcFare(distance_of_destination);
                        }

                        float total_distance = distance_of_juntion + distance_of_destination;
                        float total_fare = fare_till_junction + fare_till_destination;
                        list_of_cardData.add(new CardData(connected_buses, origin, destination, total_distance, total_fare, junction));

                    } else if (list_of_common_Junction.size() == 0) {
                        //TODO Things are complecated . Need to change 3 buses ;

                    } else {
                        // more than one junction found. use one who is nearest to origin ;
                        String junction = getNearestJunction(list_of_common_Junction, origin, bus_at_origin);
                        Log.e(LOG_TAG, junction);
                        String connected_buses = bus_at_origin + " + " + bus_at_destination;
                        float integer_distance_junction = dataBaseHelper.getDistance(origin, junction, bus_at_origin);
                        float distance_of_juntion = Util.twoDigitPrecision(integer_distance_junction);
                        int fare_till_junction ;
                        if(bus_at_origin.equalsIgnoreCase("TR4AC")){
                            fare_till_junction = calcFareAc(distance_of_juntion);
                        }else {
                            fare_till_junction = calcFare(distance_of_juntion);
                        }

                        float integer_distance_destination = dataBaseHelper.getDistance(junction, destination, bus_at_destination);
                        float distance_of_destination =Util.twoDigitPrecision(integer_distance_destination);
                        int fare_till_destination ;
                        if(bus_at_destination.equalsIgnoreCase("TR4AC")){
                            fare_till_destination = calcFareAc(distance_of_destination);
                        } else {
                            fare_till_destination = calcFare(distance_of_destination);
                        }

                        float total_distance = distance_of_juntion + distance_of_destination;
                        float total_fare = fare_till_junction + fare_till_destination;
                        list_of_cardData.add(new CardData(connected_buses, origin, destination, total_distance, total_fare, junction));

                    }
                }
            }
            //Sort the data accordance with fare.
            Collections.sort(list_of_cardData);
            fare = list_of_cardData.get(0).getFare() ;
            route = list_of_cardData.get(0).getBuses();
            return list_of_cardData ;
        }

    }

    /**
     * Return the minimum fare from origin to destination.
     * Must have called calc() before calling this function .
     * @return fare in float .
     */

    public float getFare(){
        return fare;
    }

    /**
     * Return the route of bus of minimum fare .Must have called calc() before calling this function.
     * @return route as String.
     */
    public String getRoute(){
        return route;
    }

    /**
     * Find the common junction present in given lists .
     * @param l1 First list of junction.
     * @param l2 Second list of junction.
     * @return list of junction common in both list .
     */

    private List commonJunction(List<String> l1,List<String> l2){
        List<String> result = new ArrayList<>();
        for (String l1stop:l1){
            for(String l2stop:l2){
                if(l1stop.equalsIgnoreCase(l2stop)){
                    result.add(l2stop);
                }
            }
        }
        return  result ;
    }

    /**
     * Calculate the nearest junction from the origin in list of  junction for given bus.
     * @param junctions list of junction .
     * @param origin origin to start from.
     * @param bus bus of which junction is given.
     * @return nearest junction from origin.
     */

    private String getNearestJunction(List<String> junctions,String origin,String bus){
        float[] dist = new float[junctions.size()] ;
        String result = null ;
        //get the distance of all the junction from origin.
        for(int i=0;i<junctions.size();i++){
            dist[i] = dataBaseHelper.getDistance(origin,junctions.get(i),bus);
        }
        //store the distance in new array so that we can later get
        //the junction string from the index of above array.
        float[] distc = new float[dist.length] ;
        for(int i=0;i<dist.length;i++){
            distc[i] = dist[i] ;
        }
        //Sort the new array in increasing order.
        Arrays.sort(distc);

        //Get the string of index which matches the first value in new array from list of junction.
        for(int i=0;i<junctions.size();i++){
            if(dist[i]== distc[0]){
                result = junctions.get(i);
            }
        }

        return  result ;
    }

    /**
     * Calculate the fare for given distance .
     * This function need update whenever MyBus change fare.
     * @param distance the distance to calculate fare.
     * @return fare in integer .
     */
    private int calcFare(float distance){
        if(distance<=2.0){
            return 5;
        } else if(distance<=3.0){
            return 9;
        } else if(distance<=7.0){
            return 12;
        } else if(distance<=10.0){
            return 14;
        } else if(distance<=13.0){
            return 17;
        } else if (distance<=16.0){
            return 19;
        } else if (distance<=19.0){
            return 22;
        } else if (distance<=22.0){
            return 24;
        } else if(distance<=25.0){
            return 26;
        } else if (distance<=28.0){
            return 28;
        } else if (distance<= 30.0){
            return 30;
        } else if(distance<=34.0){
            return 32;
        }
        return 0;
    }

    private int calcFareAc(float distance){
        if(distance<=2.0){
            return 7;
        } else if(distance<=3.0){
            return 12;
        } else if(distance<=7.0){
            return 15;
        } else if(distance<=10.0){
            return 17;
        }else if(distance<=13.0){
            return 20;
        } else if (distance<=16.0){
            return 22;
        } else if (distance<=19.0){
            return 25;
        } else if (distance<=22.0){
            return 27;
        } else if(distance<=25.0){
            return 29;
        } else if (distance<=28.0){
            return 31;
        } else if (distance<= 30.0){
            return 33;
        } else if(distance<=34.0){
            return 35;
        }
        return 0;
    }
}
