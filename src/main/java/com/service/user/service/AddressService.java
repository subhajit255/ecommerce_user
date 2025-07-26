package com.service.user.service;

import com.service.user.entities.UserAddress;
import com.service.user.request.UserAddressRequest;

import java.util.List;
import java.util.UUID;

public interface AddressService {
    UserAddress addAddress(UserAddressRequest userAddressRequest, UUID userId);
    List<UserAddress> addresses(UUID userId);
    UserAddress updateAddress(UserAddressRequest userAddressRequest, UUID addressId);
    void deleteAddress(UUID addressId);
    boolean setAddressDefault(UUID addressId);
}
