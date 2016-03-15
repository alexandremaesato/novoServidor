/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;

import jdk.nashorn.internal.parser.JSONParser;
import model.AutenticacaoDao;
import model.Empresa;
import model.Pessoa;
import org.omg.DynamicAny.NameValuePair;


import utilitarios.Criptografia;



/**
 *
 * @author Alexandre
 */
public class TestesDoAle {
    public static void main(String args[]) throws SQLException{
        
        Empresa emp = new Empresa();
        emp.setNomeEmpresa("Empresa");
        Pessoa pessoa =  new Pessoa();
        pessoa.setNome("Nome de Teste");
         Gson g = new Gson();

   
    }
    
    
   
    
}
