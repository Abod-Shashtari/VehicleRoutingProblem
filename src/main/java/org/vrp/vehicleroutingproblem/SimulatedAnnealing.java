package org.vrp.vehicleroutingproblem;

import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing {
    private double initTemp;
    private Data data;
    private Random random;
    private double startingTemp=4000;
    public SimulatedAnnealing(double initTemp,Data data){
        this.initTemp=initTemp;
        this.data=data;
        random=new Random();
    }
    public void run(){
        ArrayList<Truck> curr=new ArrayList<>(data.trucks);
        ArrayList<Truck> best=new ArrayList<>(curr);
        ArrayList<Truck> next=new ArrayList<>(curr);
        double error=0;
        double Tc;
        int iter=0;
        while(true){
            Tc=calcTemp(iter,startingTemp);
            if(Tc<2) break;
            //next
            randomSwap(next);
            error=distanceAllNodes(next)-distanceAllNodes(curr);
            if(error>0){
                curr.clear();
                curr.addAll(next);
                if(distanceAllNodes(best)<distanceAllNodes(curr)){
                    best.clear();
                    best.addAll(curr);
                }
            }else if(probabilityFormula(error,Tc)>random.nextInt(2)){
                curr.clear();
                curr.addAll(next);
            }
            iter++;
        }
    }
    private double calcTemp(int i,double T){
        return (T/Math.log(i));
    }
    private double probabilityFormula(double error,double Tc){
        return Math.exp((-error)/(Tc));
    }
    private double distance(Node n1, Node n2){
        return (Math.sqrt(Math.pow((n1.xPos-n2.xPos),2)+Math.pow((n1.yPos-n2.yPos),2)));
    }
    public double distanceAllNodes(ArrayList<Truck> trucks){
        double sum=0;
        for(Truck t : trucks){
            for (int i=0;i<t.nodes.size()-1;++i){
                sum+=distance(t.nodes.get(i),t.nodes.get(i+1));
            }
        }
        return sum;
    }
    public void randomSwap(ArrayList<Truck> trucks){
        //!TODO if performance was bad
        // Make it to take truck only if the truck has more than 3 nodes

        int x=random.nextInt(data.getNumberOfUsedTrucks());
        Truck t=trucks.get(x);
        int n1=random.nextInt(t.nodes.size()-1)+1;
        int n2=random.nextInt(t.nodes.size()-1)+1;
        if((n2==n1)&& ((n2+1)!=(t.nodes.size()-1))) n2++;
        else if((n2==n1)&& ((n2-1)!=(0))) n2--;
        swap(t,n1,n2);
    }
    public void swap(Truck t, int n1, int n2){
        if(n1!=-1 && n2!=-1){
            Node tmp =t.nodes.get(n1);
            t.nodes.set(n1,t.nodes.get(n2));
            t.nodes.set(n2,tmp);
        }
    }
}
