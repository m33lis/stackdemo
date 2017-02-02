package ee.goodsandservices.stackservice.repository;

import ee.goodsandservices.stackservice.domain.Stack;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Stack entity.
 */
@SuppressWarnings("unused")
public interface StackRepository extends JpaRepository<Stack,Long> {

    List<Stack> findAllBySession(String session);
}
