/**
 * state-machine
 * @author João Correia -  joao.correia.marcos@gmail.com 
 * 18 de set. de 2020
 */
package jmc.exemple.springframework.statemachine.repositorty;

import org.springframework.data.jpa.repository.JpaRepository;

import jmc.exemple.springframework.statemachine.model.Payment;

/**
 * state-machine
 * @author João Correia 
 * 18 de set. de 2020
 */
public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
