package com.ssafy.gatewayservice.jwt;

import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<SavedToken,String> {
}
