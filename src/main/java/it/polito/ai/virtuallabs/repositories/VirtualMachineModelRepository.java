package it.polito.ai.virtuallabs.repositories;

import it.polito.ai.virtuallabs.entities.VirtualMachineModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirtualMachineModelRepository extends JpaRepository<VirtualMachineModel, Long> {
}
