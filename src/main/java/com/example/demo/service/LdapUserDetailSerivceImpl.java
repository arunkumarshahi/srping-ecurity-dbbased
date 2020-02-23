package com.example.demo.service;

import com.example.demo.mapper.UserModelMapper;
import com.example.demo.model.User;
import com.example.demo.model.UserModel;
import com.example.demo.repository.ApacheDSRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.SearchScope;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Profile("LDAP")
@Service
@Slf4j
public class LdapUserDetailSerivceImpl implements UserDetailsService {
    private final ApacheDSRepository apacheDSRepository;
    ;

    public LdapUserDetailSerivceImpl(ApacheDSRepository apacheDSRepository) {
        this.apacheDSRepository = apacheDSRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        LdapQuery query = query()
                .searchScope(SearchScope.SUBTREE)
                .timeLimit(10)
                .countLimit(10)
                .attributes("cn")
                .base(LdapUtils.emptyLdapName())
                .where("objectclass").is("person")
                .and("uid").like(username)
                .and("uid").isPresent();
        UserModel usermodel = apacheDSRepository.findOne(query).get();
        User user = UserModelMapper.INSTANCE.userModelToUser(usermodel);

        log.info("user in user detail service ::" + user);
        return user;
    }

}

