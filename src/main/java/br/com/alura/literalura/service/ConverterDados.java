package br.com.alura.literalura.service;

import br.com.alura.literalura.model.DadosAutor;
import br.com.alura.literalura.model.DadosLivro;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverterDados implements IConverteDados{
    private ObjectMapper mapper = new ObjectMapper();


    @Override
    public <T> T getData(String json, Class<T> classe) {
        try {
            JsonNode node = mapper.readTree(json);
            if (classe == DadosLivro.class) {
                var s = node.get("results").get(0);
                return mapper.treeToValue(s, classe);
            } else if (classe == DadosAutor.class) {
                var s = node.get("results").get(0).get("authors").get(0);
                return mapper.treeToValue(s, classe);
            } else {
                return mapper.readValue(json, classe);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

