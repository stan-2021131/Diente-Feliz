
package org.sergiotan.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.sergiotan.bean.Usuario;
import org.sergiotan.db.Conexion;
import org.sergiotan.system.Principal;

public class UsuarioController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NINGUNO, GUARDAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    @FXML private TextField txtCodUsuario;
    @FXML private TextField txtNombresUsuario;
    @FXML private TextField txtApellidosUsuario;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtContrasena;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnEliminar.setDisable(true);
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
               limpiarControles();
               activarControles();
               btnNuevo.setText("Guardar");
               btnEliminar.setText("Cancelar");
               imgNuevo.setImage(new Image("/org/sergiotan/image/guardar.png"));
               imgEliminar.setImage(new Image("/org/sergiotan/image/cancelar.png"));               
               tipoDeOperacion = operaciones.GUARDAR;
            break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                JOptionPane.showMessageDialog(null, "Se ha agregado un nuevo usuario");
                imgNuevo.setImage(new Image("/org/sergiotan/image/add_plus_interface_icon_181584.png"));
                imgEliminar.setImage(new Image("/org/sergiotan/image/delete_remove_close_checkbox_check_icon_181586.png"));
                tipoDeOperacion = operaciones.NINGUNO;                
            break;
        }
    }
    
    public void eliminar(){}

    public void guardar(){
        Usuario registro = new Usuario();
        registro.setNombreUsuario(txtNombresUsuario.getText());
        registro.setApellidosUsuario(txtApellidosUsuario.getText());
        registro.setUsuarioLogin(txtUsuario.getText());
        registro.setContrasena(txtContrasena.getText());
        try{
            PreparedStatement procedimiento = Conexion.getIntance().getConexion().prepareCall("{Call sp_AgregarUsuario(?,?,?,?)}");
            procedimiento.setString(1, registro.getNombreUsuario());
            procedimiento.setString(2, registro.getApellidosUsuario());
            procedimiento.setString(3, registro.getUsuarioLogin());
            procedimiento.setString(4, registro.getContrasena());
            procedimiento.execute();
            ventanaLogin();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
        txtCodUsuario.setEditable(false);
        txtNombresUsuario.setEditable(false);
        txtApellidosUsuario.setEditable(false);
        txtUsuario.setEditable(false);
        txtContrasena.setEditable(false);
        btnEliminar.setDisable(true);
    }
    public void activarControles(){
        txtCodUsuario.setEditable(false);
        txtNombresUsuario.setEditable(true);
        txtApellidosUsuario.setEditable(true);
        txtUsuario.setEditable(true);
        txtContrasena.setEditable(true);
        btnEliminar.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodUsuario.clear();
        txtNombresUsuario.clear();
        txtApellidosUsuario.clear();
        txtUsuario.clear();
        txtContrasena.clear();
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
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
}
