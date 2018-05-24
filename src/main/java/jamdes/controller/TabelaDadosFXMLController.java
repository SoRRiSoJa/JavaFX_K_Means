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
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author joao
 */
public class TabelaDadosFXMLController implements Initializable {

    @FXML
    private Pane pane;
    @FXML
    private TextArea txtDados;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadData();
    }

    private void loadData() {
        List<Kdata> lista = KMeansController.getInstance().getLista();
        String linha = "";
        for (Kdata matAux1 : lista) {
            for (Double caracteristicasD : matAux1.getCaracteristicasD()) {
                linha += String.format("| %.4f |", caracteristicasD);
            }
            linha += "\n";
        }
        txtDados.setText(linha);
    }

}
