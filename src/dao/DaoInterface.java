package dao;

import java.util.List;
import java.util.Optional;

public interface DaoInterface<T> {

    Optional<T> get(long id);

    List<T> getAll();

    void showAll();

    void showAllActive();

    void create(T t);

    void update(long t, Object[] params);

    void delete(long t);

    public void softDelete(long id);

    public void softDeleteAll(long id);

    public void softDeleteByStatus(long id, int state);

    public void close();
}
