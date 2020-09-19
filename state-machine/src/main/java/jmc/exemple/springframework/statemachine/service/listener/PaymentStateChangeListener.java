/**
 * state-machine
 * @author João Correia -  joao.correia.marcos@gmail.com 
 * 18 de set. de 2020
 */
package jmc.exemple.springframework.statemachine.service.listener;

import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import jmc.exemple.springframework.statemachine.model.Payment;
import jmc.exemple.springframework.statemachine.model.enumeration.PaymentEvent;
import jmc.exemple.springframework.statemachine.model.enumeration.PaymentState;
import jmc.exemple.springframework.statemachine.repositorty.PaymentRepository;
import jmc.exemple.springframework.statemachine.service.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;

/**
 * state-machine - 18 de set. de 2020
 * @author João Correia
 */

@RequiredArgsConstructor
@Component
public class PaymentStateChangeListener extends StateMachineInterceptorAdapter<PaymentState, PaymentEvent> {
	
	
	
	private final PaymentRepository repo;
	
	@Override
	public void preStateChange(State<PaymentState, PaymentEvent> state, Message<PaymentEvent> message,
			Transition<PaymentState, PaymentEvent> transition, StateMachine<PaymentState, PaymentEvent> stateMachine) {
		
		Optional.ofNullable(message).ifPresent(msg -> {
			Optional.ofNullable(Long.class.cast(msg.getHeaders().getOrDefault(PaymentServiceImpl.PAYMENT_ID_HEADER, -1L)))
				.ifPresent(paymentId -> {
					Payment payment = repo.getOne(paymentId);
					payment.setState(state.getId());
					repo.save(payment);
				});
		});
	}

	
	
}
