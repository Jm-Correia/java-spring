/**
 * state-machine
 * @author João Correia -  joao.correia.marcos@gmail.com 
 * 18 de set. de 2020
 */
package jmc.exemple.springframework.statemachine.model.enumeration;

/**
 * state-machine
 * @author João Correia 
 * 18 de set. de 2020
 */
public enum PaymentState {

	NEW  {
		public PaymentState execute() {
			// TODO Auto-generated method stub
			System.out.println("Regras de negocio ### NEW");
			return PaymentState.NEW;
		}
	}, PRE_AUTH {

		public PaymentState execute() {
			System.out.println("Regras de negocio ### PRE_AUTH");
			
			return PaymentState.PRE_AUTH;	
		}
		
	}, PRE_AUTH_ERROR  {
		@Override
		public PaymentState execute() {
			System.out.println("Regras de negocio ### PRE_AUTH_ERROR");
			return PaymentState.PRE_AUTH_ERROR;
		}
	},AUTH  {
		@Override
		public PaymentState execute() {
			System.out.println("Regras de negocio ### AUTH");
			return PaymentState.AUTH;
		}
	}, AUTH_ERROR {
		@Override
		public PaymentState execute() {
			System.out.println("Regras de negocio ### AUTH_ERROR");
			return PaymentState.AUTH_ERROR;
		}
	};	
	public abstract PaymentState execute();
}
