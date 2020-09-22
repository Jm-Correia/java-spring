/**
 * state-machine
 * @author João Correia -  joao.correia.marcos@gmail.com 
 * 18 de set. de 2020
 */
package jmc.exemple.springframework.statemachine.config;

import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import jmc.exemple.springframework.statemachine.model.enumeration.PaymentEvent;
import jmc.exemple.springframework.statemachine.model.enumeration.PaymentState;

/**
 * state-machine
 * @author João Correia 
 * 18 de set. de 2020
 */

@EnableStateMachineFactory
@Configuration
public class StateMachineConfig extends StateMachineConfigurerAdapter<PaymentState, PaymentEvent> {

	
	private Logger log = LoggerFactory.getLogger(StateMachineConfig.class);
	
	@Override
	public void configure(StateMachineStateConfigurer<PaymentState, PaymentEvent> states) throws Exception {
		
		states.withStates()
			.initial(PaymentState.NEW)
			.states(EnumSet.allOf(PaymentState.class))
			.end(PaymentState.AUTH)
			.end(PaymentState.PRE_AUTH_ERROR)
			.end(PaymentState.AUTH_ERROR);
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<PaymentState, PaymentEvent> transitions) throws Exception {
		
		transitions
			.withExternal().source(PaymentState.NEW)
			.target(PaymentState.PRE_AUTH).event(PaymentEvent.PRE_AUTHORIZE)
			.and()
			.withExternal().source(PaymentState.PRE_AUTH)
			.target(PaymentState.AUTH).event(PaymentEvent.PRE_AUTH_APPROVED)
			.and()
			.withExternal().source(PaymentState.NEW)
			.target(PaymentState.PRE_AUTH_ERROR).event(PaymentEvent.PRE_AUTH_DECLINED);
			
		
	}

	@Override
	public void configure(StateMachineConfigurationConfigurer<PaymentState, PaymentEvent> config) throws Exception {
		
		StateMachineListenerAdapter<PaymentState, PaymentEvent> adapter = new StateMachineListenerAdapter<>() {

			@Override
			public void stateChanged(State<PaymentState, PaymentEvent> from, State<PaymentState, PaymentEvent> to) {
				String de =  (from != null ? from.getId().toString() : null);
				String para = (to != null ? to.getId().toString() : null);
				log.info(String.format("#####INFO#### StateChanged(from: %s, to: %s", de, para));
			}
			
		};
		config.withConfiguration().listener(adapter);
	}

	
	
	
	
	
}
