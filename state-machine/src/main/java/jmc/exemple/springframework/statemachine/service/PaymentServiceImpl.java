/**
 * state-machine
 * @author João Correia -  joao.correia.marcos@gmail.com 
 * 18 de set. de 2020
 */
package jmc.exemple.springframework.statemachine.service;

import javax.transaction.Transactional;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import jmc.exemple.springframework.statemachine.model.Payment;
import jmc.exemple.springframework.statemachine.model.enumeration.PaymentEvent;
import jmc.exemple.springframework.statemachine.model.enumeration.PaymentState;
import jmc.exemple.springframework.statemachine.repositorty.PaymentRepository;
import jmc.exemple.springframework.statemachine.service.listener.PaymentStateChangeListener;
import lombok.RequiredArgsConstructor;

/**
 * state-machine - 18 de set. de 2020
 * @author João Correia
 */

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

	public final static String PAYMENT_ID_HEADER = "payment_id";
	
	private final PaymentRepository repo;
	
	private final StateMachineFactory<PaymentState, PaymentEvent> stateFactory;
	private final PaymentStateChangeListener intercepter;
	
	@Override
	public Payment newPayment(Payment payment) {
		payment.setState(PaymentState.NEW);
		return repo.save(payment);
	}

	@Transactional
	@Override
	public StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId) {
		StateMachine<PaymentState, PaymentEvent> stateMachine = this.build(paymentId);
		this.sendEvent(paymentId, stateMachine, PaymentEvent.PRE_AUTHORIZE);
		return stateMachine;
	}

	@Transactional
	@Override
	public StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId) {
		StateMachine<PaymentState, PaymentEvent> stateMachine = this.build(paymentId);
		this.sendEvent(paymentId, stateMachine, PaymentEvent.PRE_AUTH_APPROVED);
		return stateMachine;
	}

	@Transactional
	@Override
	public StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId) {
		StateMachine<PaymentState, PaymentEvent> stateMachine = this.build(paymentId);
		this.sendEvent(paymentId, stateMachine, PaymentEvent.PRE_AUTH_DECLINED);
		return stateMachine;
	}
	
	
	private void sendEvent(Long paymentId, StateMachine<PaymentState, PaymentEvent> state, PaymentEvent event) {
		Message msg = MessageBuilder.withPayload(event)
				.setHeader(PAYMENT_ID_HEADER, paymentId)
				.build();
		
		state.sendEvent(msg);
	}
	
	private StateMachine<PaymentState, PaymentEvent> build(Long paymentId){
		Payment payment = repo.getOne(paymentId);
		StateMachine<PaymentState, PaymentEvent> stateMachine =  stateFactory.getStateMachine(Long.toString(payment.getId()));
		
		stateMachine.stop();
		
		stateMachine.getStateMachineAccessor()
			.doWithAllRegions(sma -> {
				sma.addStateMachineInterceptor(intercepter);
				sma.resetStateMachine(new DefaultStateMachineContext<>(payment.getState(), null, null, null));
			});
		stateMachine.start();
		return stateMachine;
	}

}
