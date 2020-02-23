package com.example.demo.repository;

import com.example.demo.model.UserModel;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApacheDSRepository extends LdapRepository<UserModel> {
}