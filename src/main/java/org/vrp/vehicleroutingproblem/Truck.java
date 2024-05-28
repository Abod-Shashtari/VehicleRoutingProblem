package org.vrp.vehicleroutingproblem;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Truck {
    Color color;
    int capacity;
    ArrayList<Node> nodes;
    public Truck(Color color){
        nodes=new ArrayList<>();
        capacity=10;
        this.color=color;
    }
    public void reduceCapacity(int x){
        this.capacity-=x;
    }
    public void reset(){
        nodes.clear();
        capacity=10;
    }
}
