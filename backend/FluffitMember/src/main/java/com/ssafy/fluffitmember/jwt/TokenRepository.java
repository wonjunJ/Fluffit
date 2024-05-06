package com.ssafy.fluffitmember.jwt;

import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<SavedToken,String> {
}
