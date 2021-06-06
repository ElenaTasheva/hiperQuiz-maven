package hiperQuiz.dao.impl;


import hiperQuiz.dao.KeyGenerator;
import hiperQuiz.dao.Repository;
import hiperQuiz.exception.EntityAlreadyExistsException;
import hiperQuiz.exception.EntityNotFoundException;
import hiperQuiz.model.Identifiable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RepositoryMemoryImpl<K, V extends Identifiable<K>> implements Repository<K, V> {

    private Map<K,V> entities = new ConcurrentHashMap<>();
    private KeyGenerator<K> keyGenerator;




    public RepositoryMemoryImpl(KeyGenerator<K> keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    @Override
    public List<V> findAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public Optional<V> findById(K id) {
        return Optional.ofNullable(entities.get(id));
    }
    @Override
    public V create(V entity) {
        entity.setId(keyGenerator.getNextId());
        entities.put(entity.getId(),entity);
        return entity;
    }

    @Override
    public V update(V entity) throws EntityNotFoundException {
        Optional<V> old = findById(entity.getId());
        if(old.isEmpty()){
            throw new EntityNotFoundException(String.format("Entity with ID= %s does not exist",
                    entity.getId()));
        }
        entities.put(entity.getId(),entity);
        return entity;
    }


    @Override
    public V deleteById(K id) throws EntityNotFoundException {
        Optional<V> old = findById(id);
        if(old.isEmpty()){
            throw new EntityNotFoundException(String.format("Entity with ID= %s does not exist",
                    id));
        }
        entities.remove(id);
        return old.get();
    }

    @Override
    public long count() {
        return entities.size();
    }

    @Override
    public int createBatch(Collection<V> entityCollection) throws EntityAlreadyExistsException {
        int n = 0;
        for(V entity: entityCollection) {
            if (entities.putIfAbsent(entity.getId(), entity) != null) {
                throw new EntityAlreadyExistsException(
                        String.format("Entity with ID='%s' already exists.", entity.getId()));
            } else {
                n++;
            }
        }
        return n;
    }

    @Override
    public void drop() {
        entities.clear();
    }

}
