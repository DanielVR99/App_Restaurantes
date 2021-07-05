package com.example.proyectofinal;

public class ResenasDP {
    private String calif, text;

    public String getCalif() {
        return calif;
    }

    public void setCalif(String calif) {
        this.calif = calif;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toString(){
        return calif+" "+text;
    }
}
