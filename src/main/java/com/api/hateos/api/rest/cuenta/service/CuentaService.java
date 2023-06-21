package com.api.hateos.api.rest.cuenta.service;

import com.api.hateos.api.rest.cuenta.exception.CuentaNotFoudException;
import com.api.hateos.api.rest.cuenta.model.Cuenta;
import com.api.hateos.api.rest.cuenta.repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    public Cuenta save(Cuenta cuenta){
        return cuentaRepository.save(cuenta);
    }
    public List<Cuenta> getAll(){
        return cuentaRepository.findAll();
    }

    public Cuenta get(Integer id){
        return cuentaRepository.findById(id).get();
    }

    public void delete(Integer id) throws CuentaNotFoudException {
        if(!cuentaRepository.existsById(id)){
            throw new CuentaNotFoudException("Cuenta no encontrada con la ID : " + id);
        }
        cuentaRepository.deleteById(id);
    }

    public Cuenta depositar(float monto , Integer id){
        cuentaRepository.updateMonto(monto,id);
        return cuentaRepository.findById(id).get();
    }

    public Cuenta retirar(float monto , Integer id){
        cuentaRepository.updateMonto(-monto,id);
        return cuentaRepository.findById(id).get();
    }


}
