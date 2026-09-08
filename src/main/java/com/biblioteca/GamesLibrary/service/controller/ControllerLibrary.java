package com.biblioteca.GamesLibrary.service.controller;

import com.biblioteca.GamesLibrary.service.Jogo;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ControllerLibrary {
    RestClient client = RestClient.create();

    List<Jogo> biblioteca = new ArrayList<>();

    @GetMapping("/jogo")
    public Jogo jogo(@RequestParam String nome) {
        String slugNome = nome.toLowerCase().replace(":", "").replace(" ", "-");

        String resposta = client.get().uri("https://api.rawg.io/api/games?key=8fef92cf54344516aecd37be2e243194&search=" + nome).retrieve().body(String.class);
        JsonObject jogo = JsonParser.parseString(resposta).getAsJsonObject();

        JsonArray resultados = jogo.get("results").getAsJsonArray();
        for (int i = 0; i < resultados.size(); i++) {
            JsonObject jogoAtual = resultados.get(i).getAsJsonObject();

            if (jogoAtual.get("slug").getAsString().equalsIgnoreCase(slugNome)) {

                String titulo = jogoAtual.get("name").getAsString();
                String lancamento = jogoAtual.get("released").getAsString();
                String imagem = jogoAtual.get("background_image").getAsString();

                Jogo novoJogo = new Jogo();
                novoJogo.setNome(nome);
                novoJogo.setLancamento((lancamento));
                novoJogo.setImagem(imagem);

                biblioteca.add(novoJogo);

                System.out.println("Realizado com sucesso!");

                return novoJogo;
            }
        }
        return null;
    }

    @GetMapping("/biblioteca")
    public List<Jogo> biblioteca(){
        return biblioteca;
    }
}
