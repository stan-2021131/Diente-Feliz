package org.sergiotan.bean;

import java.sql.Time;
import java.util.Date;


public class Cita {
   private int codigoCita;
   private Date fechaCita;
   private Time horaCita;
   private String tratamiento;
   private String descripConActual;
   private int codigoPaciente;
   private int numeroColegiado;

    public Cita() {
    }

    public Cita(int codigoCita, Date fechaCita, Time horaCita, String tratamiento, String descripConActual, int codigoPaciente, int numeroColegiado) {
        this.codigoCita = codigoCita;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.tratamiento = tratamiento;
        this.descripConActual = descripConActual;
        this.codigoPaciente = codigoPaciente;
        this.numeroColegiado = numeroColegiado;
    }

    public int getCodigoCita() {
        return codigoCita;
    }

    public void setCodigoCita(int codigoCita) {
        this.codigoCita = codigoCita;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public Time getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(Time horaCita) {
        this.horaCita = horaCita;
    }


    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getDescripConActual() {
        return descripConActual;
    }

    public void setDescripConActual(String descripConActual) {
        this.descripConActual = descripConActual;
    }

    public int getCodigoPaciente() {
        return codigoPaciente;
    }

    public void setCodigoPaciente(int codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }

    public int getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(int numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }
   
   
}
