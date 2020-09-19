/**
 * state-machine
 * @author João Correia -  joao.correia.marcos@gmail.com 
 * 18 de set. de 2020
 */
package jmc.exemple.springframework.statemachine.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;

import jmc.exemple.springframework.statemachine.model.Payment;
import jmc.exemple.springframework.statemachine.model.enumeration.PaymentEvent;
import jmc.exemple.springframework.statemachine.model.enumeration.PaymentState;
import jmc.exemple.springframework.statemachine.repositorty.PaymentRepository;

/**
 * state-machine - 18 de set. de 2020
 * @author João Correia
 */
@SpringBootTest
class PaymentServiceImpTest {

	@Autowired
	PaymentService service;
	
	@Autowired
	PaymentRepository repo;
	
	
	Payment payment;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		payment = Payment.builder().amount(new BigDecimal(12.99)).build();
	}

	@Transactional
	@Test
	void test() {
		Payment savedPayment = service.newPayment(payment);
		
		StateMachine<PaymentState, PaymentEvent> sm = service.preAuth(savedPayment.getId());
		
		System.out.println("state: " + sm);

		Payment preAuthedPayment =  repo.getOne(savedPayment.getId());
		
		System.out.println(String.format("Objeto Payment: %s", preAuthedPayment.toString()));
		
		assertEquals(sm, preAuthedPayment.getId());
	}

}
