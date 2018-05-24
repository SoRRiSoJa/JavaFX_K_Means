/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jamdes.util;

import com.jamdes.model.Kdata;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author joao
 */
public class ReaderCSV {

    private String linha;
    private String fileName;
    private BufferedReader br;

    public ReaderCSV(String fileName) {
        this.fileName = fileName;
        this.linha = null;
        try {
            this.br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        } catch (FileNotFoundException ex) {
            System.out.println("Erro ao abrir arquio:" + ex);
        }
    }

    /**
     * Realiza a leitura do arquivo CSV e gera uma lista de pontos(classes)
     * public List<Kdata> readFile()
     *
     * @return List<Kdata> - Lista de pontos (classes)
     */
    public List<Kdata> readFile() {
        List<Kdata> kd = new ArrayList<>();
        List<Double> caracteristicas = new ArrayList<>();
        long cont = 0;
        try {
            while ((linha = br.readLine()) != null) {
                String[] dadosClasse = linha.split(",");
                for (String dado : dadosClasse) {
                    try {
                        caracteristicas.add(new Double(dado));
                    } catch (Exception ex) {
                        System.out.println("Erro ao converter" + ex);
                    }
                }
                kd.add(new Kdata(cont, caracteristicas, "N/A"));
                caracteristicas = new ArrayList<>();
            }
            br.close();
        } catch (IOException ex) {
            System.out.println("Erro ao ler arquio:" + ex);
        }
        return kd;
    }
}
