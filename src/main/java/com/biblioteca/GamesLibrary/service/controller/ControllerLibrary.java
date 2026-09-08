package com.biblioteca.GamesLibrary.service.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import com.google.gson.JsonObject;

@RestController
public class ControllerLibrary {
    RestClient client = RestClient.create();

    @GetMapping("/jogo")
    public String jogo(@RequestParam String nome){
        String resposta = client.get().uri("https://api.rawg.io/api/games?key=8fef92cf54344516aecd37be2e243194&search=" + nome).retrieve().body(String.class);
        JsonObject json = JsonParser.parseString(resposta).getAsJsonObject();

        JsonArray resultados = json.get("results").getAsJsonArray();
        for(int i = 0; i < resultados.size(); i++){
            if(resultados.get(i).getAsJsonObject().get("name").getAsString().equalsIgnoreCase(nome)){
                String titulo = resultados.get(i).getAsJsonObject().get("name").getAsString();
                String lancamento = resultados.get(i).getAsJsonObject().get("released").getAsString();
                String imagem = resultados.get(i).getAsJsonObject().get("background_image").getAsString();
                return titulo + "<br>" + lancamento + "<br>" +  "<img src ='" + imagem + "' width ='300' height='200'>";
            }
        }
        return "Erro";
    }
}
