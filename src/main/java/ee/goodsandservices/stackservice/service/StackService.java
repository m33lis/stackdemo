package ee.goodsandservices.stackservice.service;

import ee.goodsandservices.stackservice.domain.Stack;
import ee.goodsandservices.stackservice.repository.StackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Stack.
 */
@Service
@Transactional
public class StackService {

    private final Logger log = LoggerFactory.getLogger(StackService.class);

    @Inject
    private StackRepository stackRepository;

    /**
     * Save a stack.
     *
     * @param stack the entity to save
     * @return the persisted entity
     */
    public Stack save(Stack stack) {
        log.debug("Request to save Stack : {}", stack);
        Stack result = stackRepository.save(stack);
        return result;
    }

    /**
     *  Get all the stacks.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Stack> findAll() {
        log.debug("Request to get all Stacks");
        List<Stack> result = stackRepository.findAll();

        return result;
    }

    /**
     *  Get one stack by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Stack findOne(Long id) {
        log.debug("Request to get Stack : {}", id);
        Stack stack = stackRepository.findOne(id);
        return stack;
    }

    /**
    *  Get all stacks by session
    *
    *  @param session
    *  @return the list of entities
    * */
    @Transactional(readOnly = true)
    public List<Stack> findAllBySession(String session) {
        log.debug("Request to get all Stacks with session : ", session);
        List<Stack> result = stackRepository.findAllBySession(session);
        return result;
    }


    /**
     *  Delete the  stack by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Stack : {}", id);
        stackRepository.delete(id);
    }
}
