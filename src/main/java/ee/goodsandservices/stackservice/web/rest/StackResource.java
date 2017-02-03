package ee.goodsandservices.stackservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import ee.goodsandservices.stackservice.domain.Stack;
import ee.goodsandservices.stackservice.service.StackService;
import ee.goodsandservices.stackservice.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Stack.
 */
@RestController
@RequestMapping("/api")
public class StackResource {

    private final Logger log = LoggerFactory.getLogger(StackResource.class);

    @Inject
    private StackService stackService;

    /**
     * POST  /stacks : Create a new stack.
     *
     * @param stack the stack to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stack, or with status 400 (Bad Request) if the stack has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stacks")
    @Timed
    public ResponseEntity<Stack> createStack(@RequestBody Stack stack) throws URISyntaxException {
        log.debug("REST request to save Stack : {}", stack);
        if (stack.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("stack", "idexists", "A new stack cannot already have an ID")).body(null);
        }
        Stack result = stackService.save(stack);
        return ResponseEntity.created(new URI("/api/stacks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("stack", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stacks : Updates an existing stack.
     *
     * @param stack the stack to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stack,
     * or with status 400 (Bad Request) if the stack is not valid,
     * or with status 500 (Internal Server Error) if the stack couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stacks")
    @Timed
    public ResponseEntity<Stack> updateStack(@RequestBody Stack stack) throws URISyntaxException {
        log.debug("REST request to update Stack : {}", stack);
        if (stack.getId() == null) {
            return createStack(stack);
        }
        Stack result = stackService.save(stack);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("stack", stack.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stacks : get all the stacks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of stacks in body
     */
    @GetMapping("/stacks")
    @Timed
    public List<Stack> getAllStacks() {
        log.debug("REST request to get all Stacks");
        return stackService.findAll();
    }

    /**
     * GET  /stacks/:id : get the "id" stack.
     *
     * @param id the id of the stack to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stack, or with status 404 (Not Found)
     */
    @GetMapping("/stacks/{id}")
    @Timed
    public ResponseEntity<Stack> getStack(@PathVariable Long id) {
        log.debug("REST request to get Stack : {}", id);
        Stack stack = stackService.findOne(id);
        return Optional.ofNullable(stack)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     *  GET /stacks/session/:session : get all stacks with given "session"
     *
     *  @return the ResponseEntity with status 200 (OK) and the filtered list of stacks in body
     * */
    @GetMapping("/stacks/session/{session}")
    @Timed
    public List<Stack> getAllBySession(@PathVariable String session) {
        log.debug("REST request to get Stacks with session : {}", session);
        return stackService.findAllBySession(session);
    }

    /**
     * DELETE  /stacks/:id : delete the "id" stack.
     *
     * @param id the id of the stack to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stacks/{id}")
    @Timed
    public ResponseEntity<Void> deleteStack(@PathVariable Long id) {
        log.debug("REST request to delete Stack : {}", id);
        Stack st = stackService.findOne(id);
        if (st.getId() != null) {
            stackService.delete(id);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stack", id.toString())).build();
    }

}
