
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
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.sergiotan.bean.Paciente;
import org.sergiotan.db.Conexion;
import org.sergiotan.report.GenerarReporte;
import org.sergiotan.system.Principal;

public class PacientesController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR,NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Paciente> listaPaciente;
    private DatePicker fNacimiento, fPV;

    @FXML private TextField txtCodigoPaciente;
    @FXML private TextField txtNombrePaciente;
    @FXML private TextField txtApellidosPaciente;
    @FXML private TextField txtSexo;
    @FXML private TextField txtDireccionPaciente;
    @FXML private TextField txtTelefonoPaciente;
    @FXML private GridPane grpFechas;
    @FXML private TableView tblPacientes;
    @FXML private TableColumn colCodigoPaciente;
    @FXML private TableColumn colNombrePaciente;
    @FXML private TableColumn colApellidosPacientes;
    @FXML private TableColumn colSexo;
    @FXML private TableColumn colFechaNacimiento;
    @FXML private TableColumn colDireccionPaciente;
    @FXML private TableColumn colTelefonoPersonal;
    @FXML private TableColumn colFechaPrimeraVisita;
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
        fNacimiento = new DatePicker(Locale.ENGLISH);
        fNacimiento.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fNacimiento.getCalendarView().todayButtonTextProperty().set("Today");
        fNacimiento.getCalendarView().setShowWeeks(false);
        grpFechas.add(fNacimiento, 3,2);
        fNacimiento.getStylesheets().add("/org/sergiotan/resource/DatePicker.css");
        fPV = new DatePicker(Locale.ENGLISH);
        fPV.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fPV.getCalendarView().todayButtonTextProperty().set("Today");
        fPV.getCalendarView().setShowWeeks(false);
        grpFechas.add(fPV, 4, 3);
        fPV.getStylesheets().add("/org/sergiotan/resource/DatePicker.css");
    }
    public void cargarDatos(){
        tblPacientes.setItems(getPaciente());
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory <Paciente, Integer>("codigoPaciente"));
        colNombrePaciente.setCellValueFactory(new PropertyValueFactory <Paciente, String>("nombrePaciente"));
        colApellidosPacientes.setCellValueFactory(new PropertyValueFactory <Paciente, String>("apellidosPaciente"));
        colSexo.setCellValueFactory(new PropertyValueFactory <Paciente, String>("sexo"));
        colFechaNacimiento.setCellValueFactory(new PropertyValueFactory <Paciente, Date>("FechaNacimiento"));
        colDireccionPaciente.setCellValueFactory(new PropertyValueFactory <Paciente, String>("direccionPaciente"));
        colTelefonoPersonal.setCellValueFactory(new PropertyValueFactory <Paciente, String>("telefonoPersonal"));
        colFechaPrimeraVisita.setCellValueFactory(new PropertyValueFactory <Paciente, String>("fechaPrimeraVisita"));
    }
    
    
    
    
    public void seleccionarElementos(){
        if(tblPacientes.getSelectionModel().getSelectedItem()== null){
            JOptionPane.showMessageDialog(null, "No ha seleccionado ningun dato");
        }
        else{
        txtCodigoPaciente.setText(String.valueOf(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
        txtNombrePaciente.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getNombrePaciente());
        txtApellidosPaciente.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getApellidosPaciente());
        txtSexo.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getSexo());
        fNacimiento.selectedDateProperty().set(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getFechaNacimiento());
        txtDireccionPaciente.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getDireccionPaciente());
        txtTelefonoPaciente.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
        fPV.selectedDateProperty().set(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getFechaPrimeraVisita());
        
        }

        
    }
    
    public ObservableList<Paciente> getPaciente(){
        ArrayList<Paciente> lista= new ArrayList<Paciente>();
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{call sp_ListarPacientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Paciente( resultado.getInt("codigoPaciente"), 
                                        resultado.getString("nombrePaciente"),
                                        resultado.getString("apellidosPaciente"),
                                        resultado.getString("sexo"),
                                        resultado.getDate("fechaNacimiento"),
                                        resultado.getString("direccionPaciente"),
                                        resultado.getString("telefonoPersonal"),
                                        resultado.getDate("fechaPrimeraVisita")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPaciente = FXCollections.observableArrayList(lista);
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
                if(tblPacientes.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar el registro?", "Eliminar registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{call sp_EliminarPaciente(?)}");
                                procedimiento.setInt(1, ((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente());
                                procedimiento.execute();
                                listaPaciente.remove(tblPacientes.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            
                        }else {
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
                if(tblPacientes.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/sergiotan/image/guardar.png"));
                    imgReporte.setImage(new Image("/org/sergiotan/image/cancelar.png"));
                    activarControles();
                    txtCodigoPaciente.setEditable(false);
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
        Paciente registro = new Paciente();
        registro.setCodigoPaciente(Integer.parseInt(txtCodigoPaciente.getText()));
        registro.setNombrePaciente(txtNombrePaciente.getText());
        registro.setApellidosPaciente(txtApellidosPaciente.getText());
        registro.setSexo(txtSexo.getText());
        registro.setFechaNacimiento(fNacimiento.getSelectedDate());
        registro.setDireccionPaciente(txtDireccionPaciente.getText());
        registro.setTelefonoPersonal(txtTelefonoPaciente.getText());
        registro.setFechaPrimeraVisita(fPV.getSelectedDate());
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_AgregarPaciente(?, ?, ?, ?, ?, ?, ?,?)}");
            procedimiento.setInt(1, registro.getCodigoPaciente());
            procedimiento.setString(2, registro.getNombrePaciente());
            procedimiento.setString(3, registro.getApellidosPaciente());
            procedimiento.setString(4, registro.getSexo());
            procedimiento.setDate(5, new java.sql.Date(registro.getFechaNacimiento().getTime()));
            procedimiento.setString(6, registro.getDireccionPaciente());
            procedimiento.setString(7, registro.getTelefonoPersonal());
            procedimiento.setDate(8, new java.sql.Date(registro.getFechaPrimeraVisita().getTime()));
            procedimiento.execute();
            listaPaciente.add(registro);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_EditarPaciente (?, ?,?, ?, ?,?,?,?)}");
                Paciente registro = (Paciente)tblPacientes.getSelectionModel().getSelectedItem();
                registro.setNombrePaciente(txtNombrePaciente.getText());
                registro.setApellidosPaciente(txtApellidosPaciente.getText());
                registro.setSexo(txtSexo.getText());
                registro.setFechaNacimiento(fNacimiento.getSelectedDate());
                registro.setDireccionPaciente(txtDireccionPaciente.getText());
                registro.setTelefonoPersonal(txtTelefonoPaciente.getText());
                registro.setFechaPrimeraVisita(fPV.getSelectedDate());
                procedimiento.setInt(1, registro.getCodigoPaciente());
                procedimiento.setString(2, registro.getNombrePaciente());
                procedimiento.setString(3, registro.getApellidosPaciente());
                procedimiento.setString(4, registro.getSexo());
                procedimiento.setDate(5, new java.sql.Date(registro.getFechaNacimiento().getTime()));
                procedimiento.setString(6, registro.getDireccionPaciente());
                procedimiento.setString(7, registro.getTelefonoPersonal());
                procedimiento.setDate(8, new java.sql.Date(registro.getFechaPrimeraVisita().getTime()));
                procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoPaciente", null);
        parametros.put("ICONO", GenerarReporte.class.getResource("/org/sergiotan/image/Patient_Male_icon-icons.com_75053.png"));
        parametros.put("FONDO", GenerarReporte.class.getResource("/org/sergiotan/image/fondoHorizontal.png"));
        GenerarReporte.mostrarReporte("ReportePacientes.jasper", "Reporte de Pacientes", parametros);
    }
    
    public void desactivarControles(){
        txtCodigoPaciente.setEditable(false);
        txtNombrePaciente.setEditable(false);
        txtApellidosPaciente.setEditable(false);
        txtSexo.setEditable(false);
        txtDireccionPaciente.setEditable(false);
        txtTelefonoPaciente.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoPaciente.setEditable(true);
        txtNombrePaciente.setEditable(true);
        txtApellidosPaciente.setEditable(true);
        txtSexo.setEditable(true);
        txtDireccionPaciente.setEditable(true);
        txtTelefonoPaciente.setEditable(true);
    }
    
    public void limpiarControles (){
        txtCodigoPaciente.clear();
        txtNombrePaciente.clear();
        txtApellidosPaciente.clear();
        txtSexo.clear();
        txtDireccionPaciente.clear();
        txtTelefonoPaciente.clear();
        tblPacientes.getSelectionModel().clearSelection();
        fNacimiento.setSelectedDate(null);
        fPV.setSelectedDate(null);
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
