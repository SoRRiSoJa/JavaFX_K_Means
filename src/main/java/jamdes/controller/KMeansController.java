/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jamdes.controller;

import com.jamdes.model.Kdata;
import com.jamdes.util.ReaderCSV;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author joao
 */
public class KMeansController {

    public static KMeansController kMeansController = null;
    private File arquivoCsv; // arquivo csv
    private ObservableList<Kdata> lista; // Lista de dados inicial
    private ReaderCSV rCsv; // Classe utilitária para leitura do arquivo
    private int nKlusters; // Numero total de Clusters
    private Kdata[] klusters; //Vetor de clusters
    private Double[][] distancias;//Matriz de distâncias
    private List<Kdata>[] vetClasses;//Vetor de classes
    private int pX; //Caracteristica para Eixo X do grafico
    private int pY;//Caracteristica para Eixo Y do grafico
    private int nIt;

    private KMeansController() {

    }

    public List<Kdata>[] getVetClasses() {
        return vetClasses;
    }

    public static synchronized KMeansController getInstance() {
        if (kMeansController == null) {
            kMeansController = new KMeansController();
        }
        return kMeansController;
    }

    public int getnIt() {
        return nIt;
    }

    public void setnIt(int nIt) {
        this.nIt = nIt;
    }

    public int getpX() {
        return pX;
    }

    public void setpX(int pX) {
        this.pX = pX;
    }

    public int getpY() {
        return pY;
    }

    public void setpY(int pY) {
        this.pY = pY;
    }

    public Kdata[] getKlusters() {
        return klusters;
    }

    public int getnKlusters() {
        return nKlusters;
    }

    public void setnKlusters(int nKlusters) {
        this.nKlusters = nKlusters;
    }

    public File getArquivoCsv() {
        return arquivoCsv;
    }

    public void setArquivoCsv(File arquivoCsv) {
        this.arquivoCsv = arquivoCsv;
    }

    public ObservableList<Kdata> getLista() {
        return lista;
    }

    public void setLista(ObservableList<Kdata> lista) {
        this.lista = lista;
    }

    public void lerArquivo() {
        try {
            this.rCsv = new ReaderCSV(arquivoCsv.getPath());
            this.setLista(FXCollections.observableList(rCsv.readFile()));
        } catch (Exception ex) {
            System.out.println("Erro ao abri arquivo:" + ex);
        }

    }

    /**
     * Calcula a distancia euclidiana para dui pontos da matriz public double
     * private double distanciaEuclidiana(Double[] , Double[])
     *
     * @param pA - ponto A
     * @param pB - ponto B
     * @return double - Distancia euclidiana entre A e B
     */
    private double distanciaEuclidiana(Double[] pA, Double[] pB) {
        double aux, soma = 0;
        int len = pA.length, i;
        for (i = 0; i < len; i++) {
            aux = pA[i] - pB[i];
            aux *= aux;
            soma += aux;
        }
        return Math.sqrt(soma);
    }

    private void inicializarKlusters() {
        klusters = new Kdata[nKlusters];
        for (int i = 0; i < nKlusters; i++) {
            klusters[i] = new Kdata(i, lista.get(i).getCaracteristicas(), "Kluster - " + i);
        }
    }

    private void inicializaClasses() {
        vetClasses = new LinkedList[nKlusters];
        for (int i = 0; i < nKlusters; i++) {
            vetClasses[i] = new LinkedList<>();
        }
    }

    /**
     * Calcula a matriz das distancias euclidianas entre os pontos da matriz e
     * os Klusters E os Klusters do vetor de Klusters public void private void
     * calcDistancias()
     */
    private void calcDistancias() {
        int len = lista.size(), i, j;
        distancias = new Double[len][nKlusters];
        for (i = 0; i < len; i++) {
            for (j = 0; j < nKlusters; j++) {
                distancias[i][j] = distanciaEuclidiana(lista.get(i).getCaracteristicasD(), klusters[j].getCaracteristicasD());
            }
        }
    }

    public void showDistances() {
        String linha = "";
        for (Double[] matAux1 : distancias) {
            for (int j = 0; j < distancias[0].length; j++) {
                linha += String.format("| %.4f |", matAux1[j]);
            }
            linha += "\n";
        }
        System.out.println(linha);
    }

    /**
     * Retorna um cluster médio a partir de uma lista de pontos public Kdata
     * clusterMedio(List<Kdata>)
     *
     * @param lista - lista de pontos
     * @return Kdata - Novo Cluster medio
     */
    private Kdata clusterMedio(List<Kdata> lista) {
        int nCaracteristicas = lista.get(0).getCaracteristicas().size(), nElementos = lista.size();
        Double[] media = new Double[nCaracteristicas];
        //Zerando vetor de medias
        for (int i = 0; i < nCaracteristicas; i++) {
            media[i] = 0.0;
        }
        //acumulando valores das caracteristicas em suas colunas
        lista.forEach((Kdata data) -> {
            for (int i = 0; i < nCaracteristicas; i++) {
                media[i] += data.getCaracteristicasD()[i];
            }
        });
        //calculando a media dos valores acumulados
        for (int i = 0; i < nCaracteristicas; i++) {
            media[i] /= nElementos;
        }
        //construindo um novo cluster medio
        return new Kdata(0l, Arrays.asList(media), "Kluster Medio");
    }

    private void showKlusters() {
        for (Kdata d : klusters) {
            System.out.println(d + "|" + d.getCaracteristicas().toString());
        }
    }

    private boolean atualizarValorCentroides() {
        boolean flag = true;
        Kdata[] auxK;
        auxK = Arrays.copyOf(klusters, nKlusters);
        for (int i = 0; i < nKlusters; i++) {
            klusters[i] = clusterMedio(vetClasses[i]);
            flag = compararPontos(klusters[i], auxK[i]);
        }
        return !flag;
    }

    private boolean compararPontos(Kdata pUm, Kdata pDois) {
        boolean flag = true;
        int cont = 0;
        int len = pUm.getCaracteristicasD().length;
        while (flag && cont < len) {
            flag = pUm.getCaracteristicasD()[cont].equals(pDois.getCaracteristicasD()[cont]);
            cont++;
        }
        return flag;
    }

    public void KmeansCore() {
        nIt = 0;
        Boolean flag = true;
        //PASSO 01: Fornecer valores para os centróides.
        inicializarKlusters();
        //Criando lista com K Classes;
        inicializaClasses();
        while (flag && nIt++ < 1200) {
            //PASSO 02: Gerar uma matriz de distância entre cada ponto e os centróides.
            calcDistancias();
            showDistances();//---Somente para teste
            //PASSO 03: Colocar cada ponto nas classes de acordo com a sua distância do centróide da classe.
            refreshClasses();
            separarClasses();
            //PASSO 04: Calcular os novos centróides para cada classe.
            flag = atualizarValorCentroides();
            showKlusters();//---Somente para teste
        }
        System.out.println("Iteracoes:" + nIt);
    }

    private void refreshClasses() {
        for (int i = 0; i < nKlusters; i++) {
            vetClasses[i].clear();
        }
    }

    private void separarClasses() {
        int lin = distancias.length, dAux, i;
        Kdata aux;
        for (i = 0; i < lin - 1; i++) {
            aux = lista.get(i);
            dAux = getMenorDistancia(distancias[i]);
            aux.setClasse("C[" + dAux + "]");
            System.out.println("Classe:" + aux);
            vetClasses[dAux].add(aux);
        }
    }

    private int getMenorDistancia(Double[] vetDist) {
        int index = 0, i, len = vetDist.length;
        Double menor = vetDist[0];
        for (i = 0; i < len; i++) {
            if (vetDist[i] < menor) {
                menor = vetDist[i];
                index = i;
            }
        }
        return index;
    }
}
