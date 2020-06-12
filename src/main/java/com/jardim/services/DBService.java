package com.jardim.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jardim.domain.Categoria;
import com.jardim.domain.Cidade;
import com.jardim.domain.Cliente;
import com.jardim.domain.Endereco;
import com.jardim.domain.Estado;
import com.jardim.domain.ItemPedido;
import com.jardim.domain.Pagamento;
import com.jardim.domain.PagamentoComBoleto;
import com.jardim.domain.PagamentoComCartao;
import com.jardim.domain.Pedido;
import com.jardim.domain.Produto;
import com.jardim.domain.enums.EstadoPagamento;
import com.jardim.domain.enums.TipoCliente;
import com.jardim.repositories.CategoriaRepository;
import com.jardim.repositories.CidadeRepository;
import com.jardim.repositories.ClienteRepository;
import com.jardim.repositories.EnderecoRepository;
import com.jardim.repositories.EstadoRepository;
import com.jardim.repositories.ItemPedidoRepository;
import com.jardim.repositories.PagamentoRepository;
import com.jardim.repositories.PedidoRepository;
import com.jardim.repositories.ProdutoRepository;

@Service
public class DBService {
	
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
	@Autowired
	private PagamentoRepository pagamentoRepo;
	@Autowired
	private PedidoRepository pedidoRepo;
	@Autowired
	private ItemPedidoRepository itemPedidoRepo;
	
	public void instantiateTestDatabase() throws ParseException {
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");
		Categoria cat3 = new Categoria(null, "Cama mesa e banho");
		Categoria cat4 = new Categoria(null, "Lazer");
		Categoria cat5 = new Categoria(null, "Livros");
		Categoria cat6 = new Categoria(null, "Eletrodomesticos");
		
		
		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 30.00);
		Produto p4 = new Produto(null, "Shampoo", 12.00);
		Produto p5 = new Produto(null, "Mesa", 250.00);
		Produto p6 = new Produto(null, "Lampada de Escritorio", 45.00);
		Produto p7 = new Produto(null, "It Stephen King", 49.90);
		Produto p8 = new Produto(null, "Jogo", 200.00);
		Produto p9 = new Produto(null, "TV", 2500.00);
		Produto p10 = new Produto(null, "Geladeira", 1000.00);
		Produto p11 = new Produto(null, "IoIo", 15.00);
		

		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3, p8, p9));
		cat2.getProdutos().addAll(Arrays.asList(p2, p3, p5, p6));
		cat3.getProdutos().addAll(Arrays.asList(p5, p4));
		cat4.getProdutos().addAll(Arrays.asList(p8, p9, p11));
		cat5.getProdutos().addAll(Arrays.asList(p7));
		cat6.getProdutos().addAll(Arrays.asList(p10));

		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p4.getCategorias().addAll(Arrays.asList(cat3));
		p5.getCategorias().addAll(Arrays.asList(cat2, cat3));
		p6.getCategorias().addAll(Arrays.asList(cat2));
		p7.getCategorias().addAll(Arrays.asList(cat5));
		p8.getCategorias().addAll(Arrays.asList(cat1, cat4));
		p9.getCategorias().addAll(Arrays.asList(cat1, cat4));
		p10.getCategorias().addAll(Arrays.asList(cat6));
		p11.getCategorias().addAll(Arrays.asList(cat4));


		categoriaRepo.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5, cat6));
		produtoRepo.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11));

		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");

		Cidade cid1 = new Cidade(null, "Uberlandia", est1);
		Cidade cid2 = new Cidade(null, "São Paulo", est2);
		Cidade cid3 = new Cidade(null, "Campinas", est2);

		est1.getCidades().addAll(Arrays.asList(cid1));
		est2.getCidades().addAll(Arrays.asList(cid2, cid3));

		estadoRepo.saveAll(Arrays.asList(est1, est2));
		cidadeRepo.saveAll(Arrays.asList(cid1, cid2, cid3));

		Cliente cli1 = new Cliente(null, "João da Silva", "gabriellacerdajardim@gmail.com", "12345678910", TipoCliente.PESSOAFISICA);
		cli1.getTelefones().addAll(Arrays.asList("998858654", "985654854"));

		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto.303", "Jardim", "11350875", cli1, cid1);
		Endereco e2 = new Endereco(null, "Avenida Matos", "124", "Frente", "Centro", "11860883", cli1, cid2);

		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));
		
		clienteRepo.saveAll(Arrays.asList(cli1));
		enderecoRepo.saveAll(Arrays.asList(e1, e2));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Pedido ped1 = new Pedido(null, sdf.parse("30/01/2020 10:43"), cli1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/02/2020 11:43"), cli1, e2);
		
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagto1);
		
		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/02/2020 00:00"), null);
		ped2.setPagamento(pagto2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));
		
		pedidoRepo.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepo.saveAll(Arrays.asList(pagto1, pagto2));
		
		ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, 2000.00);
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, 30.00);
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, 800.00);
		
		ped1.getItens().addAll(Arrays.asList(ip1, ip2));
		ped2.getItens().addAll(Arrays.asList(ip3));
		
		p1.getItens().addAll(Arrays.asList(ip1));
		p2.getItens().addAll(Arrays.asList(ip3));
		p3.getItens().addAll(Arrays.asList(ip2));
		
		itemPedidoRepo.saveAll(Arrays.asList(ip1, ip2, ip3));
	}
}
