package com.letscode.campeonato;

import com.letscode.campeonato.services.Processador;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        if(args.length != 2){
            System.err.println("Você precisa passar dois argumentos, o caminho para o arquivo CSV e o caminho do diretório de saída.");
        }else{
            Processador processador = new Processador(args[0]);
            processador.processaPartidas();
            processador.geraResultado(args[1]);
        }

    }
}
