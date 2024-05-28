package org.vrp.vehicleroutingproblem;

public class Node implements Cloneable{
    double xPos;
    double yPos;
    int capacity;
    boolean station;
    public Node(){}
    public Node(double xPos,double yPos,int capacity,boolean station){
        this.xPos=xPos;
        this.yPos=yPos;
        this.capacity=capacity;
        this.station=station;
    }
    @Override
    public Node clone() {
        try {
            // Perform a shallow copy using super.clone()
            return (Node) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error("Cloning failed", e);
        }
    }

}
