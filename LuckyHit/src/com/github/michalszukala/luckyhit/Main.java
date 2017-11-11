package com.github.michalszukala.luckyhit;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.net.URI;



public class Main extends Application {

    private SearchingForLinks search = new SearchingForLinks();

    public static void main(String[] args) {
        launch(args);
    }

    //setting up stage, layout and all the elements on the layout
    @Override
    public void start(Stage primaryStage) throws Exception{

        GridPane layout = layout();
        TextField startingUrl = startingUrl();
        Button buttonLuckyHit = buttonLuckyHit(startingUrl, layout);

        layout.getChildren().addAll(startingUrl, buttonLuckyHit);
        primaryStage.setTitle("Lucky Hit");
        Scene scene = new Scene(layout, 500, 300);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    //creating layout
    public GridPane layout(){
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        layout.setHgap(20);
        layout.setVgap(20);
        layout.setPadding(new Insets(25, 25, 25, 25));
        layout.setStyle("-fx-background-color: #383838");
        return layout;
    }

    //creating TextField for starting Url
    public TextField startingUrl(){
        TextField startingUrl = new TextField();
        startingUrl.setPrefWidth(380);
        startingUrl.setPromptText("Type your starting page, and wait......");
        startingUrl.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); -fx-font-size: 18");
        return startingUrl;
    }

    //creating button that display the "lucky" link
    public Button buttonResult(String message){
        Button button = new Button(message);
        button.setStyle("-fx-font-size: 11; -fx-font-weight: bold");
        GridPane.setConstraints(button,0,2);
        button.setPrefSize(380, 35);
        return button;
    }

    //creating button that display a chart for all the searched links
    public Button buttonChart(){
        Button button = new Button("Chart Analysis");
        GridPane.setConstraints(button,1,2);
        button.setPrefSize(100, 25);
        button.setStyle("-fx-font-size: 11; -fx-font-weight: bold");
        button.setOnAction(e->{

            Stage chartStage = new Stage();
            chartStage.setTitle("Lucky-Hit Chart");
            Scene sceneChart = chartScene();
            chartStage.setScene(sceneChart);
            chartStage.show();
        });
        return button;
    }

    //creating button that will access SearchForLinks starting method and will search for "lucky" link
    public Button buttonLuckyHit( TextField startingUrl, GridPane layout){

        Button button = new Button("Lucky-Hit");
        GridPane.setConstraints(button,1,0);
        button.setMinWidth(100);
        button.setStyle("-fx-font-size: 14; -fx-font-weight: bold");

        button.setOnAction(e -> {

            String startingPage = startingUrl.getText();
            startingPage = startingPage.trim();
            if(startingPage.isEmpty()){
                Button buttonResult = buttonResult("No URL, Try new one");
                layout.getChildren().add(buttonResult);
            }
            else {
                String luckyHit = search.start(startingPage);

                if (luckyHit != null) {
                    Button buttonResult = buttonResult(luckyHit);
                    Button buttonChart = buttonChart();
                    layout.getChildren().add(buttonResult);
                    layout.getChildren().add(buttonChart);

                    buttonResult.setOnAction(e1->{
                        try {
                            Desktop.getDesktop().browse(URI.create(luckyHit));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                } else {
                    Button buttonResult = buttonResult("Wrong URL, Try new one");
                    layout.getChildren().add(buttonResult);
                }
            }
        });
        return button;
    }
    //creating new scene with the chart on it
    public Scene chartScene (){
        ScatterChart scatterChart = search.chart();
                VBox vbox = new VBox(scatterChart);

        Scene scene = new Scene(vbox, 500, 300);
        return scene;
    }
}
