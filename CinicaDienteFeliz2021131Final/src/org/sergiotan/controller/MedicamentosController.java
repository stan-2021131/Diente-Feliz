
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
import org.sergiotan.bean.Medicamento;
import org.sergiotan.db.Conexion;
import org.sergiotan.report.GenerarReporte;
import org.sergiotan.system.Principal;

public class MedicamentosController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Medicamento>listaMedicamento;
    
    @FXML private TextField txtCodigoMedicamento;
    @FXML private TextField txtMedicamento;
    @FXML private TableView tblMedicamentos;
    @FXML private TableColumn colCodigoMedicamento;
    @FXML private TableColumn colMedicamento;
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
        tblMedicamentos.setItems(getMedicamento());
        colCodigoMedicamento.setCellValueFactory(new PropertyValueFactory<Medicamento, Integer>("codigoMedicamento"));
        colMedicamento.setCellValueFactory(new PropertyValueFactory<Medicamento, String>("nombreMedicamento"));
    }
    
    public ObservableList<Medicamento>getMedicamento(){
        ArrayList<Medicamento>lista = new ArrayList<Medicamento>();
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_ListarMedicamentos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Medicamento(resultado.getInt("codigoMedicamento"),
                                          resultado.getString("nombreMedicamento")
                                
                        ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaMedicamento = FXCollections.observableArrayList(lista);
    }

    
    public void seleccionarElementos(){
        if(tblMedicamentos.getSelectionModel().getSelectedItem()== null){
            JOptionPane.showMessageDialog(null, "No ha seleccionado ningun dato");
        }else{
            txtCodigoMedicamento.setText(String.valueOf(((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getCodigoMedicamento()));
            txtMedicamento.setText(String.valueOf(((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getNombreMedicamento()));
        }
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

    public void guardar(){
            Medicamento registro = new Medicamento();
        registro.setNombreMedicamento(txtMedicamento.getText());
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_AgregarMedicamento(?)}");
            procedimiento.setString(1, registro.getNombreMedicamento());
            procedimiento.execute();            
            listaMedicamento.add(registro);
        }catch(Exception e){
            e.printStackTrace();
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
                if(tblMedicamentos.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar el registro?", "Eliminar registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{call sp_EliminarMedicamento(?)}");
                            procedimiento.setInt(1, ((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
                            procedimiento.execute();
                            listaMedicamento.remove(tblMedicamentos.getSelectionModel().getSelectedIndex());
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
                if(tblMedicamentos.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/sergiotan/image/guardar.png"));
                    imgReporte.setImage(new Image("/org/sergiotan/image/cancelar.png"));
                    activarControles();
                    txtCodigoMedicamento.setEditable(false);
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

    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{call sp_EditarMedicamento(?,?)}");
            Medicamento registro = (Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem();
            registro.setNombreMedicamento(txtMedicamento.getText());
            procedimiento.setInt(1, registro.getCodigoMedicamento());
            procedimiento.setString(2, registro.getNombreMedicamento());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
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

    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("ICONO", GenerarReporte.class.getResource("/org/sergiotan/image/Medicamento.png"));
        parametros.put("FONDO", GenerarReporte.class.getResource("/org/sergiotan/image/fondoHorizontal.png"));
        GenerarReporte.mostrarReporte("ReporteMedicamentos.jasper", "Reporte Medicamentos", parametros);
    }        
    
    public void activarControles(){
        txtCodigoMedicamento.setEditable(true);
        txtMedicamento.setEditable(true);    
    }

    public void desactivarControles(){
        txtCodigoMedicamento.setEditable(false);
        txtMedicamento.setEditable(false);    
    }
    
    public void limpiarControles(){
        txtCodigoMedicamento.clear();
        txtMedicamento.clear();
        tblMedicamentos.getSelectionModel().clearSelection();
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
