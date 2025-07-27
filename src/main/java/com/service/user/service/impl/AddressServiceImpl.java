package com.service.user.service.impl;

import com.service.user.entities.User;
import com.service.user.entities.UserAddress;
import com.service.user.repository.AddressRepository;
import com.service.user.request.UserAddressRequest;
import com.service.user.service.AddressService;
import com.service.user.service.AuthService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    public final AuthService authService;
    public AddressServiceImpl(AddressRepository addressRepository, AuthService authService){
        this.addressRepository = addressRepository;
        this.authService = authService;
    }
    @Override
    public UserAddress addAddress(UserAddressRequest userAddressRequest, UUID userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setType(userAddressRequest.getAddressType());
        userAddress.setFullAddress(userAddressRequest.getFullAddress());
        userAddress.setLatitude(userAddressRequest.getLatitude());
        userAddress.setLongitude(userAddressRequest.getLongitude());
        userAddress.setCountry(userAddressRequest.getCountry());
        userAddress.setState(userAddressRequest.getState());
        userAddress.setCity(userAddressRequest.getCity());
        userAddress.setZipCode(userAddressRequest.getZipCode());
        userAddress.setUser(authService.getUser(userId));
        // save the entry
        return addressRepository.save(userAddress);
    }

    @Override
    public List<UserAddress> addresses(UUID userId) {
        User user = authService.getUser(userId);
        return addressRepository.findAll().stream().filter(
                address -> address.getUser() == user
        ).collect(Collectors.toList());
    }

    @Override
    public UserAddress updateAddress(UserAddressRequest userAddressRequest, UUID addressId) {
        UserAddress userAddress = addressRepository.findById(addressId).orElseThrow(
                () -> new RuntimeException("address not found with that given id " + addressId)
        );
        userAddress.setType(userAddressRequest.getAddressType());
        userAddress.setFullAddress(userAddressRequest.getFullAddress());
        userAddress.setLatitude(userAddressRequest.getLatitude());
        userAddress.setLongitude(userAddressRequest.getLongitude());
        userAddress.setCountry(userAddressRequest.getCountry());
        userAddress.setState(userAddressRequest.getState());
        userAddress.setCity(userAddressRequest.getCity());
        userAddress.setZipCode(userAddressRequest.getZipCode());

        return addressRepository.save(userAddress);
    }

    @Override
    public void deleteAddress(UUID addressId) {
        UserAddress userAddress = addressRepository.findById(addressId).orElseThrow(
                () -> new RuntimeException("address not found with that given id " + addressId)
        );
        addressRepository.delete(userAddress);
    }

    @Override
    public boolean setAddressDefault(UUID addressId, UUID userId) {
        UserAddress userAddress = addressRepository.findById(addressId).orElseThrow(
                () -> new RuntimeException("address not found with that given id " + addressId)
        );
        // check and update if any other address is set to default
        addressRepository.findAll().stream()
                        .filter(
                                address -> address.getUser().equals(authService.getUser(userId))
                        ).peek(
                                addr -> addr.setDefault(false)
                        )
//                      .map(addr -> {
//                            addr.setDefault(false);
//                            return addr;
//                        })
                        .collect(Collectors.toList());
        userAddress.setDefault(true);
        addressRepository.save(userAddress);
        return true;
    }
}
