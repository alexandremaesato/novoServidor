/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Guilherme
 */
public class Notificacao {
    
    private int notificacaoid;
    private int entidadeid;
    private boolean visualizada;

    public int getNotificacaoid() {
        return notificacaoid;
    }

    public void setNotificacaoid(int notificacaoid) {
        this.notificacaoid = notificacaoid;
    }

    public int getEntidadeid() {
        return entidadeid;
    }

    public void setEntidadeid(int entidadeid) {
        this.entidadeid = entidadeid;
    }

    public boolean isVisualizada() {
        return visualizada;
    }

    public void setVisualizada(boolean visualizada) {
        this.visualizada = visualizada;
    }
}
