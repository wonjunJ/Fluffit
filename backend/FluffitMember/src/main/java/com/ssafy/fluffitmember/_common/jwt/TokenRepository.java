package com.ssafy.fluffitmember._common.jwt;

import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<SavedToken,String> {
}
