package com.example.demo.service;

import com.example.demo.mapper.UserModelMapper;
import com.example.demo.model.User;
import com.example.demo.model.UserModel;
import com.example.demo.repository.ApacheDSRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.List;

@Profile({"LDAP", "EMBDLDAP"})
@Service
@Slf4j
public class LdapUserDetailSerivceImpl implements UserDetailsService {
    private final ApacheDSRepository apacheDSRepository;
    @Autowired
    private LdapTemplate ldapTemplate;

    public LdapUserDetailSerivceImpl(ApacheDSRepository apacheDSRepository) {
        this.apacheDSRepository = apacheDSRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
//        LdapQuery query = query()
//                .searchScope(SearchScope.SUBTREE)
//                .timeLimit(10)
//                .countLimit(10)
//                .attributes("cn")
//                .base(LdapUtils.emptyLdapName())
//                .where("objectclass").is("person")
//                .and("uid").like(username)
//                .and("uid").isPresent();
//        UserModel usermodel = apacheDSRepository.findOne(query).get();
        List<UserModel> userModels = ldapTemplate
                .search(
                        "ou=people,dc=example,dc=com",
                        "uid=" + username,
                        new PersonAttributesMapper());
        User user = UserModelMapper.INSTANCE.userModelToUser(userModels.get(0));

        log.info("user in user detail service ::" + user);
        return user;
    }

    private class PersonAttributesMapper implements AttributesMapper<UserModel> {
        public UserModel mapFromAttributes(Attributes attrs) throws javax.naming.NamingException {
            UserModel person = new UserModel();
            person.setLastName((String) attrs.get("cn").get());

            log.info("last name ::" + attrs.get("cn"));
            Attribute sn = attrs.get("sn");
            if (sn != null) {
                person.setLastName((String) sn.get());
            }
            person.setUid((String) attrs.get("uid").get());
            byte[] bytes = (byte[]) attrs.get("userPassword").get();
            person.setPassword(String.valueOf(bytes));
            return person;
        }
    }
}

