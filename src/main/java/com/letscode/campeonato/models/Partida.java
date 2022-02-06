package com.letscode.campeonato.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
        "time_1(mandante)",
        "time2(visitante)",
        "placar_time_1",
        "placar_time_2",
        "data"
})
public class Partida {
    @JsonProperty("time_1(mandante)")
    private String mandante;
    @JsonProperty("time2(visitante)")
    private String visitante;
    @JsonProperty("placar_time_1")
    private int placarMandate;
    @JsonProperty("placar_time_2")
    private int placarVisitante;
    @JsonProperty("data")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;

    public String getVencedor(){
        if (placarMandate > placarVisitante){
            return mandante;
        }else if(placarMandate < placarVisitante){
            return visitante;
        }else {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("%s: %s %d x %d %s",
                data.format(DateTimeFormatter.ofPattern("dd/MM/yy")),
                mandante,
                placarMandate,
                placarVisitante,
                visitante);
    }
}
