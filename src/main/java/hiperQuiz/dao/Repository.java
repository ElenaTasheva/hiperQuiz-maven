package hiperQuiz.dao;

import hiperQuiz.exception.EntityAlreadyExistsException;
import hiperQuiz.exception.EntityNotFoundException;
import hiperQuiz.model.Identifiable;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface Repository<K,V extends Identifiable<K>> {

    List<V> findAll();
    Optional<V> findById(K id);
    V create(V entity);
    V update(V entity)throws EntityNotFoundException;
    V deleteById(K id) throws EntityNotFoundException;
    long count();
    void drop();


    int createBatch(Collection<V> entities) throws EntityAlreadyExistsException, EntityAlreadyExistsException;

}
