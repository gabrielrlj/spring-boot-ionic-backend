package com.jardim.services;

import org.springframework.mail.SimpleMailMessage;

import com.jardim.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	void sendEmail(SimpleMailMessage msg);
	
}
