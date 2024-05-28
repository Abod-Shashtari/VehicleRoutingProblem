package org.vrp.vehicleroutingproblem;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Draw {

    Canvas canvas;
    GraphicsContext gc;
    Draw(Canvas canvas,GraphicsContext gc){
        this.canvas=canvas;
        this.gc=gc;
    }
    public void clearCanvas(){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.GRAY);
        gc.strokeLine(0,0,canvas.getWidth(),0);
        gc.strokeLine(0,0,0,canvas.getHeight());
        gc.strokeLine(canvas.getWidth(),0,canvas.getWidth(),canvas.getHeight());
        gc.strokeLine(0,canvas.getHeight(),canvas.getWidth(),canvas.getHeight());
    }
    public void drawAll(Data data){
        clearCanvas();
        drawAllPaths(data);
        drawAllNodes(data);
    }
    public void drawNode(Node node) {
        if(node.station){
            gc.setFill(Color.BLACK);
        }else{
            gc.setFill(Color.GRAY);
        }
        gc.fillOval(node.xPos, node.yPos, 10, 10);
    }
    public void drawAllNodes(Data data){
        for (Node n :data.nodes){
            drawNode(n);
        }
    }
    public void drawPath(Truck t,Node n1,Node n2){
        gc.setStroke(t.color);
        double offset=5;
        gc.strokeLine(n1.xPos+offset,n1.yPos+offset,n2.xPos+offset,n2.yPos+offset);
    }
    public void drawAllPaths(Data data){
        for (Truck t:data.trucks){
            for (int n=0;n<t.nodes.size()-1;++n){
                drawPath(t,t.nodes.get(n),t.nodes.get(n+1));
            }
        }
    }
}
