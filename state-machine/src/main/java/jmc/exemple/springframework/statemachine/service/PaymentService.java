/**
 * state-machine
 * @author João Correia -  joao.correia.marcos@gmail.com 
 * 18 de set. de 2020
 */
package jmc.exemple.springframework.statemachine.service;

import org.springframework.statemachine.StateMachine;

import jmc.exemple.springframework.statemachine.model.Payment;
import jmc.exemple.springframework.statemachine.model.enumeration.PaymentEvent;
import jmc.exemple.springframework.statemachine.model.enumeration.PaymentState;

/**
 * state-machine - 18 de set. de 2020
 * @author João Correia
 */

public interface PaymentService {

	Payment newPayment(Payment payment);
	
	StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId);
	
	StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId);
	
	StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId);
	
}
