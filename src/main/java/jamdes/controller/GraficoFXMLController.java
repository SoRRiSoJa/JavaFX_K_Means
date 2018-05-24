/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jamdes.controller;

import com.jamdes.model.Kdata;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author joao
 */
public class GraficoFXMLController implements Initializable {

    @FXML
    private Pane paneRoot;
    @FXML
    private Pane paneBody;

    //----------
    float g_minX;
    float g_MaxX;
    float g_minY;
    float g_MaxY;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        grafico(KMeansController.getInstance().getLista(), KMeansController.getInstance().getKlusters());
    }

    public void grafico(List<Kdata> lista, Kdata[] centroides) {
        List<Kdata>[] kla = KMeansController.getInstance().getVetClasses();
        int pX = KMeansController.getInstance().getpX();
        int pY = KMeansController.getInstance().getpY();
        int nK = centroides.length;
        int nCar = centroides[0].getCaracteristicasD().length;
        graphRange(pX, pY, lista);
        System.out.println("PX:" + pX + " PY:" + pY + " NK:" + nCar + " ");

        final NumberAxis xAxis = new NumberAxis(g_minX, g_MaxX, 0.5);
        final NumberAxis yAxis = new NumberAxis(g_minY, g_MaxY, 0.5);
        xAxis.setLabel("" + pX);
        yAxis.setLabel("" + pY);
        ScatterChart<Number, Number> sc = new ScatterChart<>(xAxis, yAxis);
        sc.setPrefSize(1048, 548);
        sc.setTitle("Agrupamento K-Means");
        XYChart.Series series[] = new XYChart.Series[nK + 1];
        //Crinado uma Série para os centroides
        XYChart.Series sCentroide = new XYChart.Series();
        sCentroide.setName("Centroide");
        //Adicionando centroides ao grafico
        series[nK] = sCentroide;
        for (int i = 0; i < nK; i++) {
            series[nK].getData().add(new XYChart.Data(centroides[i].getCaracteristicasD()[pX].floatValue(), centroides[i].getCaracteristicasD()[pY].floatValue()));

        }
        //Adicionando classes ao grafico

        for (int i = 0; i < nK; i++) {
            series[i] = new XYChart.Series();
            series[i].setName("C [" + i + "]");
            for (Kdata kd : kla[i]) {
                series[i].getData().add(new XYChart.Data(kd.getCaracteristicasD()[pX].floatValue(), kd.getCaracteristicasD()[pY].floatValue()));
            }
            sc.getData().addAll(series[i]);
        }

        sc.getData().addAll(series[nK]);
        sc.setAnimated(true);

        /*
                
        
         */
        paneBody.getChildren().add(sc);
    }

    public void graphRange(int x, int y, List<Kdata> lista) {
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;

        float bx, by;
        for (int i = 0; i < lista.size() - 1; i++) {
            bx = lista.get(i).getCaracteristicasD()[x].floatValue();
            by = lista.get(i).getCaracteristicasD()[y].floatValue();

            if (by > maxY) {
                maxY = by;
            }
            if (bx > maxX) {
                maxX = bx;
            }

            if (bx < minX) {
                minX = bx;
            }
            if (by < minY) {
                minY = by;
            }
        }

        g_minX = minX;
        g_MaxX = maxX;
        g_minY = minY;
        g_MaxY = maxY;
        /*         
        g_minX = 0.1f;
        g_MaxX = 40;
        g_minY = 0.1f;
        g_MaxY = 40;
         */
        System.out.println("Aqui:--------->>>>" + g_MaxX + "|" + g_MaxY + "|" + g_minY + "|" + g_MaxY);
    }
}
