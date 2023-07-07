package org.sergiotan.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
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
import org.sergiotan.bean.Cita;
import org.sergiotan.bean.Doctor;
import org.sergiotan.bean.Paciente;
import org.sergiotan.db.Conexion;
import org.sergiotan.report.GenerarReporte;
import org.sergiotan.system.Principal;

public class CitasController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR,NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;    
    private Principal escenarioPrincipal;
    private ObservableList<Cita> listaCita;    
    private ObservableList<Paciente> listaPaciente;    
    private ObservableList<Doctor> listaDoctor;    
    private DatePicker fCita;
    
    @FXML private TextField txtCodigoCita;
    @FXML private TextField txtHoraCita;
    @FXML private TextField txtTratamiento;
    @FXML private TextField txtDescrip;
    @FXML private ComboBox  cmbCodPaciente;
    @FXML private ComboBox  cmbNumColegiado;
    @FXML private GridPane  grpFechas;
    @FXML private TableView  tblCitas;
    @FXML private TableColumn  colCodigoCita;
    @FXML private TableColumn  colFechaCita;
    @FXML private TableColumn  colHoraCita;
    @FXML private TableColumn  colTratamiento;
    @FXML private TableColumn  colDescrip;
    @FXML private TableColumn  colCodPaciente;
    @FXML private TableColumn  colNumColegiado;
    @FXML private Button  btnNuevo;
    @FXML private Button  btnEliminar;
    @FXML private Button  btnEditar;
    @FXML private Button  btnReporte;
    @FXML private ImageView  imgNuevo;
    @FXML private ImageView  imgEliminar;
    @FXML private ImageView  imgEditar;
    @FXML private ImageView  imgReporte;
    
    public void cargarDatos(){
        tblCitas.setItems(getCita());
        colCodigoCita.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("codigoCita"));
        colFechaCita.setCellValueFactory(new PropertyValueFactory<Cita, Date>("fechaCita"));
        colHoraCita.setCellValueFactory(new PropertyValueFactory<Cita, Time>("horaCita"));
        colTratamiento.setCellValueFactory(new PropertyValueFactory<Cita, String>("tratamiento"));
        colDescrip.setCellValueFactory(new PropertyValueFactory<Cita, String>("descripConActual"));
        colCodPaciente.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("codigoPaciente"));
        colNumColegiado.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("numeroColegiado"));
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
                    tipoDeOperacion = CitasController.operaciones.GUARDAR;
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
                    tipoDeOperacion = CitasController.operaciones.NINGUNO;
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
                if(tblCitas.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar el registro?", "Eliminar registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{call sp_EliminarCita(?)}");
                            procedimiento.setInt(1, ((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita());
                            procedimiento.execute();
                            listaCita.remove(tblCitas.getSelectionModel().getSelectedIndex());
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
        }}   

    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblCitas.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/sergiotan/image/guardar.png"));
                    imgReporte.setImage(new Image("/org/sergiotan/image/cancelar.png"));
                    activarControles();
                    txtCodigoCita.setEditable(false);
                    cmbCodPaciente.setDisable(true);
                    cmbNumColegiado.setDisable(true);
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
        Cita registro = new Cita();
        registro.setFechaCita(fCita.getSelectedDate());
        registro.setHoraCita((Time.valueOf(txtHoraCita.getText())));
        registro.setTratamiento(txtTratamiento.getText());
        registro.setDescripConActual(txtDescrip.getText());
        registro.setCodigoPaciente(((Paciente)cmbCodPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        registro.setNumeroColegiado(((Doctor)cmbNumColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_AgregarCita(?,?,?,?,?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setTime(2, registro.getHoraCita());
            procedimiento.setString(3, registro.getTratamiento());
            procedimiento.setString(4, registro.getDescripConActual());
            procedimiento.setInt(5, registro.getCodigoPaciente());
            procedimiento.setInt(6, registro.getNumeroColegiado());
            procedimiento.execute();
            listaCita.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_EditarCita(?,?,?,?,?)}");
            Cita registro = (Cita)tblCitas.getSelectionModel().getSelectedItem();
            registro.setFechaCita(fCita.getSelectedDate());
            registro.setHoraCita(Time.valueOf(txtHoraCita.getText()));
            registro.setTratamiento(txtTratamiento.getText());
            registro.setDescripConActual(txtDescrip.getText());
            procedimiento.setInt(1, registro.getCodigoCita());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setTime(3, registro.getHoraCita());
            procedimiento.setString(4, registro.getTratamiento());
            procedimiento.setString(5, registro.getDescripConActual());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("ICONO", GenerarReporte.class.getResource("/org/sergiotan/image/Patient_Male_icon-icons.com_75053.png"));
        parametros.put("FONDO", GenerarReporte.class.getResource("/org/sergiotan/image/fondoHorizontal.png"));
        GenerarReporte.mostrarReporte("ReporteCitas.jasper", "Reporte Citas", parametros);
    }      
    
    public ObservableList<Cita>getCita(){
        ArrayList<Cita> lista = new ArrayList<Cita>();
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_ListarCitas()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Cita(
                        resultado.getInt("codigoCita"),
                        resultado.getDate("fechaCita"),
                        resultado.getTime("horaCita"),
                        resultado.getString("tratamiento"),
                        resultado.getString("descripConActual"),
                        resultado.getInt("codigoPaciente"),
                        resultado.getInt("numeroColegiado")
                
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaCita = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Paciente>getPaciente(){
        ArrayList<Paciente> lista = new ArrayList<Paciente>();
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("Call sp_ListarPacientes()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Paciente(
                        resultado.getInt("codigoPaciente"),
                        resultado.getString("nombrePaciente"),
                        resultado.getString("apellidosPaciente"),
                        resultado.getString("sexo"),
                        resultado.getDate("fechaNacimiento"),
                        resultado.getString("direccionPaciente"),
                        resultado.getString("telefonoPersonal"),
                        resultado.getDate("fechaPrimeraVisita")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    
        return listaPaciente = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Doctor>getDoctor(){
        ArrayList<Doctor>lista= new ArrayList<Doctor>();
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("Call sp_ListarDoctores");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Doctor(
                        resultado.getInt("numeroColegiado"),
                        resultado.getString("nombresDoctor"),
                        resultado.getString("apellidosDoctor"),
                        resultado.getString("telefonoCOntacto"),
                        resultado.getInt("codigoEspecialidad")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDoctor = FXCollections.observableArrayList(lista);
    }

    public void seleccionarElementos(){
        if(tblCitas.getSelectionModel().getSelectedItem()== null){
            JOptionPane.showMessageDialog(null, "No se ha seleccionado nigun dato");
        }else{
            txtCodigoCita.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita()));
            fCita.selectedDateProperty().set(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getFechaCita());
            txtHoraCita.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita()));
            txtTratamiento.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getTratamiento()));
            txtDescrip.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getDescripConActual()));
            cmbCodPaciente.getSelectionModel().select(buscarPaciente(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
            cmbNumColegiado.getSelectionModel().select(buscarDoctor(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getNumeroColegiado()));

        }    
    
    }
    
    public Paciente buscarPaciente(int codigoPaciente){
        Paciente resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_BuscarPaciente(?)}");
            procedimiento.setInt(1, codigoPaciente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Paciente (registro.getInt("codigoPaciente"),
                                          registro.getString("nombrePaciente"),
                                          registro.getString("apellidosPaciente"),
                                          registro.getString("sexo"),
                                          registro.getDate("fechaNacimiento"),
                                          registro.getString("direccionPaciente"),
                                          registro.getString("telefonoPersonal"),
                                          registro.getDate("fechaPrimeraVisita"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return
                resultado;
    }
    
    public Doctor buscarDoctor(int codigoDoctor){
        Doctor resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_BuscarDoctor(?)}");
            procedimiento.setInt(1, codigoDoctor);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Doctor(registro.getInt("numeroColegiado"),
                                     registro.getString("nombresDoctor"),
                                     registro.getString("ApellidosDoctor"),
                                     registro.getString("telefonoCOntacto"),
                                     registro.getInt("codigoEspecialidad"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void limpiarControles(){
        txtCodigoCita.clear();
        txtHoraCita.clear();
        txtTratamiento.clear();
        txtDescrip.clear();
        cmbCodPaciente.getSelectionModel().clearSelection();
        cmbNumColegiado.getSelectionModel().clearSelection();
        tblCitas.getSelectionModel().clearSelection();
        fCita.setSelectedDate(null);
    }
    
    public void activarControles(){
        txtCodigoCita.setEditable(true);
        txtHoraCita.setEditable(true);
        txtTratamiento.setEditable(true);
        txtDescrip.setEditable(true);
        cmbCodPaciente.setDisable(false);
        cmbNumColegiado.setDisable(false);
        fCita.setDisable(false);
    }
    
    public void desactivarControles(){
        txtCodigoCita.setEditable(false);
        txtHoraCita.setEditable(false);
        txtTratamiento.setEditable(false);
        txtDescrip.setEditable(false);
        cmbCodPaciente.setDisable(true);
        cmbNumColegiado.setDisable(true);
        fCita.setDisable(true);        
    }
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal= escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodPaciente.setItems(getPaciente());
        cmbNumColegiado.setItems(getDoctor());
        fCita = new DatePicker(Locale.ENGLISH);
        fCita.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fCita.getCalendarView().todayButtonTextProperty().set("Today");
        fCita.getCalendarView().setShowWeeks(false);
        grpFechas.add(fCita, 3, 1);
        fCita.getStylesheets().add("/org/sergiotan/resource/DatePicker.css");        
    }
}
