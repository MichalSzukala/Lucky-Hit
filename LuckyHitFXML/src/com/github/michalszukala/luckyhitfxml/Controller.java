package com.github.michalszukala.luckyhitfxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class Controller {

    private SearchingForLinks search = new SearchingForLinks();

    @FXML private Button buttonLuckyHit;
    @FXML private Button buttonChart;
    @FXML private Button buttonResult;
    @FXML private TextField textFieldUrl;
    private String luckyHit;


    //if data is valid it will show "lucky" link if data are not valid it will show error message
    @FXML
    public void showLuckyHit(ActionEvent event) {
        String startingPage = textFieldUrl.getText();
        startingPage = startingPage.trim();

        if (startingPage.isEmpty()) {
            buttonResult.setText("No URL, Try new one");
            buttonResult.setVisible(true);
        } else {
            luckyHit = search.start(startingPage);

            if (luckyHit != null) {
                buttonResult.setText(luckyHit);
                buttonResult.setVisible(true);
                buttonChart.setVisible(true);
            } else {
                buttonResult.setText("Wrong URL, Try new one");
                buttonResult.setVisible(true);
            }
       }
  }
    //will open "lucky" link in the browser window
    @FXML
    public void goToUrl(){
        if(luckyHit != null) {
            try {
                Desktop.getDesktop().browse(URI.create(luckyHit));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //will show chart with analysis of all the scanned links
    @FXML
    public void showChart(ActionEvent event){

        Stage chartStage = new Stage();
        chartStage.setTitle("Lucky-Hit Chart");
        ScatterChart scatterChart = search.chart();
        VBox vbox = new VBox(scatterChart);
        Scene scene = new Scene(vbox, 600, 300);
        chartStage.setScene(scene);
        chartStage.show();
    }

    //key event handler for finding "lucky" link
    @FXML
    public void pressEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

            String startingPage = textFieldUrl.getText();
            startingPage = startingPage.trim();

            if (startingPage.isEmpty()) {
                buttonResult.setText("No URL, Try new one");
                buttonResult.setVisible(true);
            } else {
                luckyHit = search.start(startingPage);

                if (luckyHit != null) {
                    buttonResult.setText(luckyHit);
                    buttonResult.setVisible(true);
                    buttonChart.setVisible(true);
                } else {
                    buttonResult.setText("Wrong URL, Try new one");
                    buttonResult.setVisible(true);
                }
            }
        }
    }
}
