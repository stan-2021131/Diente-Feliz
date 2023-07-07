
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.sergiotan.bean.Doctor;
import org.sergiotan.bean.Especialidad;
import org.sergiotan.db.Conexion;
import org.sergiotan.report.GenerarReporte;
import org.sergiotan.system.Principal;

public class DoctoresController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Doctor>listaDoctor;
    private ObservableList<Especialidad>listaEspecialidad;
    
    @FXML private TextField txtNumColegiado;
    @FXML private TextField txtNombresDoctor;
    @FXML private TextField txtApellidosDoctor;
    @FXML private TextField txtTelefonoDoctor;
    @FXML private ComboBox cmbCodigoEspecialidad;
    @FXML private TableView tblDoctores;
    @FXML private TableColumn colNoColegiado;
    @FXML private TableColumn colNombresDoctor;
    @FXML private TableColumn colApellidosDoctor;
    @FXML private TableColumn colTelefonoDoctor;
    @FXML private TableColumn colCodigoEspecialidad;
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
        cmbCodigoEspecialidad.setItems(getEspecialidad());
    }

    public void cargarDatos(){
        tblDoctores.setItems(getDoctor());
        colNoColegiado.setCellValueFactory(new PropertyValueFactory<Doctor, Integer>("numeroColegiado"));
        colNombresDoctor.setCellValueFactory(new PropertyValueFactory <Doctor, String>("nombresDoctor"));
        colApellidosDoctor.setCellValueFactory(new PropertyValueFactory <Doctor, String>("apellidosDoctor"));
        colTelefonoDoctor.setCellValueFactory(new PropertyValueFactory <Doctor, String>("telefonoCOntacto"));
        colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory <Doctor, Integer>("codigoEspecialidad"));
    }
    
    public ObservableList<Doctor>getDoctor(){
        ArrayList<Doctor> lista = new ArrayList<Doctor>();
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{call sp_ListarDoctores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Doctor(resultado.getInt("numeroColegiado"),
                                     resultado.getString("nombresDoctor"),
                                     resultado.getString("ApellidosDoctor"),
                                     resultado.getString("telefonoCOntacto"),
                                     resultado.getInt("codigoEspecialidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDoctor = FXCollections.observableArrayList(lista);
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
                    imgNuevo.setImage(new Image("/org/sergiotan/image/new_add_user_16734.png"));
                    imgEliminar.setImage(new Image("/org/sergiotan/image/delete_remove_user_16733.png"));
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
                imgNuevo.setImage(new Image("/org/sergiotan/image/new_add_user_16734.png"));
                imgEliminar.setImage(new Image("/org/sergiotan/image/delete_remove_user_16733.png"));
                tipoDeOperacion = operaciones.NINGUNO;                
            break;
            default:
                if(tblDoctores.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar el registro?", "Eliminar registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{call sp_EliminarDoctor(?)}");
                            procedimiento.setInt(1, ((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getNumeroColegiado());
                            procedimiento.execute();
                            listaDoctor.remove(tblDoctores.getSelectionModel().getSelectedIndex());
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
                if(tblDoctores.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/sergiotan/image/guardar.png"));
                    imgReporte.setImage(new Image("/org/sergiotan/image/cancelar.png"));
                    activarControles();
                    txtNumColegiado.setEditable(false);
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
                if(tblDoctores.getSelectionModel().getSelectedItem() != null){
                        imprimirReporteEsp();
                }
                else {
                    imprimirReporte();
                }
                limpiarControles();
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
        Doctor registro = new Doctor();
        registro.setNumeroColegiado(Integer.parseInt(txtNumColegiado.getText()));
        registro.setNombresDoctor(txtNombresDoctor.getText());
        registro.setApellidosDoctor(txtApellidosDoctor.getText());
        registro.setTelefonoCOntacto(txtTelefonoDoctor.getText());
        registro.setCodigoEspecialidad(((Especialidad)cmbCodigoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_AgregarDoctor(?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getNumeroColegiado());
            procedimiento.setString(2, registro.getNombresDoctor());
            procedimiento.setString(3, registro.getApellidosDoctor());
            procedimiento.setString(4, registro.getTelefonoCOntacto());
            procedimiento.setInt(5, registro.getCodigoEspecialidad());
            procedimiento.execute();
            listaDoctor.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{call sp_EditarDoctor(?, ?, ?, ?)}");
            Doctor registro = (Doctor)tblDoctores.getSelectionModel().getSelectedItem();
            registro.setNombresDoctor(txtNombresDoctor.getText());
            registro.setApellidosDoctor(txtApellidosDoctor.getText());
            registro.setTelefonoCOntacto(txtTelefonoDoctor.getText());
            procedimiento.setInt(1, registro.getNumeroColegiado());
            procedimiento.setString(2, registro.getNombresDoctor());
            procedimiento.setString(3, registro.getApellidosDoctor());
            procedimiento.setString(4, registro.getTelefonoCOntacto());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("numeroColegiado", null);
        parametros.put("ICONO", GenerarReporte.class.getResource("/org/sergiotan/image/Doctor_Male_icon-icons.com_75051.png"));
        parametros.put("FONDO", GenerarReporte.class.getResource("/org/sergiotan/image/fondoHorizontal.png"));
        GenerarReporte.mostrarReporte("ReporteDoctores.jasper", "Reporte de Doctores", parametros);
    }
       
    public void imprimirReporteEsp(){
        Map parametros = new HashMap();
        int NUM_COLEGIADO = ((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getNumeroColegiado();
        parametros.put("NUM_COLEGIADO", NUM_COLEGIADO);
        parametros.put("ICONO", GenerarReporte.class.getResource("/org/sergiotan/image/Doctor_Male_icon-icons.com_75051.png"));
        parametros.put("FONDO", GenerarReporte.class.getResource("/org/sergiotan/image/fondoHorizontal.png"));  
        GenerarReporte.mostrarReporte("ReporteDoctor.jasper", "Reporte del Doctor", parametros);

    }
    
    
    public void seleccionarElementos(){
        if(tblDoctores.getSelectionModel().getSelectedItem()== null){
            JOptionPane.showMessageDialog(null, "No ha seleccionado ningun dato");
        }else{
            txtNumColegiado.setText(String.valueOf(((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
            txtNombresDoctor.setText(((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getNombresDoctor());
            txtApellidosDoctor.setText(((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getApellidosDoctor());
            txtTelefonoDoctor.setText(((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getTelefonoCOntacto());
            //cmbCodigoEspecialidad.setItems(String.valueOf((Doctor)tblDoctores.getSelectionModel().getSelectedItem().getCodigoEspecialidad()));
        }
    }    
    
    public void seleccionarElemento(){
        if(tblDoctores.getSelectionModel().getSelectedItem()== null){
            JOptionPane.showMessageDialog(null, "No se ha seleccionado nigun dato");
        }else{
            txtNumColegiado.setText(String.valueOf(((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
            txtNombresDoctor.setText(((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getNombresDoctor());
            txtApellidosDoctor.setText(((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getApellidosDoctor());
            txtTelefonoDoctor.setText(((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getTelefonoCOntacto());
            cmbCodigoEspecialidad.getSelectionModel().select(buscarEspecialidad(((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
        }
    }
        
    
    public Especialidad buscarEspecialidad(int codigoEspecialidad){
        Especialidad resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{call sp_BuscarEspecialidad(?)}");
            procedimiento.setInt(1, codigoEspecialidad);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Especialidad (registro.getInt("codigoEspecialidad"),
                                              registro.getString("descripcion")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return 
                resultado;
    }
    
    public void desactivarControles(){
        txtNumColegiado.setEditable(false);
        txtNombresDoctor.setEditable(false);
        txtApellidosDoctor.setEditable(false);
        txtTelefonoDoctor.setEditable(false);
        cmbCodigoEspecialidad.setDisable(true);
    }
    
    public void activarControles(){
        txtNumColegiado.setEditable(true);
        txtNombresDoctor.setEditable(true);
        txtApellidosDoctor.setEditable(true);
        txtTelefonoDoctor.setEditable(true);
        cmbCodigoEspecialidad.setDisable(false);
    }
    
    public void limpiarControles(){
        txtNumColegiado.clear();
        txtNombresDoctor.clear();
        txtApellidosDoctor.clear();
        txtTelefonoDoctor.clear();
        tblDoctores.getSelectionModel().clearSelection();
        cmbCodigoEspecialidad.getSelectionModel().clearSelection();
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
