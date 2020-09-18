/**
 * state-machine
 * @author João Correia -  joao.correia.marcos@gmail.com 
 * 18 de set. de 2020
 */
package jmc.exemple.springframework.statemachine.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import jmc.exemple.springframework.statemachine.model.enumeration.PaymentEvent;
import jmc.exemple.springframework.statemachine.model.enumeration.PaymentState;

/**
 * state-machine - 18 de set. de 2020
 * @author João Correia
 */

@SpringBootTest
class StateMachineConfigTest {
	
	
	@Autowired
	StateMachineFactory<PaymentState, PaymentEvent> factory;
	
	private StateMachine<PaymentState, PaymentEvent> state;

	@BeforeEach
	void inicializerState() {
		state = factory.getStateMachine(UUID.randomUUID());
		state.start();	
		
	}
	
	@Test
	void testNewState() {
		
		state.sendEvent(PaymentEvent.PRE_AUTHORIZE);
		
		assertNotEquals(state.getState().getId(), PaymentState.PRE_AUTH);
		
	}
	
	@Test
	void testPreAuthState() {
		
		assertEquals(state.getState().getId(), PaymentState.NEW);
		state.sendEvent(PaymentEvent.PRE_AUTH_APPROVED);
		
		assertEquals(state.getState().getId(), PaymentState.PRE_AUTH);
		
	}
	@Test
	void testPreAuthErrorState() {
		
		assertEquals(state.getState().getId(), PaymentState.NEW);
		state.sendEvent(PaymentEvent.PRE_AUTH_DECLINED);
		
		assertEquals(state.getState().getId(), PaymentState.PRE_AUTH_ERROR);
		
	}

}
