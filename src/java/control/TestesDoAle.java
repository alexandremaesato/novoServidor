/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;

import jdk.nashorn.internal.parser.JSONParser;
import model.AutenticacaoDao;
import utilitarios.Criptografia;


/**
 *
 * @author Alexandre
 */
public class TestesDoAle {
    public static void main(String args[]) throws SQLException{
//        String login = null;
//        String senha;
//        String metodo;
//        String criptografia;
//        try {
//            Autenticacao.Autenticar(login, senha, metodo, criptografia);
//        } catch (NoSuchAlgorithmException ex) {
//            Logger.getLogger(TestesDoAle.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(TestesDoAle.class.getName()).log(Level.SEVERE, null, ex);
//        }
        String senha = null;
        try {
            senha = Criptografia.gerarCriptografia("qwe123@");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(TestesDoAle.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TestesDoAle.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(senha);
        
        String val = "[813B889BFB73F49A7EE571283662B6F2374F19A49C03C60674E41221EAD4D216, {\"cnpj\":\"\",\"descricao\":\"\",\"empresaId\":0,\"nome\":\"\"}, 1457553035642]";
        Gson gson = new Gson();
       
        
                
       
        
        
    }
    
    public void login(String login, String senha, String metodoHttp, String request, String sharedKey){
        
    }
    
 
   
    
}
