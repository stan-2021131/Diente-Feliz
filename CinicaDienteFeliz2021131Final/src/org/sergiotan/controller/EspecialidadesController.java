
package org.sergiotan.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.sergiotan.bean.Especialidad;
import org.sergiotan.db.Conexion;
import org.sergiotan.report.GenerarReporte;
import org.sergiotan.system.Principal;

public class EspecialidadesController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR,NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Especialidad> listaEspecialidad;

    
    @FXML private TextField txtCodigoEspecialidad;
    @FXML private TextField txtEspecialidad;
    @FXML private TableView tblEspecialidad;
    @FXML private TableColumn colCodigoEspecialidad;
    @FXML private TableColumn colEspecialidad;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblEspecialidad.setItems(getEspecialidad());
        colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory <Especialidad, Integer>("codigoEspecialidad"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory <Especialidad, String>("descripcion"));
    }
    
    public void seleccionarElementos(){
        if(tblEspecialidad.getSelectionModel().getSelectedItem()== null){
            JOptionPane.showMessageDialog(null, "No ha seleccionado ningun dato");
        }else{
            txtCodigoEspecialidad.setText(String.valueOf(((Especialidad)tblEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
            txtEspecialidad.setText(String.valueOf(((Especialidad)tblEspecialidad.getSelectionModel().getSelectedItem()).getDescripcion()));
        }
    }
    
    public ObservableList<Especialidad> getEspecialidad(){
        ArrayList<Especialidad> lista = new ArrayList<Especialidad>();
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{call sp_ListarEspecialidades}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Especialidad(
                        resultado.getInt("codigoEspecialidad"),
                        resultado.getString("descripcion")             
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEspecialidad = FXCollections.observableArrayList(lista);
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
                if(tblEspecialidad.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar el registro?", "Eliminar registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{call sp_EliminarEspecialidad(?)}");
                            procedimiento.setInt(1, ((Especialidad)tblEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
                            procedimiento.execute();
                            listaEspecialidad.remove(tblEspecialidad.getSelectionModel().getSelectedIndex());
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
                if(tblEspecialidad.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/sergiotan/image/guardar.png"));
                    imgReporte.setImage(new Image("/org/sergiotan/image/cancelar.png"));
                    activarControles();
                    txtCodigoEspecialidad.setEditable(false);
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
        Especialidad registro = new Especialidad();
        registro.setDescripcion(txtEspecialidad.getText());
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_AgregarEspecialidad(?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();            
            listaEspecialidad.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{call sp_EditarEspecialidad(?,?)}");
            Especialidad registro = (Especialidad)tblEspecialidad.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtEspecialidad.getText());
            procedimiento.setInt(1, registro.getCodigoEspecialidad());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("ICONO", GenerarReporte.class.getResource("/org/sergiotan/image/tooth_icon_134228.png"));
        parametros.put("FONDO", GenerarReporte.class.getResource("/org/sergiotan/image/fondoHorizontal.png"));
        GenerarReporte.mostrarReporte("ReporteEspecialidades.jasper", "Reporte Especialidades", parametros);
    }    
    
    public void desactivarControles(){
        txtCodigoEspecialidad.setEditable(false);
        txtEspecialidad.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoEspecialidad.setEditable(true);
        txtEspecialidad.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoEspecialidad.clear();
        txtEspecialidad.clear();
        tblEspecialidad.getSelectionModel().clearSelection();        
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
}
