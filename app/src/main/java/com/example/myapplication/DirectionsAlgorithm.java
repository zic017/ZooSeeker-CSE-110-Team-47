package com.example.myapplication;


import android.content.Context;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.Map;

public class DirectionsAlgorithm {

    // List of the IDs of every planned
    public ArrayList<String> plannedIds;
    public String current;
    public String currentName;
    public Context context;
    String briefDirectionsLine;
    String detailedDirectionsLine;
    public ArrayList<String> directions;
    Graph<String, IdentifiedWeightedEdge> g;
    Map<String, ZooData.VertexInfo> vInfo;

    public DirectionsAlgorithm(ArrayList<String> plannedIds, Context context){
        this.plannedIds = plannedIds;
        this.context = context;
        //Always start at the entrance gate
        current = "entrance_exit_gate";
        g = ZooData.loadZooGraphJSON(context,"zoo_graph.json");
        vInfo = ZooData.loadVertexInfoJSON(context, "zoo_node_info.json");
    }

    public void getNext() {
        String next;

        //If no more exhibits, return to gate
        if(current == "entrance_exit_gate" && plannedIds.size() == 0) {
            currentName = "DONE";
        }
        else if(plannedIds.size() == 0) {
            getBriefDirections(current, "entrance_exit_gate");
            getDetailedDirections(current, "entrance_exit_gate");
            current = "entrance_exit_gate";
            currentName = vInfo.get(current).name;
        }
        else {
            next = closest();
            getBriefDirections(current, next);
            getDetailedDirections(current, next);
            current = next;
            currentName = vInfo.get(current).name;
            plannedIds.remove(current);
        }
    }

    //get the closest exhibit to the current location
    public String closest() {
        int minDistance = 999999999;
        int curTotal;
        String close = current;

        //For each other exhibit planned besides the current one
        for(String id : plannedIds) {
            curTotal = 0;
            // 1. Load the graph...

            GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, current, id);

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

    public void getDetailedDirections(String current, String next) {
        detailedDirectionsLine = "";
        String loc1, loc2, tempcur;
        // 1. Load the graph...
        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, current, next);

        // 2. Load the information about our nodes and edges...
        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(context,"zoo_node_info.json");
        Map<String, ZooData.EdgeInfo> eInfo = ZooData.loadEdgeInfoJSON(context, "trail_info.json");

        int i = 1;
        tempcur = current;
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            String name;

            loc1 = vInfo.get(g.getEdgeSource(e)).id;
            loc2 = vInfo.get(g.getEdgeTarget(e)).id;

            if (loc1.equals(tempcur) || loc2.equals(next)) {
                if(vInfo.get(g.getEdgeTarget(e)).kind.toString().equals("INTERSECTION")) {
                    name = vInfo.get(g.getEdgeTarget(e)).name;
                    if(name.contains(eInfo.get(e.getId()).street)) {
                        name = name.replace(" / ", "");
                        name = name.replace(eInfo.get(e.getId()).street, "");
                    }
                    else {
                        name = name.replace("/", "and");
                    }
                }
                else {
                    name = "the " + vInfo.get(g.getEdgeTarget(e)).name + " exhibit";
                }


                detailedDirectionsLine = detailedDirectionsLine + String.format("  %d. Proceed on %s %.0f feet towards %s\n",
                        i,
                        eInfo.get(e.getId()).street,
                        g.getEdgeWeight(e),
                        name) + "\n";
                tempcur = loc2;
            }
            else {
                if(vInfo.get(g.getEdgeSource(e)).kind.toString().equals("INTERSECTION")) {
                    name = vInfo.get(g.getEdgeSource(e)).name;
                    name = name.replace(" / ", "");
                    name = name.replace(eInfo.get(e.getId()).street, "");

                }
                else {
                    name = "the " + vInfo.get(g.getEdgeSource(e)).name + " exhibit";
                }

                detailedDirectionsLine = detailedDirectionsLine + String.format("  %d. Proceed on %s %.0f feet towards %s\n",
                        i,
                        eInfo.get(e.getId()).street,
                        g.getEdgeWeight(e),
                        name) + "\n";
                tempcur = loc1;
            }
            i++;
        }
    }

    public void getBriefDirections(String current, String next) {
        briefDirectionsLine = "";
        String loc1, loc2, tempcur;
        // 1. Load the graph...
        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, current, next);

        // 2. Load the information about our nodes and edges...
        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(context,"zoo_node_info.json");
        Map<String, ZooData.EdgeInfo> eInfo = ZooData.loadEdgeInfoJSON(context, "trail_info.json");

        int i = 1;
        tempcur = current;

        int accWeight = 0;
        for (int e = 0; e < path.getEdgeList().size(); e++) {
            String name;

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
                    if(vInfo.get(g.getEdgeTarget(path.getEdgeList().get(e))).kind.toString().equals("INTERSECTION")) {
                        name = vInfo.get(g.getEdgeTarget(path.getEdgeList().get(e))).name;
                        if(name.contains(eInfo.get(path.getEdgeList().get(e).getId()).street)) {
                            name = name.replace(" / ", "");
                            name = name.replace(eInfo.get(path.getEdgeList().get(e).getId()).street, "");
                        }
                        else {
                            name = name.replace("/", "and");
                        }
                    }
                    else {
                        name = "the " + vInfo.get(g.getEdgeTarget(path.getEdgeList().get(e))).name + " exhibit";
                    }
                    briefDirectionsLine = briefDirectionsLine + String.format("  %d. Proceed on %s %.0f feet towards %s\n",
                            i,
                            eInfo.get(path.getEdgeList().get(e).getId()).street,
                            g.getEdgeWeight(path.getEdgeList().get(e)) + accWeight,
                            name) + "\n";
                    tempcur = loc2;
                }
                else {
                    if(vInfo.get(g.getEdgeSource(path.getEdgeList().get(e))).kind.toString().equals("INTERSECTION")) {
                        name = vInfo.get(g.getEdgeSource(path.getEdgeList().get(e))).name;
                        name = name.replace(" / ", "");
                        name = name.replace(eInfo.get(path.getEdgeList().get(e).getId()).street, "");

                    }
                    else {
                        name = "the " + vInfo.get(g.getEdgeSource(path.getEdgeList().get(e))).name + " exhibit";
                    }

                    briefDirectionsLine = briefDirectionsLine + String.format("  %d. Proceed on %s %.0f feet towards %s\n",
                            i,
                            eInfo.get(path.getEdgeList().get(e).getId()).street,
                            g.getEdgeWeight(path.getEdgeList().get(e)) + accWeight,
                            name) + "\n";
                    tempcur = loc1;
                }
                accWeight = 0;
                i++;
            }
        }
    }
    public String getBriefDirections() {
        return briefDirectionsLine;
    }
    public String getDetailedDirections() {
        return detailedDirectionsLine;
    }
}
