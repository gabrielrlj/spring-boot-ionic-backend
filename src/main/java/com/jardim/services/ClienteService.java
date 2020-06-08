package com.jardim.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.jardim.domain.Cliente;
import com.jardim.dto.ClienteDTO;
import com.jardim.repositories.ClienteRepository;
import com.jardim.services.exceptions.DataIntegrityException;
import com.jardim.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	@Autowired
	private ClienteRepository repo;

	public Cliente buscarPorId(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
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
	
	//retorna um página de dados de categorias
	//parâmetros em ordem: a página, quantas linhas por página, ordenação, asc ou desc
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		//objeto que prepara minhas informações para que eu faça a consulta que me retorna a pagina de dados
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	//méotodo auxiliar que instancia uma categoria a partir de um dto
	public Cliente fromDto(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null);
	}
}
