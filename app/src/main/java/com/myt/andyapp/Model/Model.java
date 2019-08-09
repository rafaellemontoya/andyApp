package com.myt.andyapp.Model;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class Model {
    private boolean isSelected;
    private String animal, nota;

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
}