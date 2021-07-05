package com.example.proyectofinal;

public class LocalesDP {
    private String uID, calif, ciudad, creador, disca, nombre, tipo, id, ubi,resena;

    public String getuID() {
        return uID;
    }

    public void setuID(String calif) {
        this.uID = calif;
    }

    public String getResena() {
        return resena;
    }

    public void setResena(String calif) {
        this.resena = calif;
    }

    public String getCalif() {
        return calif;
    }

    public void setCalif(String calif) {
        this.calif = calif;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String CDMX) {
        this.ciudad = CDMX;
    }

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }


    public String getDisca() {
        return disca;
    }

    public void setDisca(String disca) {
        this.disca = disca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUbi() {
        return ubi;
    }

    public void setUbi(String ubi) {
        this.ubi = ubi;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    //no incluye id
    public String toString(){
        return uID+"$"+calif+"$"+ciudad+"$"+ creador+"$"+ disca+"$"+ nombre+"$"+ tipo+"$"+ ubi;
    }
}
