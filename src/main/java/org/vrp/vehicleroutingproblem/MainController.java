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
    private TextField txtTemp;

    @FXML
    private Canvas canvas;
    boolean station=true;
    int numberOfClicks=1;

    GraphicsContext gc;
    Data data;
    Draw draw;
    SimulatedAnnealing simAnneal;
    boolean firstTime=true;

    @FXML
    public void initialize() {
        gc=canvas.getGraphicsContext2D();
        draw=new Draw(canvas,gc);
        draw.clearCanvas();
        data = new Data();
        simAnneal=new SimulatedAnnealing(data);
    }
    @FXML
    protected void runBtnJobs(){
        firstTime=true;
        numberOfClicks=1;
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

        /*
        textArea.setText("");
        for (Truck t : data.trucks){
                textArea.appendText(t.color+":\n");
            for (Node n:t.nodes){
                textArea.appendText(n.capacity+"\n");
            }
        }
         */
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
        String txtTempStr=txtTemp.getText();
        if(txtTempStr.isEmpty())
            showMSG("PLEASE FILL THE INITIAL TEMPERATURE FIELD");
        else {
            double initTemp=0.0;
            try {
                initTemp = Double.parseDouble(txtTemp.getText());
            }catch (Exception e){
                showMSG("PLEASE ENTER INTEGER VALUE.");
                txtTemp.setText("");
                return;
            }
            int numberOfIterPerButton= simAnneal.calcNumIter(initTemp);
            if(firstTime){
                simAnneal.run(initTemp,numberOfIterPerButton,firstTime);
                firstTime=false;
            }else{
                numberOfClicks++;
                numberOfIterPerButton*=numberOfClicks;
                simAnneal.run(initTemp,numberOfIterPerButton,firstTime);
            }
            System.out.println(numberOfIterPerButton);
            draw.drawAll(data);
            System.out.println("AFTER");
            System.out.println("===========");
            System.out.println(simAnneal.distanceAllNodes(data.trucks));
            System.out.println("===========");
        }
    }
    @FXML
    protected void resetBtn(){
        station=true;
        draw.clearCanvas();
        data.clearData();
        firstTime=true;
        numberOfClicks=1;
        //textArea.setText("");
        txtTemp.setText("");
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
            if(data.currentNodeCapacitySum+capacity>200){
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
