
package org.sergiotan.bean;

public class Usuario {
    private int codigoUsuario;
    private String nombreUsuario;
    private String apellidosUsuario;
    private String usuarioLogin;
    private String contrasena;

    public Usuario() {
    }

    public Usuario(int codigoUsuario, String nombreUsuario, String apellidosUsuario, String usuarioLogin, String contrasena) {
        this.codigoUsuario = codigoUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidosUsuario = apellidosUsuario;
        this.usuarioLogin = usuarioLogin;
        this.contrasena = contrasena;
    }

    public int getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(int codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidosUsuario() {
        return apellidosUsuario;
    }

    public void setApellidosUsuario(String apellidosUsuario) {
        this.apellidosUsuario = apellidosUsuario;
    }

    public String getUsuarioLogin() {
        return usuarioLogin;
    }

    public void setUsuarioLogin(String usuarioLogin) {
        this.usuarioLogin = usuarioLogin;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    
}
