package br.com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name= "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nome;
    private String idioma;
    private Integer quantidadeDeDownloads;

    @ManyToOne
    private Autor autor;

    public Livro(){}
    public Livro(DadosLivro dadosLivro) {
        this.nome = dadosLivro.nomeDoLivro();
        this.idioma = String.join(", ", dadosLivro.idiomas());  // Converte a lista em uma string
        this.quantidadeDeDownloads = dadosLivro.quantidadeDeDownloads();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getQuantidadeDeDownloads() {
        return quantidadeDeDownloads;
    }

    public void setQuantidadeDeDownloads(Integer quantidadeDeDownloads) {
        this.quantidadeDeDownloads = quantidadeDeDownloads;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }


    @Override
    public String toString() {
        return "\n---------------------------------------"+
                "\nNome: " + nome +
                "\nIdioma: " + idioma +
                "\nAutor: " + autor.getNome() +
                "\nQuantidade De Downloads: " + quantidadeDeDownloads;
    }
}