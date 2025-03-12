/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yosoy
 */
public abstract class GenericDAO<T> {
    List<T> datastore;

    // Constructor que inicializa el 'datastore' con una lista de entidades
    public GenericDAO(List<T> datastore) {
        this.datastore = datastore;
    }

    // Método para agregar una nueva entidad al datastore
    public void create(T entity) {
        datastore.add(entity);
    }

    // Retorna una copia de todas las entidades almacenadas en el datastore
    public List<T> findAll() {
        return new ArrayList<>(datastore);
    }

    // Busca una entidad por índice; retorna null si el índice es inválido
    public T findById(int index) {
        if (index >= 0 && index < datastore.size()) {
            return datastore.get(index);
        } else {
            return null;
        }
    }

    // Actualiza una entidad en un índice específico; retorna false si el índice es inválido
    public boolean update(int index, T newEntity) {
        if (index >= 0 && index < datastore.size()) {
            datastore.set(index, newEntity);
            return true;
        } else {
            return false;
        }
    }

    // Elimina una entidad en un índice específico; retorna false si el índice es inválido
    public boolean delete(int index) {
        if (index >= 0 && index < datastore.size()) {
            datastore.remove(index);
            return true;
        } else {
            return false;
        }
    }
}
