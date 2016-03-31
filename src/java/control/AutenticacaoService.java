/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.sun.xml.ws.security.Token;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.AutenticacaoDao;
import utilitarios.Criptografia;
import utilitarios.HMAC;

/**
 *
 * @author Alexandre
 */
public class AutenticacaoService {

    public static boolean Autenticar(String login, String senha, String metodo, String criptografia) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
        if (AutenticacaoDao.autenticar(login, senha)) {

            String token = AutenticacaoDao.getToken(login);
            AutenticacaoDao.limpaToken(login);
            String id = login + senha + metodo + token;

            if (Criptografia.gerarCriptografia(id) == criptografia) {
                return true;
            }

        }
        return false;
    }

//    public String loginGetToken(String login, String senha) throws SQLException, NoSuchAlgorithmException {
//        if (AutenticacaoDao.autenticar(login, senha)) {
//            String token = getToken();
//            AutenticacaoDao.gravarToken(tokenInfo, token);
//            return token;
//        }
//        return null;
//    }

    public static String getToken() throws NoSuchAlgorithmException {
        String secretSeed = convertStringToHex("12345678901234567890");
        String result = null;
        long timeWindow = 60L;
        long exactTime = System.currentTimeMillis() / 1000L;
        long preRounded = (long) (exactTime / timeWindow);
        String roundedTime = Long.toHexString(preRounded).toUpperCase();
        while (roundedTime.length() < 16) {
            roundedTime = "0" + roundedTime;
        }
        byte[] hash = HMAC.hmac(hexStr2Bytes(secretSeed), hexStr2Bytes(roundedTime));
        int offset = hash[hash.length - 1] & 0xf;
        int otp = ((hash[offset + 0] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);
        otp = otp % 1000000;
        result = Integer.toString(otp);
        while (result.length() < 6) {
            result = "0" + result;
        }
        return result;
    }

    private static byte[] hexStr2Bytes(String hex) {//Responsavel por Converter HEX em Bytes
        byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();
        byte[] ret = new byte[bArray.length - 1];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = bArray[i + 1];
        }
        return ret;
    }

    public static String convertStringToHex(String str) {//Responsavel por converter String para HEX

        char[] chars = str.toCharArray();

        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            hex.append(Integer.toHexString((int) chars[i]));
        }

        return hex.toString();
    }
    

}
