
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
import org.sergiotan.bean.DetalleReceta;
import org.sergiotan.bean.Medicamento;
import org.sergiotan.bean.Receta;
import org.sergiotan.db.Conexion;
import org.sergiotan.report.GenerarReporte;
import org.sergiotan.system.Principal;

public class DetalleRecetasController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<DetalleReceta>listaDetalleReceta;
    private ObservableList<Receta>listaReceta;
    private ObservableList<Medicamento>listaMedicamento;    
    
    @FXML private TextField txtCodDetalle;
    @FXML private TextField txtDosis;
    @FXML private ComboBox cmbCodReceta;
    @FXML private ComboBox cmbCodMedicamento;
    @FXML private TableView tblDetallesRecetas;
    @FXML private TableColumn colCodDetalle;
    @FXML private TableColumn colDosis;
    @FXML private TableColumn colCodReceta;
    @FXML private TableColumn colCodMedicamento;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    
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
                imgNuevo.setImage(new Image("/org/sergiotan/image/new_add_user_16734.png"));
                imgEliminar.setImage(new Image("/org/sergiotan/image/delete_remove_user_16733.png"));
                tipoDeOperacion = operaciones.NINGUNO;                
            break;
            default:
                if(tblDetallesRecetas.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar el registro?", "Eliminar registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{call sp_EliminarDetalle(?)}");
                            procedimiento.setInt(1, ((DetalleReceta)tblDetallesRecetas.getSelectionModel().getSelectedItem()).getCodigoDetalleReceta());
                            procedimiento.execute();
                            listaDetalleReceta.remove(tblDetallesRecetas.getSelectionModel().getSelectedIndex());
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
                if(tblDetallesRecetas.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/sergiotan/image/guardar.png"));
                    imgReporte.setImage(new Image("/org/sergiotan/image/cancelar.png"));
                    activarControles();
                    txtCodDetalle.setEditable(false);
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
                if(tblDetallesRecetas.getSelectionModel().getSelectedItem() != null){
                        imprimirReporte();
                }
                else {
                    JOptionPane.showMessageDialog(null, "Selecciones un detalle de la receta");
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
        DetalleReceta registro = new DetalleReceta();
        registro.setDosis(txtDosis.getText());
        registro.setCodigoReceta(((Receta)cmbCodReceta.getSelectionModel().getSelectedItem()).getCodigoReceta());
        registro.setCodigoMedicamento(((Medicamento)cmbCodMedicamento.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_AgregarDetalle(?, ?, ?)}");
            procedimiento.setString(1, registro.getDosis());
            procedimiento.setInt(2, registro.getCodigoReceta());
            procedimiento.setInt(3, registro.getCodigoMedicamento());
            procedimiento.execute();
            listaDetalleReceta.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_EditarDetalle(?, ?)}");
            DetalleReceta registro = (DetalleReceta)tblDetallesRecetas.getSelectionModel().getSelectedItem();
            registro.setDosis((txtDosis.getText()));
            procedimiento.setInt(1, registro.getCodigoDetalleReceta());
            procedimiento.setString(2, registro.getDosis());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int COD_RECETA = ((DetalleReceta)tblDetallesRecetas.getSelectionModel().getSelectedItem()).getCodigoDetalleReceta();
        int COD_MEDI = ((DetalleReceta)tblDetallesRecetas.getSelectionModel().getSelectedItem()).getCodigoMedicamento();
        parametros.put("COD_RECETA", COD_RECETA);        
        parametros.put("COD_MEDI", COD_MEDI);        
        parametros.put("ICONO", GenerarReporte.class.getResource("/org/sergiotan/image/iconDetalleReceta.png"));
        parametros.put("FONDO", GenerarReporte.class.getResource("/org/sergiotan/image/fondoHorizontal.png"));
        GenerarReporte.mostrarReporte("RecetaMedica.jasper", "Receta Medica", parametros);
    }    
    
    
    public void cargarDatos(){
        tblDetallesRecetas.setItems(getDetalleReceta());
        colCodDetalle.setCellValueFactory(new PropertyValueFactory<DetalleReceta, Integer>("codigoDetalleReceta"));
        colDosis.setCellValueFactory(new PropertyValueFactory<DetalleReceta, String>("dosis"));
        colCodReceta.setCellValueFactory(new PropertyValueFactory<DetalleReceta, Integer>("codigoReceta"));        
        colCodMedicamento.setCellValueFactory(new PropertyValueFactory<DetalleReceta, Integer>("codigoMedicamento"));        
    }
    
    public ObservableList<DetalleReceta>getDetalleReceta(){
        ArrayList<DetalleReceta> lista = new ArrayList<DetalleReceta>();
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_ListarDetalles()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new DetalleReceta(
                          resultado.getInt("codigoDetalleReceta"),
                          resultado.getString("dosis"),
                          resultado.getInt("codigoReceta"),  
                          resultado.getInt("codigoMedicamento")  
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDetalleReceta = FXCollections.observableArrayList(lista);
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

    public void seleccionarElementos(){
        if(tblDetallesRecetas.getSelectionModel().getSelectedItem()== null){
            JOptionPane.showMessageDialog(null, "No ha seleccionado ningun dato");
        }else{
            txtCodDetalle.setText(String.valueOf(((DetalleReceta)tblDetallesRecetas.getSelectionModel().getSelectedItem()).getCodigoDetalleReceta()));
            txtDosis.setText(String.valueOf(((DetalleReceta)tblDetallesRecetas.getSelectionModel().getSelectedItem()).getDosis()));
            cmbCodReceta.getSelectionModel().select(buscarReceta(((DetalleReceta)tblDetallesRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta()));
            cmbCodMedicamento.getSelectionModel().select(buscarMedicamento(((DetalleReceta)tblDetallesRecetas.getSelectionModel().getSelectedItem()).getCodigoMedicamento()));
        }
    }    
    
    public Receta buscarReceta(int codigoReceta){
        Receta resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_BuscarReceta(?)}");
            procedimiento.setInt(1 , codigoReceta);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new Receta(registro.getInt("codigoReceta"),
                                     registro.getDate("fechaRecete"),
                                     registro.getInt("numeroColegiado")
                        
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return  resultado;
    }
    
    public Medicamento buscarMedicamento(int codigoMedicamento){
        Medicamento resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_BuscarMedicamento(?)}");
            procedimiento.setInt(1, codigoMedicamento);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Medicamento(registro.getInt("codigoMedicamento"),
                                          registro.getString("nombreMedicamento")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }    
        return resultado;
    }
 
    public void activarControles(){
        txtCodDetalle.setEditable(true);
        txtDosis.setEditable(true);
        cmbCodReceta.setDisable(false);    
        cmbCodMedicamento.setDisable(false);    
    }

    public void desactivarControles(){
        txtCodDetalle.setEditable(false);
        txtDosis.setEditable(false);
        cmbCodReceta.setDisable(true);    
        cmbCodMedicamento.setDisable(true);  
    }
    
    public void limpiarControles(){
        txtCodDetalle.clear();
        txtDosis.clear();
        cmbCodReceta.getSelectionModel().clearSelection();    
        cmbCodMedicamento.getSelectionModel().clearSelection();
        tblDetallesRecetas.getSelectionModel().clearSelection();
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
        cmbCodReceta.setItems(getReceta());
        cmbCodMedicamento.setItems(getMedicamento());
    }
        
}
