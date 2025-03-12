/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVICE;

import DAO.GenericDAO;
import java.util.List;

/**
 *
 * @author yosoy
 */
public abstract class GenericService<T> {
     protected GenericDAO<T> dao;

    public GenericService(GenericDAO<T> dao) {
        this.dao = dao;
    }

    public void save(T entity) {
        dao.create(entity);
    }

    public List<T> findAll() {
        return dao.findAll();
    }

    public T findById(int id) {
        return dao.findById(id);
    }

    public boolean update(int id, T entity) {
        return dao.update(id, entity);
    }

    public boolean delete(int id) {
        return dao.delete(id);
    }
}
