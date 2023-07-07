/*
Nombre: Sergio Estuardo Tan Coromac
Carné: 2021131
Código Técnico: IN5AM
Fecha de Creación: 5/04/2022
Modificaciones:5/04/2022
               18/04/2022
               22/04/2022
               25/04/2022
               3/05/2022
               7/05/2022
               23/05/2022
               24/05/2022
               25/05/2022
               27/05/2022
               30/05/2022
               2/06/2022
               6/06/2022
               13/06/2022
               16/06/2022
Fecha de Entrega: 16/06/2022               
 */
package org.sergiotan.system;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.sergiotan.controller.CitasController;
import org.sergiotan.controller.DetalleRecetasController;
import org.sergiotan.controller.DoctoresController;
import org.sergiotan.controller.EspecialidadesController;
import org.sergiotan.controller.LoginController;
import org.sergiotan.controller.MedicamentosController;
import org.sergiotan.controller.MenuPrincipalController;
import org.sergiotan.controller.PacientesController;
import org.sergiotan.controller.ProgramadorController;
import org.sergiotan.controller.RecetasController;
import org.sergiotan.controller.UsuarioController;

public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/sergiotan/view/";
    private Stage escenarioPrincipal;
    private Scene escena;   
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Clinica Diente Feliz");
        escenarioPrincipal.getIcons().add(new Image("/org/sergiotan/image/icono.png"));
        ventanaLogin();
        //menuPrincipal();
        escenarioPrincipal.show();
    }

    public void ventanaLogin(){
        try{
            LoginController ventanaLogin = (LoginController) cambiarEscena("LoginView.fxml", 415, 415);
            ventanaLogin.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
    public void ventanaUsuario(){
        try{
            UsuarioController ventanaUsuario = (UsuarioController) cambiarEscena("UsuariosView.fxml", 613, 395);
            ventanaUsuario.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
    
    public void menuPrincipal(){
        try{
            MenuPrincipalController ventanaMenu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 486,485);
            ventanaMenu.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProgramador(){
        try{
            ProgramadorController ventanaProgramador = (ProgramadorController)cambiarEscena("ProgramadorView.fxml", 530, 385);
            ventanaProgramador.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaPacientes(){
        try{
            PacientesController ventanaPacientes = (PacientesController)cambiarEscena("PacientesView.fxml", 941, 395);
            ventanaPacientes.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaDoctores(){
        try{
            DoctoresController ventanaDoctores = (DoctoresController) cambiarEscena("DoctoresView.fxml", 681, 395);
            ventanaDoctores.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEspecialidades(){
        try{
            EspecialidadesController ventanaEspecialidades = (EspecialidadesController) cambiarEscena("EspecialidadesView.fxml", 613, 395);
            ventanaEspecialidades.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaMedicamentos(){
        try{
            MedicamentosController ventanaMedicamentos = (MedicamentosController) cambiarEscena("MedicamentosView.fxml", 613, 395);
            ventanaMedicamentos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void ventanaRecetas(){
        try{
            RecetasController ventanaRecetas = (RecetasController) cambiarEscena("RecetasView.fxml", 681, 395);
            ventanaRecetas.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void ventanaDetalleRecetas(){
        try{
            DetalleRecetasController ventanaDetalleRecetas = (DetalleRecetasController) cambiarEscena("DetallesRecetaView.fxml", 681, 395);
            ventanaDetalleRecetas.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void ventanaCitas(){
        try{
            CitasController ventanaCitas = (CitasController) cambiarEscena("CitasView.fxml", 941, 395);
            ventanaCitas.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
  
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
    }
}
