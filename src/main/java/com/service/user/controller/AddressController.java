package com.service.user.controller;

import com.service.user.entities.UserAddress;
import com.service.user.request.UserAddressRequest;
import com.service.user.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {
    private final AddressService addressService;
    public AddressController(AddressService addressService){
        this.addressService = addressService;
    }
    @PostMapping("/add")
    public ResponseEntity<?> addAddress(@Valid @RequestBody UserAddressRequest userAddressRequest, BindingResult bindingResult, @RequestAttribute UUID userId){
        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(
                    error -> errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.unprocessableEntity().body(
                    Map.of(
                            "status", false,
                            "errors", errors
                    )
            );
        }
        try{
//            userId = UUID.fromString("7f6357d0-cc65-4a35-bf67-0061658f2148");
            UserAddress isAddressAdded = addressService.addAddress(userAddressRequest, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(isAddressAdded);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of(
                        "status", false,
                        "message", e.getMessage() + "--" + e.getClass()
                )
            );
        }
    }
    @GetMapping("")
    public ResponseEntity<?> getAddressesOfUser(@RequestAttribute UUID userId){
        System.out.println("user_id " + userId);
        try{
            List<UserAddress> allAddresses = addressService.addresses(userId);
            return ResponseEntity.ok(allAddresses);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of(
                            "status", false,
                            "message", e.getMessage() + " -- " + e.getClass()
                    )
            );
        }
    }
    @PutMapping("/update/{addressId}")
    public ResponseEntity<?> updateAddress(@Valid @RequestBody UserAddressRequest userAddressRequest,
                                           BindingResult bindingResult,
                                           @PathVariable UUID addressId,
                                           @RequestAttribute UUID userId){
        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(
                    error -> errors.put(error.getField(), error.getDefaultMessage())
            );
        }
        try{
            UserAddress isAddressUpdated = addressService.updateAddress(userAddressRequest, addressId);
            return ResponseEntity.ok(isAddressUpdated);
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of(
                            "status", false,
                            "message", e.getMessage() + " -- " + e.getClass()
                    )
            );
        }
    }

    @DeleteMapping("{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable UUID addressId, @RequestAttribute UUID userId){
        try{
            addressService.deleteAddress(addressId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    Map.of(
                            "status", true,
                            "message", "address deleted"
                    )
            );
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of(
                            "status", false,
                            "message", e.getMessage() + " -- " + e.getClass()
                    )
            );
        }
    }

    @GetMapping("{addressId}")
    public ResponseEntity<?> setAddressDefault(@PathVariable UUID addressId, @RequestAttribute UUID userId){
        try{
            Boolean isAddressDefault = addressService.setAddressDefault(addressId, userId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    Map.of(
                            "status", true,
                            "message", "address saved as default address"
                    )
            );
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of(
                            "status", false,
                            "message", e.getMessage() + " -- " + e.getClass()
                    )
            );
        }
    }
}
