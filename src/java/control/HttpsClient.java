/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.io.*;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;


public class HttpsClient{
	
private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public static String readJsonFromUrl(String token) throws IOException {
    try (InputStream is = new URL("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token="+token).openStream()) {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      return jsonText;
    }
  }

  public static void main(String[] args) throws IOException {
    
    System.out.println(readJsonFromUrl("eyJhbGciOiJSUzI1NiIsImtpZCI6IjIyZmYxMGI5NTY0NDQzYTJmNmJhZWJiYzhhOGIyOTUzZWZiNmVkYzEifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhdWQiOiIyNTM3MzI5ODAxMjQtdmpwNWsxbWJmanAyNmY0OGZuOTF1OGR1NzBhcXAyYmEuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDA4OTg3MzY5NzA5NTg4NDU1MzkiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXpwIjoiMjUzNzMyOTgwMTI0LTlvaGprZjk0aDNuaGVrcWkzdGpkYmFhcTU5MmVxa3FxLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiZW1haWwiOiJhbGV4YW5kcmVtYWVzYXRvQGdtYWlsLmNvbSIsImlhdCI6MTQ1OTE5MDQwNCwiZXhwIjoxNDU5MTk0MDA0LCJuYW1lIjoiQWxleGFuZHJlIE1hZXNhdG8iLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDQuZ29vZ2xldXNlcmNvbnRlbnQuY29tLy1UU2lIcTBPRWZ1ay9BQUFBQUFBQUFBSS9BQUFBQUFBQURLay9mUHNiVlY1VFdRRS9zOTYtYy9waG90by5qcGciLCJnaXZlbl9uYW1lIjoiQWxleGFuZHJlIiwiZmFtaWx5X25hbWUiOiJNYWVzYXRvIiwibG9jYWxlIjoicHQifQ.hWQcdIsJaiReImo9ooO74mjMmg1pXbp5qqEK8k1AEhTRDP3TdhjC36JDEOPoA8g774YxdZpZ5AbyjWnlZ4AwE5-uNgiTHlTGUk3kW_kIopRdl4Z2EXKkX4biadJbbB42fxBUVsQFO2DiI_UaNEfJg9RRuRR5kTC-7wmRXzb_BJ-eVQt1E2ySYWhbl29P2AsHCqtJC-jnnMvCUR7tmJlYRlOlHhcf1viP8gf4OJXZaxadHzfXEvRRbTIGv003e9TSNaQiZ-e_uENZyIXBGcyGlSJh_BdDcS1sP2OTgsToKJReOY8umDGvuC0hX3NtVtiBclnBHxkCQMcu0OOTivEKxQ"));
    
  }
	
}