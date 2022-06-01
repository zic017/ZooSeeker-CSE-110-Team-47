package com.example.myapplication;


import android.content.Context;
import android.location.Location;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.Map;

public class DirectionsAlgorithm {

    // List of the IDs of every planned

    public ArrayList<String> plannedIds, visitedIds;
    public String current;
    public String currentName;
    public Context context;
    String briefDirectionsLine;
    String detailedDirectionsLine;
    public ArrayList<String> directions;
    Graph<String, IdentifiedWeightedEdge> g;
    Map<String, ZooData.VertexInfo> vInfo;
    double longitude, latitude;
    public int size;


    public DirectionsAlgorithm(ArrayList<String> plannedIds, Context context, double latitude, double longitude){
        this.plannedIds = plannedIds;
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
        size = plannedIds.size();
        this.visitedIds = new ArrayList<>();
        g = ZooData.loadZooGraphJSON(context,"zoo_graph.json");
        vInfo = ZooData.loadVertexInfoJSON(context, "zoo_node_info.json");

        current = "entrance_exit_gate";
    }

    //Get the closest vertex to current location
    private String getCurrent() {
        String closest = "";
        double lowest = 9999999;
        float[] results = new float[1];
        for(Map.Entry<String, ZooData.VertexInfo> vertex : vInfo.entrySet()) {
            Location.distanceBetween(latitude, longitude, vInfo.get(vertex.getKey()).lat, vInfo.get(vertex.getKey()).lng, results);
            if((double) results[0] < lowest ) {
                lowest = (double)results[0];
                closest = vInfo.get(vertex.getKey()).id;
            }
        }
        return closest;
    }


    public void getNext() {
        String next;
        //If no more exhibits, return to gate
        if(current.equals("entrance_exit_gate") && plannedIds.size() == 0) {
            currentName = "DONE";
        }
        else if(plannedIds.size() == 0) {
            setBriefDirections(current, "entrance_exit_gate");
            setDetailedDirections(current, "entrance_exit_gate");
            visitedIds.add("entrance_exit_gate");
            current = "entrance_exit_gate";
            currentName = vInfo.get(current).name;
        }
        else {
            next = closest();
            current = getCurrent();
            if(!current.equals(next)) {
                setBriefDirections(current, next);
                setDetailedDirections(current, next);
                currentName = vInfo.get(next).name;
                visitedIds.add(next);
                plannedIds.remove(next);
            }
        }
    }


    public void getPrevious() {
        //If no previous, do nothing
        if (visitedIds.size() > 1) {
            String previous = visitedIds.get(visitedIds.size()-2);
            current = getCurrent();
            currentName = vInfo.get(visitedIds.get(visitedIds.size()-2)).name;
            setBriefDirections(current, previous);
            setDetailedDirections(current, previous);
            plannedIds.add(0,visitedIds.get(visitedIds.size()-1));
            visitedIds.remove(visitedIds.size()-1);
        }
    }

    //get the closest planned exhibit to the current location
    public String closest() {
        int minDistance = 999999999;
        int curTotal;
        String close = current;
        GraphPath<String, IdentifiedWeightedEdge> path;

        //For each other exhibit planned besides the current one
        for(String id : plannedIds) {
            if(vInfo.get(id).group_id != null) {
                path = DijkstraShortestPath.findPathBetween(g, current, vInfo.get(id).group_id);
            }
            else {
                path = DijkstraShortestPath.findPathBetween(g, current, id);
            }
            curTotal = 0;

            //Get total distance to this exhibit
            for (IdentifiedWeightedEdge e : path.getEdgeList()) {
                curTotal += g.getEdgeWeight(e);
            }

            //Check if new shortest exhibit
            if(curTotal < minDistance) {
                minDistance = curTotal;
                close = id;
            }
        }
        return close;
    }

    public void setDetailedDirections(String current, String next) {
        detailedDirectionsLine = "";
        String loc1, loc2, tempcur;
        // 1. Load the graph...


        // 2. Load the information about our nodes and edges...
        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(context,"zoo_node_info.json");
        Map<String, ZooData.EdgeInfo> eInfo = ZooData.loadEdgeInfoJSON(context, "trail_info.json");

        if(!(vInfo.get(next).group_id == null) && current.equals(vInfo.get(next).group_id)) {
            detailedDirectionsLine = "1. Find the " + vInfo.get(next).name + " inside the " + vInfo.get(current).name;
        }
        else if(current.equals(next)) {
            detailedDirectionsLine = "1. You are currently at the " + vInfo.get(next).name + " exhibit";
        }
        else {
            GraphPath<String, IdentifiedWeightedEdge> path;

            if(vInfo.get(current).group_id != null && vInfo.get(next).group_id != null) {
                path = DijkstraShortestPath.findPathBetween(g, vInfo.get(current).group_id, vInfo.get(next).group_id);
            }
            else if(vInfo.get(next).group_id != null) {
                path = DijkstraShortestPath.findPathBetween(g, current, vInfo.get(next).group_id);
            }
            else if(vInfo.get(current).group_id != null) {
                path = DijkstraShortestPath.findPathBetween(g, vInfo.get(current).group_id, next);
            }
            else {
                path = DijkstraShortestPath.findPathBetween(g, current, next);
            }

            int i = 1;
            tempcur = current;
            for (IdentifiedWeightedEdge e : path.getEdgeList()) {
                String name;
                String inGroup = "";

                loc1 = vInfo.get(g.getEdgeSource(e)).id;
                loc2 = vInfo.get(g.getEdgeTarget(e)).id;

                if (loc1.equals(tempcur) || loc2.equals(next)) {
                    if(vInfo.get(next).group_id != null && loc2.equals(vInfo.get(next).group_id)) {
                        inGroup = "and find the " + vInfo.get(next).name + " exhibit inside";
                    }
                    if(vInfo.get(g.getEdgeTarget(e)).kind.toString().equals("INTERSECTION")) {
                        name = vInfo.get(g.getEdgeTarget(e)).name;
                        name = "corner of " + name.replace(" / ", " and ");

                    }
                    else if(vInfo.get(g.getEdgeTarget(e)).kind.toString().equals("EXHIBIT_GROUP")) {
                        name= vInfo.get(g.getEdgeTarget(e)).name;
                    }
                    else if(vInfo.get(g.getEdgeTarget(e)).kind.toString().equals("EXHIBIT")){
                        name = "the " + vInfo.get(g.getEdgeTarget(e)).name + " exhibit";
                    }
                    else {
                        name = "the " + vInfo.get(g.getEdgeTarget(e)).name;
                    }


                    detailedDirectionsLine = detailedDirectionsLine + String.format("  %d. Proceed on %s %.0f feet towards %s %s\n",
                            i,
                            eInfo.get(e.getId()).street,
                            g.getEdgeWeight(e),
                            name,
                            inGroup) + "\n";
                    tempcur = loc2;
                }
                else {
                    if(vInfo.get(next).group_id != null && loc1.equals(vInfo.get(next).group_id)) {
                        inGroup = "and find the " + vInfo.get(next).name + " exhibit inside";
                    }

                    if(vInfo.get(g.getEdgeSource(e)).kind.toString().equals("INTERSECTION")) {
                        name = vInfo.get(g.getEdgeSource(e)).name;
                        name = "corner of " + name.replace(" / ", " and ");

                    }
                    else if(vInfo.get(g.getEdgeSource(e)).kind.toString().equals("EXHIBIT_GROUP")) {
                        name= vInfo.get(g.getEdgeSource(e)).name;
                    }
                    else if(vInfo.get(g.getEdgeSource(e)).kind.toString().equals("EXHIBIT")){
                        name = "the " + vInfo.get(g.getEdgeSource(e)).name + " exhibit";
                    }
                    else {
                        name = "the " + vInfo.get(g.getEdgeSource(e)).name;
                    }

                    detailedDirectionsLine = detailedDirectionsLine + String.format("  %d. Proceed on %s %.0f feet towards %s %s\n",
                            i,
                            eInfo.get(e.getId()).street,
                            g.getEdgeWeight(e),
                            name,
                            inGroup) + "\n";
                    tempcur = loc1;
                }
                i++;
            }
        }
    }

    public void setBriefDirections(String current, String next) {
        briefDirectionsLine = "";
        String loc1, loc2, tempcur;

        // 2. Load the information about our nodes and edges...
        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(context,"zoo_node_info.json");
        Map<String, ZooData.EdgeInfo> eInfo = ZooData.loadEdgeInfoJSON(context, "trail_info.json");

        if(!(vInfo.get(next).group_id == null) && current.equals(vInfo.get(next).group_id)) {
            briefDirectionsLine = "1. Find the " + vInfo.get(next).name + " inside the " + vInfo.get(current).name;
        }
        else if(current.equals(next)) {
            briefDirectionsLine = "1. You are currently at the " + vInfo.get(next).name + " exhibit";
        }
        else {
            GraphPath<String, IdentifiedWeightedEdge> path;

            if(vInfo.get(current).group_id != null && vInfo.get(next).group_id != null) {
                path = DijkstraShortestPath.findPathBetween(g, vInfo.get(current).group_id, vInfo.get(next).group_id);
            }
            else if(vInfo.get(next).group_id != null) {
                path = DijkstraShortestPath.findPathBetween(g, current, vInfo.get(next).group_id);
            }
            else if(vInfo.get(current).group_id != null) {
                path = DijkstraShortestPath.findPathBetween(g, vInfo.get(current).group_id, next);
            }
            else {
                path = DijkstraShortestPath.findPathBetween(g, current, next);
            }

            int i = 1;
            tempcur = current;

            int accWeight = 0;
            for (int e = 0; e < path.getEdgeList().size(); e++) {
                String name;
                String inGroup = "";

                loc1 = vInfo.get(g.getEdgeSource(path.getEdgeList().get(e))).id;
                loc2 = vInfo.get(g.getEdgeTarget(path.getEdgeList().get(e))).id;

                if ((e + 1) < path.getEdgeList().size() && eInfo.get(path.getEdgeList().get(e).getId()).street.equals(eInfo.get(path.getEdgeList().get(e + 1).getId()).street)) {
                    accWeight += g.getEdgeWeight(path.getEdgeList().get(e));
                    if (loc1.equals(tempcur) || loc2.equals(next)) {
                        tempcur = loc2;
                    }
                    else {
                        tempcur = loc1;
                    }
                }
                else {
                    if (loc1.equals(tempcur) || loc2.equals(next)) {
                        if(vInfo.get(next).group_id != null && loc2.equals(vInfo.get(next).group_id)) {
                            inGroup = "and find the " + vInfo.get(next).name + " exhibit inside";
                        }
                        if(vInfo.get(g.getEdgeTarget(path.getEdgeList().get(e))).kind.toString().equals("INTERSECTION")) {
                            name = vInfo.get(g.getEdgeTarget(path.getEdgeList().get(e))).name;
                            name = "corner of " + name.replace(" / ", " and ");

                        }
                        else if(vInfo.get(g.getEdgeTarget(path.getEdgeList().get(e))).kind.toString().equals("EXHIBIT_GROUP")) {
                            name= vInfo.get(g.getEdgeTarget(path.getEdgeList().get(e))).name;
                        }
                        else if(vInfo.get(g.getEdgeTarget(path.getEdgeList().get(e))).kind.toString().equals("EXHIBIT")){
                            name = "the " + vInfo.get(g.getEdgeTarget(path.getEdgeList().get(e))).name + " exhibit";
                        }
                        else {
                            name = "the " + vInfo.get(g.getEdgeTarget(path.getEdgeList().get(e))).name;
                        }
                        briefDirectionsLine = briefDirectionsLine + String.format("  %d. Proceed on %s %.0f feet towards %s %s\n",
                                i,
                                eInfo.get(path.getEdgeList().get(e).getId()).street,
                                g.getEdgeWeight(path.getEdgeList().get(e)) + accWeight,
                                name,
                                inGroup) + "\n";
                        tempcur = loc2;
                    }
                    else {
                        if(vInfo.get(next).group_id != null && loc1.equals(vInfo.get(next).group_id)) {
                            inGroup = "and find the " + vInfo.get(next).name + " exhibit inside";
                        }
                        if(vInfo.get(g.getEdgeSource(path.getEdgeList().get(e))).kind.toString().equals("INTERSECTION")) {
                            name = vInfo.get(g.getEdgeSource(path.getEdgeList().get(e))).name;
                            name = "corner of " + name.replace(" / ", " and ");

                        }
                        else if(vInfo.get(g.getEdgeSource(path.getEdgeList().get(e))).kind.toString().equals("EXHIBIT_GROUP")) {
                            name= vInfo.get(g.getEdgeSource(path.getEdgeList().get(e))).name;
                        }
                        else if(vInfo.get(g.getEdgeSource(path.getEdgeList().get(e))).kind.toString().equals("EXHIBIT")){
                            name = "the " + vInfo.get(g.getEdgeSource(path.getEdgeList().get(e))).name + " exhibit";
                        }
                        else {
                            name = "the " + vInfo.get(g.getEdgeSource(path.getEdgeList().get(e))).name;
                        }

                        briefDirectionsLine = briefDirectionsLine + String.format("  %d. Proceed on %s %.0f feet towards %s %s\n",
                                i,
                                eInfo.get(path.getEdgeList().get(e).getId()).street,
                                g.getEdgeWeight(path.getEdgeList().get(e)) + accWeight,
                                name,
                                inGroup) + "\n";
                        tempcur = loc1;
                    }
                    accWeight = 0;
                    i++;
                }
            }
        }
    }

    public String getBriefDirections() {
        return briefDirectionsLine;
    }
    public String getDetailedDirections() {
        return detailedDirectionsLine;
    }

    public void updateLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

        if(!current.equals(getCurrent())) {
            current = getCurrent();
            if(closerTo(current)) {
                ((DirectionsActivity)context).promptReplan();
            }
        }
    }

    public void replan() {
        plannedIds.add(visitedIds.get(visitedIds.size() - 1));
        visitedIds.remove(visitedIds.get(visitedIds.size() - 1));
        ((DirectionsActivity)context).showReplan();
    }

    public void replanSkip() {
        visitedIds.remove(visitedIds.get(visitedIds.size() - 1));
        ((DirectionsActivity)context).showReplan();
    }

    public boolean closerTo(String current) {

        for(String id: plannedIds) {
            // If current location is closer to a later planned exhibit
            if(vInfo.get(id).group_id == null && vInfo.get(visitedIds.get(visitedIds.size() - 1)).group_id == null) {
                if (DijkstraShortestPath.findPathBetween(g, current, id).getWeight() < DijkstraShortestPath.findPathBetween(g, current, visitedIds.get(visitedIds.size() - 1)).getWeight()) {
                    return true;
                }
            }
            else if(vInfo.get(id).group_id != null && vInfo.get(visitedIds.get(visitedIds.size() - 1)).group_id == null) {
                if (DijkstraShortestPath.findPathBetween(g, current, vInfo.get(id).group_id).getWeight() < DijkstraShortestPath.findPathBetween(g, current, visitedIds.get(visitedIds.size() - 1)).getWeight()) {
                    return true;
                }
            }
            else if(vInfo.get(id).group_id == null && vInfo.get(visitedIds.get(visitedIds.size() - 1)).group_id != null) {
                if (DijkstraShortestPath.findPathBetween(g, current, id).getWeight() < DijkstraShortestPath.findPathBetween(g, current, vInfo.get(visitedIds.get(visitedIds.size() - 1)).group_id).getWeight()) {
                    return true;
                }
            }
            else {
                if (DijkstraShortestPath.findPathBetween(g, current, vInfo.get(id).group_id).getWeight() < DijkstraShortestPath.findPathBetween(g, current, vInfo.get(visitedIds.get(visitedIds.size() - 1)).group_id).getWeight()) {
                    return true;
                }
            }
        }
        return false;
    }

     public void skipExhibit() {

     }

    /**
     * Get the distance for every exhibit from the entrance
     * @param list
     * @return ArrayList of distances for each exhibit
     */
    public ArrayList<Integer> getDistances (ArrayList<String> list){
        GraphPath<String, IdentifiedWeightedEdge> path;
        ArrayList<Integer> distances = new ArrayList<Integer>();
        int curTotal = 0;

        for(String exhibit : list){
            if(vInfo.get(exhibit).group_id != null)
            {
                path = DijkstraShortestPath.findPathBetween(g, current, vInfo.get(exhibit).group_id);
            }
            else
            {
                path = DijkstraShortestPath.findPathBetween(g, current, exhibit);
            }

            curTotal = 0;
            for (IdentifiedWeightedEdge e : path.getEdgeList()) {
                curTotal += g.getEdgeWeight(e);
            }
            distances.add(curTotal);
        }

        return distances;
    }
}