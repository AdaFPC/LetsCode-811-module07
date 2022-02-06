package com.letscode.campeonato.services;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.letscode.campeonato.models.Classificacao;
import com.letscode.campeonato.models.Partida;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.*;

public class Processador {
    private final CsvMapper mapper = new CsvMapper();
    private final CsvSchema headers = CsvSchema.emptySchema().withHeader().withColumnSeparator(';');
    private final Set<Partida> partidas = new LinkedHashSet<>();
    private final Map<String,List<Partida>> partidasPorTime = new HashMap<>();
    private final Map<String, Classificacao> campeonato = new HashMap<>();


    public Processador(String arquivoCsv) {
        mapper.enable(CsvParser.Feature.SKIP_EMPTY_LINES);
        mapper.findAndRegisterModules();
        carregaArquivoCsv(arquivoCsv);
    }

    public void processaPartidas(){
        partidas.forEach(partida -> {
            adicionaPartidaTime(partida.getMandante(), partida);
            adicionaPartidaTime(partida.getVisitante(), partida);
            contabilizaClassificacao(partida.getMandante(), partida);
            contabilizaClassificacao(partida.getVisitante(), partida);
        });
    }

    public void geraResultado(String diretorioSaida){
        criaDiretorioSaida(diretorioSaida);
        gerapartidasporTime(diretorioSaida);
        geraClassificacao(diretorioSaida);
    }

    private void geraClassificacao(String diretorioSaida) {
        List<Classificacao> listadeClassificacao = new ArrayList<>(campeonato.values());
        listadeClassificacao.sort(Comparator.comparing(Classificacao::getPontos).reversed());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(diretorioSaida + "/Classificacao.csv"))){
           CsvSchema schemaClassificacao = mapper.schemaFor(Classificacao.class).withHeader().withoutEscapeChar().withoutQuoteChar();
            SequenceWriter sequenceWriter = mapper.writerWithSchemaFor(Classificacao.class).with(schemaClassificacao).writeValues(writer);
            sequenceWriter.writeAll(listadeClassificacao);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void gerapartidasporTime(String diretorioSaida) {
        partidasPorTime.forEach((time, partidasTime)->{
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(diretorioSaida + "/"+time+".txt"))){
                for (Partida partida : partidasTime) {
                    writer.append(partida.toString());
                    writer.append("\n");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    private void criaDiretorioSaida(String diretorioSaida) {
        File diretorio = new File(diretorioSaida);
        if(!diretorio.exists()){
            diretorio.mkdirs();
        }
    }

    private void contabilizaClassificacao(String time, Partida partida) {
        Classificacao classificacaoTime = new Classificacao(time);
        if(campeonato.containsKey(time)){
            classificacaoTime = campeonato.get(time);
        }

        if(partida.getVencedor() == null){
            classificacaoTime.adicionarEmpates();
        }else {
            if(partida.getVencedor().equals(time)){
                classificacaoTime.adicionarVitoria();
            }else{
                classificacaoTime.adicionarDerrotas();
            }
        }

        campeonato.put(time, classificacaoTime);
    }

    private void adicionaPartidaTime(String time, Partida partida) {
        List<Partida>partidasTime = new ArrayList<>();
        if(partidasPorTime.containsKey(time)){
            partidasTime = partidasPorTime.get(time);
        }
        partidasTime.add(partida);
        partidasTime.sort(
                Comparator.comparing(Partida::getData)
                        .thenComparing(Partida::getMandante)
                        .thenComparing(Partida::getVisitante)
        );
        partidasPorTime.put(time, partidasTime);
    }

    private void carregaArquivoCsv(String arquivoCsv){

        File fileCsv = new File(arquivoCsv);
        if(fileCsv.exists()){
            try {
                MappingIterator<Partida> iterator = mapper
                        .readerFor(Partida.class)
                        .with(headers)
                        .readValues(Paths.get(arquivoCsv).normalize().toFile());

                partidas.addAll(iterator.readAll());

            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            System.err.println(arquivoCsv + " não encontrado!");
        }


    }
}
