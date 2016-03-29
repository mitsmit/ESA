/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author smita
 */
public class Google {
    public static void main(String[] args) throws IOException {
        String key="011534530197018961913:-l1zu2qqdyy";
        URL url = new URL(
                "https://www.googleapis.com/customsearch/v1?key=011534530197018961913:-l1zu2qqdyy&cx=013036536707430787589:_pqjad5hr1a&q=flowers&alt=json");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }

        conn.disconnect();
    }
}
