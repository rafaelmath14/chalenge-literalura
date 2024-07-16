package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivro(@JsonAlias("title")String nomeDoLivro,
                         @JsonAlias("download_count") Integer quantidadeDeDownloads,
                         @JsonAlias("languages") List<String> idiomas) {

}
