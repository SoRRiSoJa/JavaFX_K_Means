/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jamdes.controller;

import com.jamdes.lsmkmeans.MainApp;
import com.jamdes.model.Kdata;
import com.jamdes.util.PoupUpUtil;
import com.jamdes.util.TextFieldFormatter;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author joao
 */
public class MenuFXMLController implements Initializable {

    @FXML
    private FontAwesomeIconView icOpenFile;
    @FXML
    private FontAwesomeIconView icStart;
    @FXML
    private FontAwesomeIconView icPlot;
    @FXML
    private FontAwesomeIconView icExit;
    @FXML
    private Pane paneBody;
    //----
    private FileChooser fileChooser;
    private JFXComboBox<Kdata> cbbCOne;
    private JFXComboBox<Kdata> cbbCtwo;
    @FXML
    private JFXTextField txtNKlusters;
    @FXML
    private JFXComboBox<Integer> cbbPx;
    @FXML
    private JFXComboBox<Integer> cbbPy;
    @FXML
    private StackPane rootSackPane;
    private String erros = "";
    private boolean flagStart = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.fileChooser = new FileChooser();
        setUpFileCoser();
    }

    @FXML
    private void onAbrir(MouseEvent event) {
        KMeansController.getInstance().setArquivoCsv(fileChooser.showOpenDialog(MainApp.janelaAberta));
        KMeansController.getInstance().lerArquivo();
    }

    @FXML
    private void onIniciar(MouseEvent event) {
        if (validateFile()) {
            loadPanelDados();
            loadCbb();
            flagStart = true;
        } else {
            PoupUpUtil.errorMessage(paneBody, rootSackPane, erros);
            erros = "";
        }
    }

    @FXML
    private void onPlotar(MouseEvent event) {
        if (flagStart) {
            if (validateAxis()) {
                loadPanelGrafico();
            } else {
                PoupUpUtil.errorMessage(paneBody, rootSackPane, erros);
                erros = "";
            }
        } else {
            PoupUpUtil.errorMessage(paneBody, rootSackPane, "Você deve processar os dados a serem plotados.");
        }
    }

    @FXML
    private void onSair(MouseEvent event) {
        System.exit(0);
    }

    private void setUpFileCoser() {
        fileChooser.setTitle("Selecionar características do Arquivo CSV");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv", "*.CSV"));
    }

    private void loadBox(String boxPath, String title) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(boxPath));
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            MainApp.janelaAnterior = MainApp.janelaAberta;
            MainApp.janelaAberta = stage;
            stage.show();
        } catch (IOException ex) {
            System.out.println("Erro---->" + ex);
        }
    }

    private void loadPanelDados() {
        Pane auxPane;
        try {
            auxPane = FXMLLoader.load(getClass().getResource("/fxml/tabelaDados/TabelaDadosFXML.fxml"));
            paneBody.getChildren().add(auxPane);
        } catch (IOException ex) {
            System.out.println("Erro ao abrir:" + ex);
        }

    }

    private void loadPanelGrafico() {
        Pane auxPane;
        try {
            auxPane = FXMLLoader.load(getClass().getResource("/fxml/graficoAgrupamento/GerenciarGraficos.fxml"));
            paneBody.getChildren().add(auxPane);
        } catch (IOException ex) {
            System.out.println("Erro ao abrir:" + ex);
        }
    }

    private void loadCbb() {
        List<Integer> aux = new ArrayList<>();
        for (int i = 0; i < KMeansController.getInstance().getLista().get(0).getCaracteristicas().size(); i++) {
            aux.add(i);
        }
        cbbPx.setItems(FXCollections.observableList(aux));
        cbbPy.setItems(FXCollections.observableList(aux));
    }

    @FXML
    private void onClassificar(ActionEvent event) {
        if (validateFields() && validateFile()) {
            getData();
            KMeansController.getInstance().KmeansCore();
            loadData();
        } else {
            PoupUpUtil.errorMessage(paneBody, rootSackPane, erros);
            erros = "";
        }

    }

    private boolean validateFields() {
        boolean flag = true;

        try {
            KMeansController.getInstance().setnKlusters(Integer.parseInt(txtNKlusters.getText()));
        } catch (NumberFormatException ex) {
            erros += " Informe um valor para o nº de classes para o agrupamento. \n";
            erros += " Utilize 3 Digitos Ex. [004]. \n";
            flag = false;
        }
        return flag;
    }

    private boolean validateFile() {
        boolean flag = true;
        if (KMeansController.getInstance().getArquivoCsv() == null) {
            erros += " Um arquivo de dados .CSV ainda não foi Carregado. \n";
            flag = false;
        }
        return flag;
    }

    private boolean validateAxis() {
        boolean flag = true;
        if (cbbPx.getSelectionModel().getSelectedItem() == null) {
            erros += " Selecione uma característica para o Eixo X do Gráfico. \n";
            flag = false;
        }
        if (cbbPy.getSelectionModel().getSelectedItem() == null) {
            erros += " Selecione uma característica para o Eixo Y do Gráfico. \n";
            flag = false;
        }
        return flag;
    }

    private void getData() {
        try {
            KMeansController.getInstance().setnKlusters(Integer.parseInt(txtNKlusters.getText()));
        } catch (NumberFormatException ex) {
            KMeansController.getInstance().setnKlusters(2);
        }

    }

    @FXML
    private void onPx(ActionEvent event) {
        if (cbbPx.getSelectionModel().getSelectedItem() != null) {
            KMeansController.getInstance().setpX(cbbPx.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void onPy(ActionEvent event) {
        if (cbbPy.getSelectionModel().getSelectedItem() != null) {
            KMeansController.getInstance().setpY(cbbPy.getSelectionModel().getSelectedItem());
        }
    }

    private void loadData() {
        erros += "Agrupamento concluído em [" + KMeansController.getInstance().getnIt() + " iterações] \n";
        erros += "Numero total de classes [" + KMeansController.getInstance().getVetClasses().length + "] \n";
        for (int i = 0; i < KMeansController.getInstance().getVetClasses().length; i++) {
            erros += "Classe Nº [" + i + "] - Total elementos: [" + KMeansController.getInstance().getVetClasses()[i].size() + "] \n";
        }
        PoupUpUtil.errorMessage(paneBody, rootSackPane, erros);
        erros = "";
    }

    @FXML
    private void onTrocar(ActionEvent event) {
    }

    @FXML
    private void onKeyReleasedKlusters(KeyEvent event) {
        TextFieldFormatter tff = new TextFieldFormatter();
        tff.setMask("###");
        tff.setCaracteresValidos("0123456789");
        tff.setTf(txtNKlusters);
        tff.formatter();
    }
}
