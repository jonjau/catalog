package com.jonjauhari.catalog.repository;

import java.util.List;

/**
 * Stripped-down version of JPA's CrudRepository.
 * @param <T>
 * @param <ID>
 */
public interface Repository<T, ID> {

    void save(T entity);

    T findById(ID id);

    List<T> findAll();

    void delete(T entity);
}
