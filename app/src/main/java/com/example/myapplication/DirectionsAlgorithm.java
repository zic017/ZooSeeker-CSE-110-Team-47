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
    public Context context;
    Graph<String, IdentifiedWeightedEdge> g;

    public DirectionsAlgorithm(ArrayList<String> plannedIds, Context context){
        this.plannedIds = plannedIds;
        this.context = context;
        //Always start at the entrance gate
        current = "entrance_exit_gate";
        g = ZooData.loadZooGraphJSON(context,"sample_zoo_graph.json");
    }

    public void getNext() {
        String next;

        //If no more exhibits, return to gate
        if(plannedIds.size() == 0) {
            getDirections(current, "entrance_exit_gate");
            current = "entrance_exit_gate";
        }
        else {
            next = closest();
            getDirections(current, next);
            current = next;
            plannedIds.remove(current);
        }
    }

    //get the closest exhibit to the current location
    public String closest() {
        int minDistance = 10000;
        int curTotal;
        String close = current;

        //For each other exhibit planned besides the current one
        for(String id : plannedIds) {
            curTotal = 0;
            // 1. Load the graph...

            GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, current, id);
            // 2. Load the information about our nodes and edges...
            Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(context, "sample_node_info.json");
            Map<String, ZooData.EdgeInfo> eInfo = ZooData.loadEdgeInfoJSON(context, "sample_edge_info.json");

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

    public void getDirections(String current, String next) {
        // "source" and "sink" are graph terms for the start and end

        // 1. Load the graph...
        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, current, next);

        // 2. Load the information about our nodes and edges...
        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(context,"sample_node_info.json");
        Map<String, ZooData.EdgeInfo> eInfo = ZooData.loadEdgeInfoJSON(context, "sample_edge_info.json");

        System.out.printf("The shortest path from '%s' to '%s' is:\n", current, next);

        int i = 1;
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            System.out.printf("  %d. Walk %.0f meters along %s from '%s' to '%s'.\n",
                    i,
                    g.getEdgeWeight(e),
                    eInfo.get(e.getId()).street,
                    vInfo.get(g.getEdgeSource(e).toString()).name,
                    vInfo.get(g.getEdgeTarget(e).toString()).name);
            i++;
        }
    }










}
