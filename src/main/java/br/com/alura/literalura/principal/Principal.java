package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.*;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.service.ConsumoApi;
import br.com.alura.literalura.service.ConverterDados;
import org.yaml.snakeyaml.util.UriEncoder;


import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi requisicao = new ConsumoApi();
    private AutorRepository repositorioAutor;
    private LivroRepository repositorioLivro;
    private List<Livro> livro = new ArrayList<>();
    private ConverterDados conversor = new ConverterDados();
    private final String ADDRESS = "https://gutendex.com/books?search=";

    public Principal(AutorRepository repositorioAutor, LivroRepository repositorioLivro){
        this.repositorioAutor = repositorioAutor;
        this.repositorioLivro = repositorioLivro;
    }

    public void ExibirMenu(){
        String menu = """
                **********************************************
                1 - Buscar livro pelo titulo
                2 - Listar livros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos em determinado ano
                5 - Listar livros em determinado idioma
                6 - Top 10 livros
                7 - Buscar autores por nome
                8 - Media de downloads por autor
                
                0 - Sair
                **********************************************
                """;
        var opcao = -1;
        while (opcao != 0){
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao){
                case 1:
                    buscarNovoLivro();
                    break;
                case 2:
                    buscarLivrosRegistrados();
                    break;
                case 3:
                    buscarAutoresRegistrados();
                    break;
                case 4:
                    buscarAutoresVivosPorAno();
                    break;
                case 5:
                    buscarLivrosPorIdioma();
                    break;
                case 6:
                    buscarTop10();
                    break;
                case 7:
                    buscarAutorPorNome();
                    break;
                case 8:
                    mediaDeDownlaodsPorAutor();
                    break;
                case 0:
                    System.out.println("Sair");
                    break;
                default:
                    System.out.println("\n***Opção Inválida***\n");
            }
        }

    }
    private void buscarNovoLivro() {
        System.out.println("\nPesquise por um título");
        var buscaDoUsuario = leitura.nextLine();
        var dados = requisicao.consumo(ADDRESS+ UriEncoder.encode(buscaDoUsuario));
        salvar(dados);

    }

    private void salvar(String dados) {
        try {
            DadosLivro dadosLivro = conversor.getData(dados, DadosLivro.class);
            DadosAutor dadosAutor = conversor.getData(dados, DadosAutor.class);

            Autor autor = new Autor(dadosAutor);
            if (!repositorioAutor.existsByNome(autor.getNome())) {
                autor = repositorioAutor.save(autor);
            } else {
                autor = repositorioAutor.findByNome(autor.getNome());
            }

            Livro livro = new Livro(dadosLivro);
            livro.setAutor(autor);
            if (!repositorioLivro.existsByNome(livro.getNome())) {
                repositorioLivro.save(livro);
            }

            System.out.println("Livro salvo: " + livro);
        } catch (Exception e) {
            System.out.println("\n\n*** Erro ao salvar o livro no banco de dados, tente novamente.***\n\n");
            e.printStackTrace();
        }
    }

    private void buscarLivrosRegistrados() {
        var bucasBd = repositorioLivro.findAll();
        if(!bucasBd.isEmpty()){
            System.out.println("\nLivros cadastrados no banco de dados: ");
            bucasBd.forEach(System.out::println);
        }else{
            System.out.println("\nNenhum livro encontrado no banco de dados!");
        }
    }

    private void buscarAutoresRegistrados() {
        var buscaBd = repositorioAutor.findAll();
        if(!buscaBd.isEmpty()){
            System.out.println("\nAutores cadastrados no banco de dados:");
            buscaBd.forEach(System.out::println);
        }else{
            System.out.println("\nNenhum autor encontrado no banco de dados!");
        }
    }

    private void buscarAutoresVivosPorAno() {
        System.out.println("\nQual ano deseja pesquisar?");
        var anoSelecionado = leitura.nextInt();
        leitura.nextLine();
        var buscaAutoresNoDb = repositorioAutor.buscarPorAnoDeFalecimento(anoSelecionado);
        if(!buscaAutoresNoDb.isEmpty()){
            System.out.println("\n\nAtores vivos no ano de: " + anoSelecionado);
            buscaAutoresNoDb.forEach(System.out::println);
        }else {
            System.out.println("\nNenhum autor encontrado para esta data!");
        }
    }

    private void buscarLivrosPorIdioma() {
        var idiomasCadastrados = repositorioLivro.bucasidiomas();
        System.out.println("\nIdiomas disponíveis:");
        idiomasCadastrados.forEach(System.out::println);
        System.out.println("\nSelecione um dos idiomas:\n");
        var idiomaSelecionado = leitura.nextLine();
        repositorioLivro.buscarPorIdioma(idiomaSelecionado).forEach(System.out::println);
    }

    private void buscarTop10() {
        var top10 = repositorioLivro.findTop10ByOrderByQuantidadeDeDownloadsDesc();
        top10.forEach(System.out::println);
    }

    private void buscarAutorPorNome() {
        System.out.println("Qual o nome do autor?");
        var pesquisa = leitura.nextLine();
        var autor = repositorioAutor.encontrarPorNome(pesquisa);
        if (!autor.isEmpty()){
            autor.forEach(System.out::println);
        }else{
            System.out.println("*** Autor não encontrado! ***");
        }
    }

    private void mediaDeDownlaodsPorAutor() {
        System.out.println("Qual autor deseja buscar?");
        var pesquisa = leitura.nextLine();
        var test = repositorioLivro.encontrarLivrosPorAutor(pesquisa);
        DoubleSummaryStatistics media = test.stream()
                .mapToDouble(Livro::getQuantidadeDeDownloads)
                .summaryStatistics();
        System.out.println("Média de Downloads: "+ media.getAverage());
    }


}

