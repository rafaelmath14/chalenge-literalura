package br.com.alura.literalura.service;

public interface IConverteDados {
    <T> T getData(String json, Class<T> classe);
}

