package com.ssafy.gatewayservice.jwt;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<SavedToken,String> {
}
