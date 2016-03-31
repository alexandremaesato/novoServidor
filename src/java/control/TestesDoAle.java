/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.xml.wss.impl.misc.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.xml.bind.DatatypeConverter;

import jdk.nashorn.internal.parser.JSONParser;
import model.AutenticacaoDao;
import model.Empresa;
import model.Pessoa;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.omg.DynamicAny.NameValuePair;


import utilitarios.Criptografia;



/**
 *
 * @author Alexandre
 */
public class TestesDoAle {
    public static void main(String args[]) throws SQLException, Base64DecodingException{
        byte[] encodedHelloBytes = DatatypeConverter.parseBase64Binary("dXNlcjpwYXNzd29yZA==");
            String decodeString = new String(encodedHelloBytes, StandardCharsets.UTF_8) ;
    
            //String decodeString = Base64.getDecoder().decode(authToken).toString();
            StringTokenizer tokenizer = new StringTokenizer(decodeString, ":");
            String username = tokenizer.nextToken();
            String password = tokenizer.nextToken();
            
            String convertido = Base64.encode("qwe123@".getBytes());
            
     System.out.println("Convertido: "+convertido);
     System.out.println("decode : "+decodeString);
    }
    
    
   
    
}
