package org.sergiotan.controller;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.sergiotan.report.GenerarReporte;
import org.sergiotan.system.Principal;

public class MenuPrincipalController implements Initializable{
    private Principal escenarioPrincipal;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    };
    public void ventanaPacientes(){
        escenarioPrincipal.ventanaPacientes();
    }
    public void ventanaDoctores(){
        escenarioPrincipal.ventanaDoctores();
    }
    public void ventanaEspecialidades(){
        escenarioPrincipal.ventanaEspecialidades();
    }
    public void ventanaMedicamentos(){
        escenarioPrincipal.ventanaMedicamentos();
    }
    public void ventanaRecetas(){
        escenarioPrincipal.ventanaRecetas();
    }
    public void ventanaDetalleRecetas(){
        escenarioPrincipal.ventanaDetalleRecetas();
    }
    public void ventanaCitas(){
        escenarioPrincipal.ventanaCitas();
    }
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
    public void ventanaUsuario(){
        escenarioPrincipal.ventanaUsuario();
    }
    
    public void imprimirReportePaciente(){
        Map parametros = new HashMap();
        parametros.put("codigoPaciente", null);
        parametros.put("ICONO", GenerarReporte.class.getResource("/org/sergiotan/image/Patient_Male_icon-icons.com_75053.png"));
        parametros.put("FONDO", GenerarReporte.class.getResource("/org/sergiotan/image/fondoHorizontal.png"));
        GenerarReporte.mostrarReporte("ReportePacientes.jasper", "Reporte de Pacientes", parametros);
    }
    
    public void imprimirReporteEspecialidades(){
        Map parametros = new HashMap();
        parametros.put("ICONO", GenerarReporte.class.getResource("/org/sergiotan/image/tooth_icon_134228.png"));
        parametros.put("FONDO", GenerarReporte.class.getResource("/org/sergiotan/image/fondoHorizontal.png"));
        GenerarReporte.mostrarReporte("ReporteEspecialidades.jasper", "Reporte Especialidades", parametros);
    }
    
    public void imprimirReporteMedicamentos(){
        Map parametros = new HashMap();
        parametros.put("ICONO", GenerarReporte.class.getResource("/org/sergiotan/image/Medicamento.png"));
        parametros.put("FONDO", GenerarReporte.class.getResource("/org/sergiotan/image/fondoHorizontal.png"));
        GenerarReporte.mostrarReporte("ReporteMedicamentos.jasper", "Reporte Medicamentos", parametros);
    }
    
    public void imprimirReporteDoctores(){
        Map parametros = new HashMap();
        parametros.put("numeroColegiado", null);
        parametros.put("ICONO", GenerarReporte.class.getResource("/org/sergiotan/image/Doctor_Male_icon-icons.com_75051.png"));
        parametros.put("FONDO", GenerarReporte.class.getResource("/org/sergiotan/image/fondoHorizontal.png"));
        GenerarReporte.mostrarReporte("ReporteDoctores.jasper", "Reporte de Doctores", parametros);
    }

    public void imprimirReporteRecetas(){
        Map parametros = new HashMap();
        parametros.put("ICONO", GenerarReporte.class.getResource("/org/sergiotan/image/iconReceta.png"));
        parametros.put("FONDO", GenerarReporte.class.getResource("/org/sergiotan/image/fondoHorizontal.png"));
        GenerarReporte.mostrarReporte("ReporteRecetas.jasper", "Reporte Recetas", parametros);
    }
    public void imprimirReporteCitas(){
        Map parametros = new HashMap();
        parametros.put("ICONO", GenerarReporte.class.getResource("/org/sergiotan/image/Patient_Male_icon-icons.com_75053.png"));
        parametros.put("FONDO", GenerarReporte.class.getResource("/org/sergiotan/image/fondoHorizontal.png"));
        GenerarReporte.mostrarReporte("ReporteCitas.jasper", "Reporte Citas", parametros);
    }      
         
}
