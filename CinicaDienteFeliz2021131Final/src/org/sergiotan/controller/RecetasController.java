
package org.sergiotan.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.sergiotan.bean.Doctor;
import org.sergiotan.bean.Receta;
import org.sergiotan.db.Conexion;
import org.sergiotan.report.GenerarReporte;
import org.sergiotan.system.Principal;

public class RecetasController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Receta>listaReceta;
    private ObservableList<Doctor>listaDoctor;
    private DatePicker fReceta;
    
    @FXML private TextField txtCodReceta;
    @FXML private GridPane grpFechaReceta;
    @FXML private ComboBox cmbNumColegiado;
    @FXML private TableView tblRecetas;
    @FXML private TableColumn colCodReceta;
    @FXML private TableColumn colFechaReceta;
    @FXML private TableColumn colNumColegiado;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    public void cargarDatos(){
        tblRecetas.setItems(getReceta());
        colCodReceta.setCellValueFactory(new PropertyValueFactory <Receta, Integer>("codigoReceta"));
        colFechaReceta.setCellValueFactory(new PropertyValueFactory <Receta, Date>("fechaRecete"));
        colNumColegiado.setCellValueFactory(new PropertyValueFactory <Receta, Integer>("numeroColegiado"));
    }
    
    public ObservableList<Receta>getReceta(){
        ArrayList<Receta> lista = new ArrayList<Receta>();
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_ListarRecetas()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Receta(resultado.getInt("codigoReceta"),
                                     resultado.getDate("fechaRecete"),
                                     resultado.getInt("numeroColegiado")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return  listaReceta = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Doctor>getDoctor(){
        ArrayList<Doctor> lista = new ArrayList<Doctor>();
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_ListarDoctores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Doctor(resultado.getInt("numeroColegiado"),
                                     resultado.getString("nombresDoctor"),
                                     resultado.getString("ApellidosDoctor"),
                                     resultado.getString("telefonoCOntacto"),
                                     resultado.getInt("codigoEspecialidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return  listaDoctor = FXCollections.observableArrayList(lista);
    }

    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                    btnNuevo.setText("Guardar");
                    btnEliminar.setText("Cancelar");
                    btnEditar.setDisable(true);
                    btnReporte.setDisable(true);
                    imgNuevo.setImage(new Image("/org/sergiotan/image/guardar.png"));
                    imgEliminar.setImage(new Image("/org/sergiotan/image/cancelar.png"));
                    tipoDeOperacion = operaciones.GUARDAR;
            break;
            case GUARDAR:
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgNuevo.setImage(new Image("/org/sergiotan/image/add_plus_interface_icon_181584.png"));
                    imgEliminar.setImage(new Image("/org/sergiotan/image/delete_remove_close_checkbox_check_icon_181586.png"));
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();                
            break;
        }
        
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/sergiotan/image/add_plus_interface_icon_181584.png"));
                imgEliminar.setImage(new Image("/org/sergiotan/image/delete_remove_close_checkbox_check_icon_181586.png"));
                tipoDeOperacion = operaciones.NINGUNO;                
            break;
            default:
                if(tblRecetas.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar el registro?", "Eliminar registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{call sp_EliminarReceta(?)}");
                            procedimiento.setInt(1, ((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta());
                            procedimiento.execute();
                            listaReceta.remove(tblRecetas.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }else{
                            desactivarControles();
                            limpiarControles();
                        }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
            break;
        }
    }    

    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblRecetas.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/sergiotan/image/guardar.png"));
                    imgReporte.setImage(new Image("/org/sergiotan/image/cancelar.png"));
                    activarControles();
                    txtCodReceta.setEditable(false);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    
                }else 
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");                
            break;
            case ACTUALIZAR:
                actualizar();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/sergiotan/image/editnote_edi_9512.png"));
                imgReporte.setImage(new Image("/org/sergiotan/image/document_note_text_file_icon_181575.png"));
                desactivarControles();
                limpiarControles();
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;                
            break;
        }
    }

    public void reporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
            break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/sergiotan/image/editnote_edi_9512.png"));
                imgReporte.setImage(new Image("/org/sergiotan/image/document_note_text_file_icon_181575.png"));
                tipoDeOperacion = operaciones.NINGUNO;
            break;
        }
    }     
    
    public void guardar(){
        Receta registro = new Receta();
        registro.setFechaRecete(fReceta.getSelectedDate());
        registro.setNumeroColegiado(((Doctor)cmbNumColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_AgregarReceta(?, ?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaRecete().getTime()));
            procedimiento.setInt(2, registro.getNumeroColegiado());
            procedimiento.execute();
            listaReceta.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_EditarReceta(?, ?)}");
            Receta registro = (Receta)tblRecetas.getSelectionModel().getSelectedItem();
            registro.setFechaRecete(fReceta.getSelectedDate());
            procedimiento.setInt(1, registro.getCodigoReceta());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaRecete().getTime()));
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("ICONO", GenerarReporte.class.getResource("/org/sergiotan/image/iconReceta.png"));
        parametros.put("FONDO", GenerarReporte.class.getResource("/org/sergiotan/image/fondoHorizontal.png"));
        GenerarReporte.mostrarReporte("ReporteRecetas.jasper", "Reporte Recetas", parametros);
    }
    
    public void seleccionarElementos(){
        if(tblRecetas.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null, "No se ha seleccionado ningú dato");
        }else{
            txtCodReceta.setText(String.valueOf(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta()));
            cmbNumColegiado.getSelectionModel().select(buscarDoctor(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
            fReceta.selectedDateProperty().set(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getFechaRecete());            
        }
    }

    public Doctor buscarDoctor(int numeroColegiado){
        Doctor resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{call sp_BuscarDoctor(?)}");
            procedimiento.setInt(1,numeroColegiado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Doctor (registro.getInt("numeroColegiado"),
                                     registro.getString("nombresDoctor"),
                                     registro.getString("ApellidosDoctor"),
                                     registro.getString("telefonoCOntacto"),
                                     registro.getInt("codigoEspecialidad"));

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return 
                resultado;
    }
    
    public void desactivarControles(){
        txtCodReceta.setEditable(false);
        fReceta.setDisable(true);
        cmbNumColegiado.setDisable(true);
    }

    public void activarControles(){
        txtCodReceta.setEditable(true);
        fReceta.setDisable(false);
        cmbNumColegiado.setDisable(false);
    }

    public void limpiarControles(){
        txtCodReceta.clear();
        fReceta.setSelectedDate(null);
        cmbNumColegiado.getSelectionModel().clearSelection();
        tblRecetas.getSelectionModel().clearSelection();          
    }
    
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbNumColegiado.setItems(getDoctor());
        fReceta = new DatePicker(Locale.ENGLISH);
        fReceta.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fReceta.getCalendarView().todayButtonTextProperty().set("Today");
        fReceta.getCalendarView().setShowWeeks(false);
        grpFechaReceta.add(fReceta, 3,1);
        fReceta.getStylesheets().add("/org/sergiotan/resource/DatePicker.css");
    }
}
