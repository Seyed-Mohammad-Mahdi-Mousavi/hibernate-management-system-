package ir.maktabSharif.repository;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T> {
    void saveOrUpdate(T object);
    void delete(Integer id) throws Exception;
    Optional<T> findById(Integer id);
    List<T> getAll() throws Exception;
}
