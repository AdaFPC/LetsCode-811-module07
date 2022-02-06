package com.letscode.campeonato.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@JsonPropertyOrder({
        "Time",
        "Vitorias",
        "Empates",
        "Derrotas",
        "Pontos"
})
public class Classificacao {
    @JsonProperty("Time")
    private final String time;
    @JsonProperty("Vitorias")
    private int vitorias = 0;
    @JsonProperty("Empates")
    private int empates = 0;
    @JsonProperty("Derrotas")
    private int derrotas = 0;
    @JsonProperty("Pontos")
    private int pontos = 0;

    public void adicionarVitoria(){
        vitorias++;
        pontos += 3;
    }

    public void adicionarEmpates(){
        empates++;
        pontos ++;
    }

    public void adicionarDerrotas(){
        derrotas++;
    }

}
