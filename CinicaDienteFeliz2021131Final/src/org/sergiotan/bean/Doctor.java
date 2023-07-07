
package org.sergiotan.bean;

public class Doctor {
    private int numeroColegiado;
    private String nombresDoctor;
    private String apellidosDoctor;
    private String telefonoCOntacto;
    private int codigoEspecialidad;

    public Doctor() {
    }

    public Doctor(int numeroColegiado, String nombresDoctor, String apellidosDoctor, String telefonoCOntacto, int codigoEspecialidad) {
        this.numeroColegiado = numeroColegiado;
        this.nombresDoctor = nombresDoctor;
        this.apellidosDoctor = apellidosDoctor;
        this.telefonoCOntacto = telefonoCOntacto;
        this.codigoEspecialidad = codigoEspecialidad;
    }

    public int getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(int numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    public String getNombresDoctor() {
        return nombresDoctor;
    }

    public void setNombresDoctor(String nombresDoctor) {
        this.nombresDoctor = nombresDoctor;
    }

    public String getApellidosDoctor() {
        return apellidosDoctor;
    }

    public void setApellidosDoctor(String apellidosDoctor) {
        this.apellidosDoctor = apellidosDoctor;
    }

    public String getTelefonoCOntacto() {
        return telefonoCOntacto;
    }

    public void setTelefonoCOntacto(String telefonoCOntacto) {
        this.telefonoCOntacto = telefonoCOntacto;
    }

    public int getCodigoEspecialidad() {
        return codigoEspecialidad;
    }

    public void setCodigoEspecialidad(int codigoEspecialidad) {
        this.codigoEspecialidad = codigoEspecialidad;
    }

    @Override
    public String toString() {
        return numeroColegiado+ " | "+ nombresDoctor + " "+ apellidosDoctor;
    }
    
    
}
