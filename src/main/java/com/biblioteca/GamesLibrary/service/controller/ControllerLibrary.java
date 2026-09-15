package com.biblioteca.GamesLibrary.service.controller;

import com.biblioteca.GamesLibrary.service.Jogo;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import com.google.gson.JsonObject;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ControllerLibrary {
    RestClient client = RestClient.create();

    List<Jogo> biblioteca = new ArrayList<>();

    @GetMapping("/jogo")
    public String jogo(@RequestParam String nome, Model model) {
        String slugNome = nome.toLowerCase().replace(":", "").replace(" ", "-");

        String resposta = client.get().uri("https://api.rawg.io/api/games?key=8fef92cf54344516aecd37be2e243194&search=" + nome).retrieve().body(String.class);
        JsonObject jogo = JsonParser.parseString(resposta).getAsJsonObject();

        JsonArray resultados = jogo.get("results").getAsJsonArray();
        JsonObject jogoEncontrado = null;

        for (int i = 0; i < resultados.size(); i++) {
            JsonObject jogoAtual = resultados.get(i).getAsJsonObject();


            if (jogoAtual.get("slug").getAsString().equalsIgnoreCase(slugNome)) {
                jogoEncontrado = jogoAtual;
                break;
            }
        }
        if (jogoEncontrado == null) {
            return null;
        }
        int id = jogoEncontrado.get("id").getAsInt();
        String detalhes = client.get()
                .uri("https://api.rawg.io/api/games/" + id + "?key=8fef92cf54344516aecd37be2e243194")
                .retrieve()
                .body(String.class);

        JsonObject detalhesJogo = JsonParser.parseString(detalhes).getAsJsonObject();

        String titulo = detalhesJogo.get("name").getAsString();
        String lancamento = detalhesJogo.get("released").getAsString();
        String imagem = detalhesJogo.get("background_image").getAsString();
        String descricao = detalhesJogo.get("description").getAsString();
        descricao = descricao.split("Español")[0];

        Jogo novoJogo = new Jogo();
        novoJogo.setNome(titulo);
        novoJogo.setLancamento(lancamento);
        novoJogo.setImagem(imagem);
        novoJogo.setDescricao(descricao);
        if (!biblioteca.contains(novoJogo)) {
            biblioteca.add(novoJogo);
        }
        model.addAttribute("jogo", novoJogo);
        return "jogo";
    }

    @GetMapping("/biblioteca")
    public String biblioteca(Model model) {
        model.addAttribute("biblioteca", biblioteca);
        return "biblioteca";
    }
}
