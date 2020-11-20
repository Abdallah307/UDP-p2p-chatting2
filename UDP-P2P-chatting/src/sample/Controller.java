package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Controller implements Initializable {


    private Channel channel;
    private ArrayList<Peer> peersList = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxes = new ArrayList<>();
    FileChooser fileChooser = new FileChooser();

    Alert alert = new Alert(Alert.AlertType.ERROR);

    BufferedReader br;

    @FXML
    TextField username;
    @FXML
    TextField userIP;
    @FXML
    TextField portNumber;
    @FXML
    Button connect;
    @FXML
    Button send;
    @FXML
    TextArea messageBox;
    @FXML
    TextArea board;
    @FXML
    Button stop;
    @FXML
    Button addPeerButton;
    @FXML
    TextField peerName;
    @FXML
    TextField peerPort;
    @FXML
    TextField peerIP;
    @FXML
    VBox peersBox;
    @FXML
    Button chooseButton;
    @FXML
    Label fileName;
    @FXML
    Button sendFile;
    @FXML
    AnchorPane chattingApplicationAnchor;
    @FXML
    AnchorPane peersAnchor;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        board.setStyle("-fx-font-size: 20px");
        board.setEditable(false);
        send.setDisable(true);
        peersBox.setSpacing(15);
        fileChooser.setTitle("Choose text file to send");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Text Files", "*.txt")
                );






    }

    public void setInformations(ActionEvent event) throws Exception {


        if(username.getText().isEmpty()) {
            alert.setContentText("Please Fill The username");
            alert.setHeaderText("ERROR");
            alert.show();
        }
        else if(userIP.getText().isEmpty()) {
            alert.setContentText("Please Fill The IP");
            alert.setHeaderText("ERROR");
            alert.show();
        }
        else if(portNumber.getText().isEmpty()) {
            alert.setContentText("Please Fill The port number");
            alert.setHeaderText("ERROR");
            alert.show();
        }
        else {
            try {
                connect.setDisable(false);

                String name = this.username.getText();

                int sourcePort = Integer.parseInt(this.portNumber.getText());

                String sourceIP = userIP.getText();

                channel = new Channel();
                channel.bind(sourcePort, board);
                channel.start();//start receive
                stop.setDisable(false);
                send.setDisable(false);

            }
            catch (NumberFormatException numberFormatException){
                alert.setContentText("The port number cant be a string enter a valid number");
                alert.setHeaderText("Wrong Format");
                alert.show();
            }
        }
    }



    public void sendMessage(ActionEvent event) throws IOException {

        boolean CheckBoxSelected = false;
        String msg = this.messageBox.getText();
        if(msg.isEmpty()) {
            alert.setHeaderText("Empty Message");
            alert.setContentText("The Message Box is Empty");
            alert.show();
        }

        for(CheckBox boxi : checkBoxes) {
            if(boxi.isSelected()) {
                CheckBoxSelected = true;
                break;
            }
        }

        if(!CheckBoxSelected) {
            alert.setContentText("No peer");
            alert.setContentText("Please choose a peer");
            alert.showAndWait();
        }

        else{
            int counter = 0;
            for(CheckBox boxi : checkBoxes) {
                if(boxi.isSelected()) {
                    msg = username.getText() + " >> " + msg;
                    board.appendText("\n"+msg+"\n");
                    InetSocketAddress newAddress = new InetSocketAddress(peersList.get(counter).getIpNumber() , peersList.get(counter).getPortNumber());
                    channel.sendTo(newAddress , msg , board);
                    messageBox.setText("");

                }
                counter++;
            }

        }



    }

    public void addPeer(ActionEvent event) {
        boolean existed = false;
        if(peerName.getText().isEmpty()) {
            alert.setContentText("Please Fill The peer name");
            alert.setHeaderText("ERROR");
            alert.show();
        }
        else if(peerPort.getText().isEmpty()) {
            alert.setContentText("Please Fill The peer port number");
            alert.setHeaderText("ERROR");
            alert.show();
        }

        else if(peerIP.getText().isEmpty()){
            alert.setContentText("Please Fill The peer ip number");
            alert.setHeaderText("ERROR");
            alert.show();
        }
        else {
            for(Peer p : peersList) {
                if(p.getPortNumber() == Integer.parseInt(peerPort.getText())) {
                    existed = true;
                }
            }
            try {
                if(!existed) {
                    Peer newPeer = new Peer();
                    newPeer.setPeerName(peerName.getText());
                    newPeer.setIpNumber(peerIP.getText());
                    newPeer.setPortNumber(Integer.parseInt(peerPort.getText()));
                    peersList.add(newPeer);
                    CheckBox newCheckBox = new CheckBox(newPeer.getPeerName());
                    newCheckBox.setFont(new Font("Arial", 23));
                    newCheckBox.setTextFill(Color.WHITE);
                    peersBox.getChildren().add(newCheckBox);
                    checkBoxes.add(newCheckBox);
                }
                else {
                    alert.setContentText("the Peer is already Existed!");
                    alert.setHeaderText("ERROR");
                    alert.show();
                }

            }
            catch (NumberFormatException numberFormatException){
                alert.setContentText("The port number cant be a string enter a valid number");
                alert.setHeaderText("Wrong Format");
                alert.show();
            }
        }
    }

    public void chooseFile(ActionEvent event) {
        File selectedFile = fileChooser.showOpenDialog(null);
        fileName.setText(selectedFile.getName());

        if (selectedFile != null) {

            String filePath = selectedFile.getPath();
            try {
                br = new BufferedReader(new FileReader(filePath));
            }
            catch (IOException ioe) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ERROR");
                alert.setHeaderText("Opps.. There is a problem");
                alert.setContentText(ioe.getMessage());
                alert.show();
            }
        }

    }

    public void sendTxtFile(ActionEvent event) throws IOException {
        String line = "";
        String text = "";
        while ((line = br.readLine()) != null) {
            text += line + "\n";
        }

        boolean CheckBoxSelected = false;
        String msg = text;
        if(msg.isEmpty()) {
            alert.setHeaderText("Empty Message");
            alert.setContentText("The Message Box is Empty");
            alert.show();
        }

        for(CheckBox boxi : checkBoxes) {
            if(boxi.isSelected()) {
                CheckBoxSelected = true;
                break;
            }
        }

        if(!CheckBoxSelected) {
            alert.setContentText("No peer");
            alert.setContentText("Please choose a peer");
            alert.showAndWait();
        }

        else{
            int counter = 0;
            for(CheckBox boxi : checkBoxes) {
                if(boxi.isSelected()) {
                    msg = username.getText() + " >> " + msg;
                    board.appendText("\n"+msg+"\n");
                    InetSocketAddress newAddress = new InetSocketAddress(peersList.get(counter).getIpNumber() , peersList.get(counter).getPortNumber());
                    channel.sendTo(newAddress , msg , board);
                    messageBox.setText("");

                }
                counter++;
            }

        }

        br.close();
    }




    public void stopConnection(ActionEvent event) {
        channel.stop();
        System.out.println("Closed");
        stop.setDisable(true);
        send.setDisable(true);

    }
}
