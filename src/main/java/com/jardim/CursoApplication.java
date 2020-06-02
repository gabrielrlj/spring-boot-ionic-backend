package com.jardim;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jardim.domain.Categoria;
import com.jardim.domain.Cidade;
import com.jardim.domain.Cliente;
import com.jardim.domain.Endereco;
import com.jardim.domain.Estado;
import com.jardim.domain.Produto;
import com.jardim.domain.enums.TipoCliente;
import com.jardim.repositories.CategoriaRepository;
import com.jardim.repositories.CidadeRepository;
import com.jardim.repositories.ClienteRepository;
import com.jardim.repositories.EnderecoRepository;
import com.jardim.repositories.EstadoRepository;
import com.jardim.repositories.ProdutoRepository;

@SpringBootApplication
public class CursoApplication implements CommandLineRunner { // interface que permite executar códigos ao rodar esta
														     // classe

	@Autowired
	private CategoriaRepository categoriaRepo;
	@Autowired
	private ProdutoRepository produtoRepo;
	@Autowired
	private EstadoRepository estadoRepo;
	@Autowired
	private CidadeRepository cidadeRepo;
	@Autowired
	private EnderecoRepository enderecoRepo;
	@Autowired
	private ClienteRepository clienteRepo;

	public static void main(String[] args) {
		SpringApplication.run(CursoApplication.class, args);
	}

	// método que roda ao iniciar essa classe
	@Override
	public void run(String... args) throws Exception {
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");

		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 30.00);

		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));

		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));

		categoriaRepo.saveAll(Arrays.asList(cat1, cat2));
		produtoRepo.saveAll(Arrays.asList(p1, p2, p3));

		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");

		Cidade cid1 = new Cidade(null, "Uberlandia", est1);
		Cidade cid2 = new Cidade(null, "São Paulo", est2);
		Cidade cid3 = new Cidade(null, "Campinas", est2);

		est1.getCidades().addAll(Arrays.asList(cid1));
		est2.getCidades().addAll(Arrays.asList(cid2, cid3));

		estadoRepo.saveAll(Arrays.asList(est1, est2));
		cidadeRepo.saveAll(Arrays.asList(cid1, cid2, cid3));

		Cliente cli1 = new Cliente(null, "João da Silva", "joao@gmail.com", "12345678910", TipoCliente.PESSOAFISICA);
		cli1.getTelefones().addAll(Arrays.asList("998858654", "985654854"));

		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto.303", "Jardim", "11350875", cli1, cid1);
		Endereco e2 = new Endereco(null, "Avenida Matos", "124", "Frente", "Centro", "11860883", cli1, cid2);

		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));
		
		clienteRepo.saveAll(Arrays.asList(cli1));
		enderecoRepo.saveAll(Arrays.asList(e1, e2));
	}

}
