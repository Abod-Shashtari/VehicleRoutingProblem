package org.vrp.vehicleroutingproblem;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;

public class MainController {
    @FXML
    private Button btnJobs;

    @FXML
    private Button btnPath;

    @FXML
    private Canvas canvas;
    @FXML
    private TextArea textArea;
    boolean station=true;

    GraphicsContext gc;
    Data data;
    Draw draw;
    SimulatedAnnealing simAnneal;

    @FXML
    public void initialize() {
        textArea.setEditable(false);
        gc=canvas.getGraphicsContext2D();
        draw=new Draw(canvas,gc);
        draw.clearCanvas();
        data = new Data();
        simAnneal=new SimulatedAnnealing(2000,data);
    }
    @FXML
    protected void runBtnJobs(){
        for (Truck t: data.trucks){
            t.reset();
        }

        //copy all nodes to freeNodes list
        data.freeNodes.addAll(data.nodes);
        data.freeNodes.removeFirst();
        for (Truck t : data.trucks){
            getWork(t);
            t.nodes.addFirst(data.nodes.getFirst());
            t.nodes.addLast(data.nodes.getFirst());
        }

        textArea.setText("");
        for (Truck t : data.trucks){
                textArea.appendText(t.color+":\n");
            for (Node n:t.nodes){
                textArea.appendText(n.capacity+"\n");
            }
        }
        simAnneal.numberOfTrucks=data.getNumberOfUsedTrucks();
        draw.drawAll(data);
        System.out.println("BEFORE");
        System.out.println("===========");
        System.out.println(simAnneal.distanceAllNodes(data.trucks));
        System.out.println("===========");
    }
    @FXML
    protected void runBtnPath(){
        System.out.println("test");
        simAnneal.run();
        draw.drawAll(data);
        System.out.println("AFTER");
        System.out.println("===========");
        System.out.println(simAnneal.distanceAllNodes(data.trucks));
        System.out.println("===========");
    }
    @FXML
    protected void test(){

    }


    @FXML
    protected void addNode(MouseEvent event){
        double mouseX=event.getX();
        double mouseY=event.getY();
        if(station) {
            Node node = new Node(mouseX, mouseY, 0,true);
            data.addNode(node);
            station=false;
            draw.drawNode(node);
        }else{
            int capacity=readCapacity();
            if(capacity==-1) return;
            if(data.currentNodeCapacitySum+capacity>100){
                showMSG("Capacity Overflow!");
            }else {
                Node node = new Node(mouseX, mouseY, capacity,false);
                data.addNode(node);
                draw.drawNode(node);
            }
        }
    }

    private void getWork(Truck t){
        Collections.shuffle(data.freeNodes);
        ArrayList<Node> removeList=new ArrayList<>();
        for (Node node : data.freeNodes){
            if(t.capacity==0){
                break;
            }
            if(node.capacity<=t.capacity){
                t.reduceCapacity(node.capacity);
                t.nodes.add(node);
                removeList.add(node);
            }
        }
        data.freeNodes.removeAll(removeList);
    }
    private int readCapacity(){
        Dialog<Integer> input =new Dialog<>();
        input.setTitle("INPUT");
        input.setHeaderText("NODE CAPACITY");
        Slider slider=new Slider();
        slider.setMax(10);
        slider.setMin(1);
        slider.setValue(0);
        Label label = new Label((int)slider.getValue()+"");
        slider.valueProperty().addListener((observable, oldValue, newValue) -> label.setText(newValue.intValue()+""));
        input.getDialogPane().setContent(new VBox(label,slider));
        input.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        input.setResultConverter(buttonType -> {
            if(buttonType==ButtonType.OK){
               return (int)slider.getValue();
            }else{
                return -1;
            }
        });
        return input.showAndWait().orElse(-1);
    }
    private void showMSG(String str){
        Dialog<String> dialog=new Dialog<>();
        dialog.setHeaderText(str);
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.show();
    }
}
