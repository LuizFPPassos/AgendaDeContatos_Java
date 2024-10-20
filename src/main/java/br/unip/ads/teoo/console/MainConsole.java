package br.unip.ads.teoo.console;

import br.unip.ads.teoo.core.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class MainConsole {

    static Scanner scanner;
    static IContatoService contatoService;

    public static void main(String[] args) throws IOException, SQLException {

        try{
            contatoService = new ContatoService();
        } catch (IOException e) {
            System.out.println("Erro ao iniciar o sistema: " + e.getMessage());
        }

        scanner = new Scanner(System.in);

        // Buscando contatos
        System.out.println("\nLista de contatos:");
        List<Contato> contatos = contatoService.listarContatos();
        // Ordenando a lista de contatos por id
        Collections.sort(contatos, Comparator.comparingInt(Contato::getId));
        for (Contato c : contatos) {
            System.out.println(c.toString());
        }

        int menu = 0;
        do{
            System.out.println("Menu:");
            System.out.println("1 - Listar contatos");
            System.out.println("2 - Adicionar contato");
            System.out.println("3 - Editar contato");
            System.out.println("4 - Remover contato");
            System.out.println("5 - Buscar contato por id");
            System.out.println("6 - Buscar contato por nome");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            menu = scanner.nextInt();
            scanner.nextLine();
            switch (menu){
                case 0:
                    break;
                case 1:
                    // Listando contatos
                    System.out.println("\nLista de contatos:");
                    List<Contato> contatosBusca = contatoService.listarContatos();
                    // Ordenando a lista de contatos por id
                    Collections.sort(contatosBusca, Comparator.comparingInt(Contato::getId));
                    for (Contato c : contatosBusca) {
                        System.out.println(c.toString());
                    }
                    break;
                case 2:
                    // Adicionando um novo contato
                    Contato contato = new Contato();

                    contato = editarNome(contato);
                    contato = editarEmail(contato);
                    contato = editarDataNascimento(contato);

                    String adicionarOutroEndereco = "S";
                    while (adicionarOutroEndereco.equalsIgnoreCase("S")){
                        Endereco endereco;
                        endereco = new Endereco();
                        endereco = editarEndereco(endereco);
                        contato.adicionarEndereco(endereco);
                        System.out.println("Deseja adicionar outro endereço? (S/N)");
                        adicionarOutroEndereco = scanner.nextLine();
                    }

                    String adicionarOutroTelefone = "S";
                    while (adicionarOutroTelefone.equalsIgnoreCase("S")){
                        Telefone telefone;
                        telefone = new Telefone();
                        telefone = editarTelefone(telefone);
                        contato.adicionarTelefone(telefone);
                        System.out.println("Deseja adicionar outro telefone? (S/N)");
                        adicionarOutroTelefone = scanner.nextLine();
                    }

                    try {
                        contatoService.adicionarContato(contato);
                        System.out.println("Contato adicionado: " + contato);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Erro ao adicionar contato: " + e.getMessage());
                    } catch (IOException e) {
                        System.out.println("Erro ao adicionar contato: " + e.getMessage());
                    }
                    break;

                case 3:
                    // Editando um contato
                    Contato contatoEditar = new Contato();
                    System.out.println("Digite o ID do contato que deseja editar: ");
                    int idEditarContato = scanner.nextInt();
                    scanner.nextLine();
                    try{
                        contatoEditar = contatoService.buscarContatoPorId(idEditarContato);
                        if (contatoEditar == null){
                            System.out.println("Contato não encontrado.");
                            break;
                        }
                    } catch (IOException e) {
                        System.out.println("Erro ao buscar contato: " + e.getMessage());
                    }
                    int menuEditar = 0;
                    do{
                        System.out.println("\n" + contatoEditar);
                        System.out.println("Menu de edição:");
                        System.out.println("1 - Editar nome");
                        System.out.println("2 - Editar email");
                        System.out.println("3 - Editar data de nascimento");
                        System.out.println("4 - Editar endereços");
                        System.out.println("5 - Editar telefones");
                        System.out.println("6 - Salvar alterações");
                        System.out.println("0 - Cancelar");
                        System.out.print("Escolha uma opção: ");
                        menuEditar = scanner.nextInt();
                        scanner.nextLine();
                        switch (menuEditar){
                            case 0:
                                break;
                            case 1:
                                // Editando o nome
                                contatoEditar = editarNome(contatoEditar);
                                break;
                            case 2:
                                // Editando o email
                                contatoEditar = editarEmail(contatoEditar);
                                break;
                            case 3:
                                // Editando a data de nascimento
                                contatoEditar = editarDataNascimento(contatoEditar);
                                break;
                            case 4:
                                // Editando o endereço
                                System.out.println("Deseja adicionar, editar, ou remover um endereço?");
                                System.out.println("1 - Adicionar");
                                System.out.println("2 - Editar");
                                System.out.println("3 - Remover");
                                int opcaoEndereco = scanner.nextInt();
                                scanner.nextLine();
                                switch (opcaoEndereco){
                                    case 1:
                                        // Adicionando um novo endereço
                                        Endereco endereco;
                                        endereco = new Endereco();
                                        endereco = editarEndereco(endereco);
                                        contatoEditar.adicionarEndereco(endereco);
                                        break;
                                    case 2:
                                        // Editando um endereço
                                        System.out.println("Deseja editar qual endereço?");
                                        for (int i = 0; i < contatoEditar.getEnderecos().size(); i++) {
                                            System.out.println(i + " - " + contatoEditar.getEnderecos().get(i));
                                        }
                                        int indexEndereco = scanner.nextInt();
                                        scanner.nextLine();
                                        if (indexEndereco < 0 || indexEndereco >= contatoEditar.getEnderecos().size()){
                                            System.out.println("Endereço inválido.");
                                            break;
                                        }
                                        Endereco enderecoEditar = contatoEditar.getEnderecos().get(indexEndereco);
                                        enderecoEditar = editarEndereco(enderecoEditar);
                                        contatoEditar.getEnderecos().set(indexEndereco, enderecoEditar);
                                        break;
                                    case 3:
                                        // Removendo um endereço
                                        System.out.println("Deseja remover qual endereço?");
                                        for (int i = 0; i < contatoEditar.getEnderecos().size(); i++) {
                                            System.out.println(i + " - " + contatoEditar.getEnderecos().get(i));
                                        }
                                        int indexRemoverEndereco = scanner.nextInt();
                                        scanner.nextLine();
                                        if (indexRemoverEndereco < 0 || indexRemoverEndereco >= contatoEditar.getEnderecos().size()){
                                            System.out.println("Endereço inválido.");
                                            break;
                                        }
                                        Endereco enderecoRemover = contatoEditar.getEnderecos().get(indexRemoverEndereco);
                                        contatoEditar.removerEndereco(enderecoRemover);
                                        break;
                                    default:
                                        System.out.println("Opção inválida.");
                                        break;
                                }
                                break;
                            case 5:
                                // Editando o telefone
                                System.out.println("Deseja adicionar, editar, ou remover um telefone?");
                                System.out.println("1 - Adicionar");
                                System.out.println("2 - Editar");
                                System.out.println("3 - Remover");
                                int opcaoTelefone = scanner.nextInt();
                                scanner.nextLine();
                                switch (opcaoTelefone){
                                    case 1:
                                        // Adicionando um novo telefone
                                        Telefone telefone;
                                        telefone = new Telefone();
                                        telefone = editarTelefone(telefone);
                                        contatoEditar.adicionarTelefone(telefone);
                                        break;
                                    case 2:
                                        // Editando um telefone
                                        System.out.println("Deseja editar qual telefone?");
                                        for (int i = 0; i < contatoEditar.getTelefones().size(); i++) {
                                            System.out.println(i + " - " + contatoEditar.getTelefones().get(i));
                                        }
                                        int indexTelefone = scanner.nextInt();
                                        scanner.nextLine();
                                        if (indexTelefone < 0 || indexTelefone >= contatoEditar.getTelefones().size()){
                                            System.out.println("Telefone inválido.");
                                            break;
                                        }
                                        Telefone telefoneEditar = contatoEditar.getTelefones().get(indexTelefone);
                                        telefoneEditar = editarTelefone(telefoneEditar);
                                        contatoEditar.getTelefones().set(indexTelefone, telefoneEditar);
                                        break;
                                    case 3:
                                        // Removendo um telefone
                                        System.out.println("Deseja remover qual telefone?");
                                        for (int i = 0; i < contatoEditar.getTelefones().size(); i++) {
                                            System.out.println(i + " - " + contatoEditar.getTelefones().get(i));
                                        }
                                        int indexRemoverTelefone = scanner.nextInt();
                                        scanner.nextLine();
                                        if (indexRemoverTelefone < 0 || indexRemoverTelefone >= contatoEditar.getTelefones().size()){
                                            System.out.println("Telefone inválido.");
                                            break;
                                        }
                                        Telefone telefoneRemover = contatoEditar.getTelefones().get(indexRemoverTelefone);
                                        contatoEditar.removerTelefone(telefoneRemover);
                                        break;
                                    default:
                                        System.out.println("Opção inválida.");
                                        break;
                                }
                                break;
                            case 6:
                                // Salvando as edições
                                try {
                                    contatoService.editarContato(contatoEditar);
                                    System.out.println("Contato editado: " + contatoEditar);
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Erro ao editar contato: " + e.getMessage());
                                } catch (IOException e) {
                                    System.out.println("Erro ao editar contato: " + e.getMessage());
                                }
                                break;
                            default:
                                System.out.println("Opção inválida.");
                                break;
                        }
                    } while(menuEditar != 0 && menuEditar != 6);
                    break;
                case 4:
                    // Removendo um contato
                    new Contato();
                    Contato contatoRemover;
                    System.out.println("Digite o ID do contato que deseja remover: ");
                    int idRemoverContato = scanner.nextInt();
                    scanner.nextLine();
                    try{
                        contatoRemover = contatoService.buscarContatoPorId(idRemoverContato);
                        if (contatoRemover == null){
                            System.out.println("Contato não encontrado.");
                            break;
                        }
                        System.out.println(contatoRemover);
                        System.out.println("Deseja realmente remover este contato? (S/N)");
                        String confirmacao = scanner.nextLine();
                        if (!confirmacao.equalsIgnoreCase("S")){
                            break;
                        }
                        contatoService.removerContato(contatoRemover);
                        System.out.println("Contato removido: " + contatoRemover);
                    } catch (IOException e) {
                        System.out.println("Erro ao buscar contato: " + e.getMessage());
                    }
                    break;
                case 5:
                    // Buscando contato por id
                    new Contato();
                    Contato contatoBuscarPorId;
                    System.out.println("Digite o ID do contato que deseja buscar: ");
                    int idBuscarContato = scanner.nextInt();
                    scanner.nextLine();
                    try{
                        contatoBuscarPorId = contatoService.buscarContatoPorId(idBuscarContato);
                        if (contatoBuscarPorId == null){
                            System.out.println("Contato não encontrado.");
                            break;
                        }
                        System.out.println("Contato encontrado: " + contatoBuscarPorId);
                    } catch (IOException e) {
                        System.out.println("Erro ao buscar contato: " + e.getMessage());
                    }
                    break;
                case 6:
                    // Buscando contato por nome
                    System.out.print("Digite o nome do contato que deseja buscar: ");
                    String nomeBuscarContato = scanner.nextLine();
                    try{
                        List<Contato> contatosBuscarPorNome = contatoService.buscarContatosPorNome(nomeBuscarContato);
                        if (contatosBuscarPorNome.isEmpty()){
                            System.out.println("Contato não encontrado.");
                            break;
                        }
                        System.out.println("Contatos encontrados:");
                        for (Contato c : contatosBuscarPorNome) {
                            System.out.println(c.toString());
                        }
                    } catch (IOException e) {
                        System.out.println("Erro ao buscar contato: " + e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }

        } while(menu != 0);

        scanner.close();
        // Fechando a conexão (caso seja necessário)
        contatoService.fecharConexao();
    }

    private static Contato editarNome(Contato contato) {
        do {
            System.out.print("Nome: ");
            contato.setNome(scanner.nextLine());
            if (!contatoService.isNomeValido(contato.getNome())) {
                System.out.println("Nome inválido. Tente novamente.");
            }
        } while (!contatoService.isNomeValido(contato.getNome()));
        return contato;
    }

    private static Contato editarEmail(Contato contato) {
        do {
            System.out.print("Email: ");
            contato.setEmail(scanner.nextLine());
            if (!contatoService.isEmailValido(contato.getEmail())) {
                System.out.println("Email inválido. Tente novamente.");
            }
        } while (!contatoService.isEmailValido(contato.getEmail()));
        return contato;
    }

    private static Contato editarDataNascimento(Contato contato) {
        do {
            System.out.print("Data de nascimento (dd/mm/aaaa): ");
            try {
                contato.setDataNascimento(contato.formatarData(scanner.nextLine()));
                break; // Sai do loop se a data estiver correta
            } catch (IllegalArgumentException e) {
                System.out.println("Data inválida. Tente novamente.");
            }
        } while (true);
        return contato;
    }

    private static Endereco editarEndereco(Endereco endereco) {
        do {
            System.out.print("Logradouro: ");
            endereco.setLogradouro(scanner.nextLine());
            if (!contatoService.isLogradouroValido(endereco.getLogradouro())) {
                System.out.println("Logradouro inválido. Tente novamente.");
            }
        } while (!contatoService.isLogradouroValido(endereco.getLogradouro()));

        do {
            System.out.print("Número: ");
            endereco.setNumero(scanner.nextLine());
            if (!contatoService.isNumeroEnderecoValido(endereco.getNumero())) {
                System.out.println("Número inválido. Tente novamente.");
            }
        } while (!contatoService.isNumeroEnderecoValido(endereco.getNumero()));

        System.out.print("Complemento: ");
        endereco.setComplemento(scanner.nextLine());

        do {
            System.out.print("Bairro: ");
            endereco.setBairro(scanner.nextLine());
            if (!contatoService.isBairroValido(endereco.getBairro())) {
                System.out.println("Bairro inválido. Tente novamente.");
            }
        } while (!contatoService.isBairroValido(endereco.getBairro()));

        do {
            System.out.print("Cidade: ");
            endereco.setCidade(scanner.nextLine());
            if (!contatoService.isCidadeValida(endereco.getCidade())) {
                System.out.println("Cidade inválida. Tente novamente.");
            }
        } while (!contatoService.isCidadeValida(endereco.getCidade()));

        do {
            System.out.print("UF: ");
            endereco.setUf(scanner.nextLine().toUpperCase());
            if (!contatoService.isUfValida(endereco.getUf())) {
                System.out.println("UF inválida. Tente novamente.");
            }
        } while (!contatoService.isUfValida(endereco.getUf()));

        do {
            System.out.print("CEP: ");
            endereco.setCep(contatoService.converterParaApenasDigitos(scanner.nextLine()));
            if (!contatoService.isCepValido(endereco.getCep())) {
                System.out.println("CEP inválido. Tente novamente.");
            }
        } while (!contatoService.isCepValido(endereco.getCep()));

        return endereco;
    }

    private static Telefone editarTelefone(Telefone telefone) {
        do {
            System.out.print("DDD: ");
            telefone.setDdd(scanner.nextLine());
            if (!contatoService.isDddValido(telefone.getDdd())) {
                System.out.println("DDD inválido. Tente novamente.");
            }
        } while (!contatoService.isDddValido(telefone.getDdd()));

        do {
            System.out.print("Número: ");
            telefone.setNumero(contatoService.converterParaApenasDigitos(scanner.nextLine()));
            if (!contatoService.isNumeroTelefoneValido(telefone.getNumero())) {
                System.out.println("Número de telefone inválido. Tente novamente.");
            }
        } while (!contatoService.isNumeroTelefoneValido(telefone.getNumero()));

        return telefone;
    }
}