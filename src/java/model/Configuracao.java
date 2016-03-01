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
public class Configuracao {
    
    private int configuracaoid;
    private char publico;
    private char notificacoes_produtos_add;
    private char notificacao_comentarios_empresa;
    private char notificacao_comentario_produtos;
    private char notificacao_comentarios_resposta;

    public int getConfiguracaoid() {
        return configuracaoid;
    }

    public void setConfiguracaoid(int configuracaoid) {
        this.configuracaoid = configuracaoid;
    }

    public char getPublico() {
        return publico;
    }

    public void setPublico(char publico) {
        this.publico = publico;
    }

    public char getNotificacoes_produtos_add() {
        return notificacoes_produtos_add;
    }

    public void setNotificacoes_produtos_add(char notificacoes_produtos_add) {
        this.notificacoes_produtos_add = notificacoes_produtos_add;
    }

    public char getNotificacao_comentarios_empresa() {
        return notificacao_comentarios_empresa;
    }

    public void setNotificacao_comentarios_empresa(char notificacao_comentarios_empresa) {
        this.notificacao_comentarios_empresa = notificacao_comentarios_empresa;
    }

    public char getNotificacao_comentario_produtos() {
        return notificacao_comentario_produtos;
    }

    public void setNotificacao_comentario_produtos(char notificacao_comentario_produtos) {
        this.notificacao_comentario_produtos = notificacao_comentario_produtos;
    }

    public char getNotificacao_comentarios_resposta() {
        return notificacao_comentarios_resposta;
    }

    public void setNotificacao_comentarios_resposta(char notificacao_comentarios_resposta) {
        this.notificacao_comentarios_resposta = notificacao_comentarios_resposta;
    }
}
