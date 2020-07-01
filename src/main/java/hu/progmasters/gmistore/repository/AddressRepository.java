package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
