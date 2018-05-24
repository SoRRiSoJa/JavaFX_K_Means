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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author joao
 */
public class GraficoAgrupamentosFXMLController implements Initializable {

    @FXML
    private Pane paneRoot;
    @FXML
    private Pane paneBody;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;
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
        loadGraficos();
    }

    private void loadGraficos() {
        List<Kdata>[] vetAux = KMeansController.getInstance().getVetClasses();
        Kdata[] klusters = KMeansController.getInstance().getKlusters();
        for (int i = 0; i < vetAux.length; i++) {
            vBox.getChildren().add(graficoAgrupamento(vetAux[i], klusters[i], i));
        }
    }

    private Pane graficoAgrupamento(List<Kdata> listaClasse, Kdata centroides, int index) {
        int pX = KMeansController.getInstance().getpX();
        int pY = KMeansController.getInstance().getpY();
        int nCar = centroides.getCaracteristicasD().length;
        graphRange(pX, pY, listaClasse);
        System.out.println("PX:" + pX + " PY:" + pY + " NK:" + nCar + " ");

        final NumberAxis xAxis = new NumberAxis(g_minX, g_MaxX, 0.5);
        final NumberAxis yAxis = new NumberAxis(g_minY, g_MaxY, 0.5);
        xAxis.setLabel("" + pX);
        yAxis.setLabel("" + pY);
        ScatterChart<Number, Number> sc = new ScatterChart<>(xAxis, yAxis);
        sc.setPrefSize(1046, 546);
        sc.setTitle("Agrupamento K-Means - Classe Nº[" + index + "]");
        XYChart.Series series[] = new XYChart.Series[2];
        //Crinado uma Série para os centroides
        XYChart.Series sCentroide = new XYChart.Series();
        sCentroide.setName("Centroide");
        //Adicionando centroide ao grafico
        series[1] = sCentroide;

        series[1].getData().add(new XYChart.Data(centroides.getCaracteristicasD()[pX].floatValue(), centroides.getCaracteristicasD()[pY].floatValue()));

        //Adicionando classes ao grafico
        series[0] = new XYChart.Series();
        series[0].setName("C [" + index + "]");
        listaClasse.forEach((kd) -> {
            series[0].getData().add(new XYChart.Data(kd.getCaracteristicasD()[pX].floatValue(), kd.getCaracteristicasD()[pY].floatValue()));
        });
        sc.getData().addAll(series[0]);

        sc.getData().addAll(series[1]);
        sc.setAnimated(true);

        Pane p = new Pane();
        p.setMaxSize(1046, 548);
        p.setMinSize(1046, 548);
        p.setStyle("-fx-border-style: solid");
        p.setStyle("-fx-background-color:#ffffff");
        p.getChildren().add(sc);
        return p;
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
        System.out.println("" + g_MaxX + "|" + g_MaxY + "|" + g_minY + "|" + g_MaxY);
    }

}
