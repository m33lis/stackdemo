package ee.goodsandservices.stackservice.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Stack.
 */
@Entity
@Table(name = "stack")
public class Stack implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "val")
    private String val;

    @Column(name = "session")
    private String session;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVal() {
        return val;
    }

    public Stack val(String val) {
        this.val = val;
        return this;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getSession() {
        return session;
    }

    public Stack session(String session) {
        this.session = session;
        return this;
    }

    public void setSession(String session) {
        this.session = session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stack stack = (Stack) o;
        if (stack.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stack.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Stack{" +
            "id=" + id +
            ", val='" + val + "'" +
            ", session='" + session + "'" +
            '}';
    }
}
