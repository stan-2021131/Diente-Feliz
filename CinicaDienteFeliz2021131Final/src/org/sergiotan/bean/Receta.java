
package org.sergiotan.bean;

import java.util.Date;

public class Receta {
    private int codigoReceta;
    private Date fechaRecete;
    private int numeroColegiado;

    public Receta() {
    }

    public Receta(int codigoReceta, Date fechaRecete) {
        this.codigoReceta = codigoReceta;
        this.fechaRecete = fechaRecete;
    }

    public Receta(Date fechaRecete, int numeroColegiado) {
        this.fechaRecete = fechaRecete;
        this.numeroColegiado = numeroColegiado;
    }

    public Receta(int codigoReceta, int numeroColegiado) {
        this.codigoReceta = codigoReceta;
        this.numeroColegiado = numeroColegiado;
    }

    public Receta(int codigoReceta, Date fechaRecete, int numeroColegiado) {
        this.codigoReceta = codigoReceta;
        this.fechaRecete = fechaRecete;
        this.numeroColegiado = numeroColegiado;
    }

    public Receta(int codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    public Receta(Date fechaRecete) {
        this.fechaRecete = fechaRecete;
    }

    public int getCodigoReceta() {
        return codigoReceta;
    }

    public void setCodigoReceta(int codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    public Date getFechaRecete() {
        return fechaRecete;
    }

    public void setFechaRecete(Date fechaRecete) {
        this.fechaRecete = fechaRecete;
    }

    public int getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(int numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    @Override
    public String toString() {
        return codigoReceta + " | " + fechaRecete + " | " + numeroColegiado;
    }
    
    
}
