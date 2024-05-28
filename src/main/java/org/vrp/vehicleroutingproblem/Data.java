package org.vrp.vehicleroutingproblem;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Data {
    ArrayList<Node> nodes;
    ArrayList<Truck> trucks;
    Color []trucksColors={Color.RED,Color.BLUE,Color.ORANGE,Color.PURPLE,Color.GREEN,Color.TEAL,Color.PINK,Color.MAROON,Color.SALMON,Color.YELLOW};
    ArrayList<Node> freeNodes;
    public int currentNodeCapacitySum;
    public Data(){
        freeNodes=new ArrayList<>();
        nodes=new ArrayList<>();
        trucks=new ArrayList<>();
        currentNodeCapacitySum=0;
        for (int i=0;i<10;++i){
            trucks.add(new Truck(trucksColors[i]));
        }
    }
    public void addNode(Node node){
        nodes.add(node);
    }
    public int getNumberOfUsedTrucks(){
        int count=0;
        for(Truck t : trucks){
            if((t.nodes.get(1).capacity!=0))
                count++;
            else
                break;
        }
        return count;
    }
}
