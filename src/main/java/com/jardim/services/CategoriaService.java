package com.jardim.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.jardim.domain.Categoria;
import com.jardim.dto.CategoriaDTO;
import com.jardim.repositories.CategoriaRepository;
import com.jardim.services.exceptions.DataIntegrityException;
import com.jardim.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	@Autowired
	private CategoriaRepository repo;

	public Categoria buscarPorId(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}

	public Categoria update(Categoria obj) {
		buscarPorId(obj.getId());
		return repo.save(obj);
	}

	public void delete(Integer id) {
		buscarPorId(id);
		try {
			repo.deleteById(id);	
		}catch(DataIntegrityViolationException e){
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos!");
		}
		
	}

	public List<Categoria> buscar() {
		
		return repo.findAll();
	}
	
	//retorna um página de dados de categorias
	//parâmetros em ordem: a página, quantas linhas por página, ordenação, asc ou desc
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		//objeto que prepara minhas informações para que eu faça a consulta que me retorna a pagina de dados
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	//méotodo auxiliar que instancia uma categoria a partir de um dto
	public Categoria fromDto(CategoriaDTO objDto) {
		return new Categoria(objDto.getId(), objDto.getNome());
	}
}
