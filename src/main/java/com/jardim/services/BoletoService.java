package com.jardim.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.jardim.domain.PagamentoComBoleto;

//classe paliativa para gerar data de vencimento para o boleto
@Service
public class BoletoService {
	public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instanteDoPedido) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(instanteDoPedido);
		cal.add(Calendar.DAY_OF_MONTH, 7);
		pagto.setDataVencimento(cal.getTime());
	}	
}
