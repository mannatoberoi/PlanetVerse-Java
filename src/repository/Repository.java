
package repository;

import java.util.List;

public interface Repository<T, ID> {

    boolean save(T entity);

    T findById(ID id);

    List<T> findAll();

    boolean delete(ID id);
}
