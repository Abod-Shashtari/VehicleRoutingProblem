package org.vrp.vehicleroutingproblem;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Truck implements Cloneable{
    Color color;
    int capacity;
    ArrayList<Node> nodes;
    public Truck(Color color){
        nodes=new ArrayList<>();
        capacity=20;
        this.color=color;
    }
    public void reduceCapacity(int x){
        this.capacity-=x;
    }
    public void reset(){
        nodes.clear();
        capacity=20;
    }
    @Override
    public Truck clone() {
        try {
            // Perform a shallow copy using super.clone()
            Truck clonedTruck = (Truck) super.clone();

            // Deep clone the ArrayList<Node> nodes
            clonedTruck.nodes = new ArrayList<>();
            for (Node originalNode : this.nodes) {
                // Assuming Node also implements Cloneable
                clonedTruck.nodes.add(originalNode.clone());
            }

            return clonedTruck;
        } catch (CloneNotSupportedException e) {
            // Handle the exception (e.g., log or throw a custom exception)
            throw new Error("Cloning failed", e);
        }
    }
}
