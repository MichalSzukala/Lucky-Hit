package com.github.michalszukala.luckyhitfxml;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class SearchingForLinks {

    private List<Integer> numberOfLinksArray = new ArrayList<>();

    //Collecting all the links from web page and following the random link to the new place on the web
    public String start(String startingUrl){
        String luckyLink;
        List<String> oldArrayList;
        List<String> arrayList;
        numberOfLinksArray.clear();

        int count = 0;

        arrayList = linksOnPage("http://" + startingUrl);

        if(arrayList.size() == 0) {
            return null;
        }
        do {
            oldArrayList = new ArrayList<>(arrayList);

            int numberOfLinks = arrayList.size();
            if(numberOfLinks != 0)
                numberOfLinksArray.add(numberOfLinks);

            luckyLink = randomLink(arrayList);
            arrayList = linksOnPage(luckyLink);
            if(arrayList.size() == 0) {
                arrayList = oldArrayList;
            }
            count++;
        }while(count < 12);
        return luckyLink;
    }



    //it scraping the web page and returning ArrayList with all the possible links on the web page
    public List<String> linksOnPage(String startingUrl) {

        List<String> linksArray = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(startingUrl).ignoreContentType(true).userAgent("Mozilla").get();
            Elements linksOnPage = doc.select("a[href]");

            for (Element link : linksOnPage) {
                String validLink = link.attr("href");

                if (validLink.contains("http://") || validLink.contains("https://")) {
                    linksArray.add(link.attr("href"));
                }
            }

        } catch (IOException e) {}

        return linksArray;
    }

    // from the ArrayList of links is choosing one random link
    public String randomLink(List<String> linksArray){
        int randomNumber;
        String randomLink;
        Random randomGenerator = new Random();
        randomNumber = randomGenerator.nextInt(linksArray.size());
        randomLink = linksArray.get(randomNumber);
        return randomLink;
    }

    //from all the links will generate the chart
    public ScatterChart chart(){

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Number of the web page");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of links");

        ScatterChart scatterChart = new ScatterChart(xAxis, yAxis);

        XYChart.Series dataSeries = new XYChart.Series();
        dataSeries.setName("Number of links per visited web page");

        for(int i = 0; i < numberOfLinksArray.size(); i++){
            dataSeries.getData().add(new XYChart.Data( (i + 1), numberOfLinksArray.get(i)));
        }

        scatterChart.getData().add(dataSeries);
        return scatterChart;
    }
}

