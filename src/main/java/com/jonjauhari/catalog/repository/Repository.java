package com.jonjauhari.catalog.repository;

import java.util.List;

/**
 * Stripped-down version of JPA's CrudRepository. Implementations of this interface are assumed
 * to have a auto-incrementing primary key column
 * @param <T> type of the entity
 * @param <ID> type of the id attribute in the entity, corresponds to a primary key
 */
public interface Repository<T, ID> {

    /**
     * inserts the given entity to the database, if the id of the entity is not set,
     * otherwise this functions as an update function
     * @param entity entity to insert/update
     */
    void save(T entity);

    /**
     * @param id the unique id of the entity to search for
     * @return the entity if found, else null
     */
    T findById(ID id);

    /**
     * @return list of all the entities
     */
    List<T> findAll();

    /**
     * Delete an entity, if the entity does not exist, this is a no-op
     * @param entity entity to delete
     */
    void delete(T entity);
}
