package com.jardim.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.jardim.domain.Cliente;
import com.jardim.domain.enums.TipoCliente;
import com.jardim.dto.NewClienteDTO;
import com.jardim.repositories.ClienteRepository;
import com.jardim.resources.exceptions.FieldMessage;
import com.jardim.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, NewClienteDTO> {

	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteInsert ann) {

	}

	@Override   
	 public boolean isValid(NewClienteDTO objDto, ConstraintValidatorContext context) { 
		 
		 List<FieldMessage> list = new ArrayList<>();                
		 // inclua os testes aqui, inserindo erros na lista 
		 
		if(objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCpf(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		}
		
		if(objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCnpj(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
		}
		
		Cliente aux = repo.findByEmail(objDto.getEmail());
		if (aux != null) {
			list.add(new FieldMessage("Email", "Email já existente"));
		}
		
		
		 
		 for (FieldMessage e : list) {
			 context.disableDefaultConstraintViolation();
			 context.buildConstraintViolationWithTemplate(e.getMessage())
			 .addPropertyNode(e.getFieldName()).addConstraintViolation(); 
		 }
		 return list.isEmpty();
	 }
}