package org.vrp.vehicleroutingproblem;

import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing {
    private final double initTemp;
    private final Data data;
    private final Random random;
    public int numberOfTrucks=0;
    public SimulatedAnnealing(double initTemp,Data data){
        this.initTemp=initTemp;
        this.data=data;
        random=new Random();
    }
    public void run(){
        ArrayList<Truck> curr=new ArrayList<>(data.trucks);
        ArrayList<Truck> best=new ArrayList<>();
        deepCopy(best,curr);
        ArrayList<Truck> next=new ArrayList<>();
        deepCopy(next,curr);
        double error=0;
        double Tc;
        int iter=1;
        while(true){
            Tc=calcTemp(iter,initTemp);
            System.out.println("TC:"+Tc);

            if(Tc<=2) break;
            if(iter>=10000) break;
            //find next
            randomSwap(next);
            error=distanceAllNodes(next)-distanceAllNodes(curr);
            System.out.println("curr:"+distanceAllNodes(curr));
            System.out.println("next:"+distanceAllNodes(next));
            if(error<0){
                deepCopy(curr,next);
                if(distanceAllNodes(best)>distanceAllNodes(curr)){
                    deepCopy(best,curr);
                }
            }else if(probabilityFormula(error,Tc)>random.nextInt(2)){
                //deepCopy(curr,next);
            }
            iter++;
        }
        deepCopy(data.trucks,best);
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
    public void deepCopy(ArrayList<Truck> a1,ArrayList<Truck> a2){
        a1.clear();
        for (Truck t: a2 ){
            a1.add(t.clone());
        }
    }
    public void randomSwap(ArrayList<Truck> trucks){
        //!TODO if performance was bad
        // Make it to take truck only if the truck has more than 3 nodes

        int x=random.nextInt(numberOfTrucks);
        Truck t=trucks.get(x);
        int n1=random.nextInt(t.nodes.size()-2)+1;
        int n2=random.nextInt(t.nodes.size()-2)+1;
        if((n2==n1)&& ((n2+1)!=(t.nodes.size()-1))) n2++;
        else if((n2==n1)&& ((n2-1)!=(0))) n2--;
        System.out.println("n1:"+n1);
        System.out.println("n2:"+n2);
        swap(t,n1,n2);
    }
    public void swap(Truck t, int n1, int n2){
        if(n1!=-1 && n2!=-1 ){
            Node tmp =t.nodes.get(n1);
            t.nodes.set(n1,t.nodes.get(n2));
            t.nodes.set(n2,tmp);
        }
    }
}
