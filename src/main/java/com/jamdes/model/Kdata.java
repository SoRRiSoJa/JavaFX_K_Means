/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jamdes.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author joao
 */
public class Kdata {

    private long id;
    private List<Double> caracteristicas;
    private String classe;

    public Kdata() {
        this.id = 0l;
        this.caracteristicas = new ArrayList<>();
        this.classe = "";
    }

    public Kdata(long id, List<Double> caracteristicas, String classe) {
        this.id = id;
        this.caracteristicas = caracteristicas;
        this.classe = classe;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Double> getCaracteristicas() {
        return caracteristicas;
    }

    public Double[] getCaracteristicasD() {
        Double[] aux = new Double[caracteristicas.size()];
        for (int i = 0; i < caracteristicas.size(); i++) {
            aux[i] = caracteristicas.get(i);
        }
        return aux;
    }

    public void setCaracteristicas(List<Double> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public void setCaracteristica(Double caracteristica) {
        this.caracteristicas.add(caracteristica);
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 97 * hash + Objects.hashCode(this.caracteristicas);
        hash = 97 * hash + Objects.hashCode(this.classe);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Kdata other = (Kdata) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.classe, other.classe)) {
            return false;
        }
        if (!Objects.equals(this.caracteristicas, other.caracteristicas)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id + " | " + classe;
    }

}
