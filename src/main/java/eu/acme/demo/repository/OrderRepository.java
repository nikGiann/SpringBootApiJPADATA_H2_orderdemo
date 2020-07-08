package eu.acme.demo.repository;

import eu.acme.demo.domain.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByClientReferenceCode(String clientReferenceCode);
    
}
