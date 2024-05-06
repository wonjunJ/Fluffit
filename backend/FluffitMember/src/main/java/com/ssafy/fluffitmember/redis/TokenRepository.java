package com.ssafy.fluffitmember.redis;

import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token,String> {
}
