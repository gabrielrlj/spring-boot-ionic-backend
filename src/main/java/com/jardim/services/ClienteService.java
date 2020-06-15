package com.jardim.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jardim.domain.Cidade;
import com.jardim.domain.Cliente;
import com.jardim.domain.Endereco;
import com.jardim.domain.enums.Perfil;
import com.jardim.domain.enums.TipoCliente;
import com.jardim.dto.ClienteDTO;
import com.jardim.dto.NewClienteDTO;
import com.jardim.repositories.ClienteRepository;
import com.jardim.repositories.EnderecoRepository;
import com.jardim.security.UserSS;
import com.jardim.services.exceptions.AuthorizationException;
import com.jardim.services.exceptions.DataIntegrityException;
import com.jardim.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepo;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private S3Service s3;
	
	@Autowired
	private ImageService is;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	@Value("${img.profile.size}")
	private Integer size;
	
	public Cliente buscarPorId(Integer id) {
		
		UserSS user	= UserService.authenticated();
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepo.saveAll(obj.getEnderecos());
		return obj;
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = buscarPorId(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}

	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

	public void delete(Integer id) {
		buscarPorId(id);
		try {
			repo.deleteById(id);	
		}catch(DataIntegrityViolationException e){
			throw new DataIntegrityException("Não é possível excluir há entidades relacionadas!");
		}
		
	}

	public List<Cliente> buscar() {
		
		return repo.findAll();
	}
	
	//retorna um página de dados de clientes
	//parâmetros em ordem: a página, quantas linhas por página, ordenação, asc ou desc
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		//objeto que prepara minhas informações para que eu faça a consulta que me retorna a pagina de dados
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDto(NewClienteDTO objDto) {
		
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), 
				TipoCliente.toEnum(objDto.getTipo()), pe.encode(objDto.getSenha()));
		
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(),
				objDto.getBairro(), objDto.getCep(), cli, cid);
		
		cli.getEnderecos().add(end);
		
		cli.getTelefones().add(objDto.getTel1());
		
		if(objDto.getTel2()!=null) {
			cli.getTelefones().add(objDto.getTel2());
		}
		
		if(objDto.getTel3()!=null) {
			cli.getTelefones().add(objDto.getTel3());
		}
		
		return cli;
	}
	
	public Cliente fromDto(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}
	
	public URI uploadProfilePicture(MultipartFile mpf) {
		UserSS user	= UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		
		BufferedImage jpgImage = is.getJpgImageFromFile(mpf);
		jpgImage = is.cropSquare(jpgImage);
		jpgImage = is.resize(jpgImage, size);
		
		
		
		String fileName = prefix + user.getId() + ".jpg";
		
		return s3.uploadFile(is.getInputStream(jpgImage, "jpg"), fileName, "image");
	}
}
