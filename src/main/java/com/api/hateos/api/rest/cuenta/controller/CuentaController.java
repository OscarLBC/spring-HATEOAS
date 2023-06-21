package com.api.hateos.api.rest.cuenta.controller;

import com.api.hateos.api.rest.cuenta.model.Cuenta;
import com.api.hateos.api.rest.cuenta.model.Monto;
import com.api.hateos.api.rest.cuenta.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
//spring HATEOAS, importacion static para ahorrar linea de codigo
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cuenta")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping
    public ResponseEntity<List<Cuenta>> listarCuentas(){
        List<Cuenta> cuentas = cuentaService.getAll();

        if(cuentas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
       //agregamos link con spring HETEOAS
        for(Cuenta cuenta: cuentas){
            cuenta.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuenta.getId())).withSelfRel());
            cuenta.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuenta.getId(), null)).withRel("depositos"));
            cuenta.add(linkTo(methodOn(CuentaController.class).retirarDinero(cuenta.getId(), null)).withRel("retiros"));
            cuenta.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));
        }

        CollectionModel<Cuenta> modelo = CollectionModel.of(cuentas);
        modelo.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withSelfRel());

        //.. fin


        return new ResponseEntity<>(cuentas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> listarCuenta(@PathVariable Integer  id){
        try {
            Cuenta cuenta = cuentaService.get(id);

            //agregamos link con spring HETEOAS
            cuenta.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuenta.getId())).withSelfRel());
            cuenta.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuenta.getId(), null)).withRel("depositos"));
            cuenta.add(linkTo(methodOn(CuentaController.class).retirarDinero(cuenta.getId(), null)).withRel("retiros"));
            cuenta.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));
           //....fin

            return new ResponseEntity<>(cuenta,HttpStatus.OK);

        } catch (Exception e ){
           return ResponseEntity.noContent().build();
        }

    }
    @PostMapping
    public ResponseEntity<Cuenta> guardarCuenta(@RequestBody Cuenta cuenta){
        Cuenta cuentaBBDD = cuentaService.save(cuenta);

        //agregamos link con spring HETEOAS
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuentaBBDD.getId())).withSelfRel());
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuentaBBDD.getId(), null)).withRel("depositos"));
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).retirarDinero(cuentaBBDD.getId(), null)).withRel("retiros"));
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));
        return ResponseEntity.created(linkTo(methodOn(CuentaController.class).listarCuenta(cuentaBBDD.getId())).toUri()).body(cuentaBBDD);
        //... fin
    }

    @PutMapping
    public ResponseEntity<Cuenta> editarCuenta(@RequestBody Cuenta cuenta){
        Cuenta cuentaBBDD = cuentaService.save(cuenta);
        //agregamos link con spring HETEOAS
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuentaBBDD.getId())).withSelfRel());
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuentaBBDD.getId(), null)).withRel("depositos"));
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).retirarDinero(cuentaBBDD.getId(), null)).withRel("retiros"));
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));
        //... fin
        return new ResponseEntity<>(cuentaBBDD,HttpStatus.OK);
    }
  @PatchMapping("/{id}/deposito")
  public ResponseEntity<Cuenta> depositarDinero(@PathVariable Integer id, @RequestBody Monto monto){
        Cuenta cuentaBBDD = cuentaService.depositar(monto.getMonto(),id);
      //agregamos link con spring HETEOAS
      cuentaBBDD.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuentaBBDD.getId())).withSelfRel());
      cuentaBBDD.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuentaBBDD.getId(), null)).withRel("depositos"));
      cuentaBBDD.add(linkTo(methodOn(CuentaController.class).retirarDinero(cuentaBBDD.getId(), null)).withRel("retiros"));
      cuentaBBDD.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));
      //... fin
    return new ResponseEntity<>(cuentaBBDD,HttpStatus.OK);
 }

    @PatchMapping("/{id}/retiro")
    public ResponseEntity<Cuenta> retirarDinero(@PathVariable Integer id, @RequestBody Monto monto){
        Cuenta cuentaBBDD = cuentaService.retirar(monto.getMonto(),id);
        //agregamos link con spring HETEOAS
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuentaBBDD.getId())).withSelfRel());
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuentaBBDD.getId(), null)).withRel("depositos"));
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).retirarDinero(cuentaBBDD.getId(), null)).withRel("retiros"));
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));
        //... fin
        return new ResponseEntity<>(cuentaBBDD,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCuenta(@PathVariable Integer id){
        try {
            cuentaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e){
            return ResponseEntity.noContent().build();
        }

    }




}
