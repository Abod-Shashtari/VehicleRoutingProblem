package org.vrp.vehicleroutingproblem;

public class Node {
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

}
