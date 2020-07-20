package it.polito.ai.virtuallabs.repositories;

import it.polito.ai.virtuallabs.entities.VirtualMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirtualMachineRepository extends JpaRepository<VirtualMachine, Long> {
}
