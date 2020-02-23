package com.example.demo.controller;

import com.example.demo.dto.JwtRequest;
import com.example.demo.dto.JwtResponse;
import com.example.demo.model.User;
import com.example.demo.model.UserModel;
import com.example.demo.repository.ApacheDSRepository;
import com.example.demo.security.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.SearchScope;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@RestController
@CrossOrigin
@Slf4j
public class JwtLDAPBasedAuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private Environment env;
    @Autowired
    ApacheDSRepository apacheDSRepository;

    @Autowired
    private LdapTemplate ldapTemplate;

    public void authenticate(final String username, final String password) throws Exception {

        try {
            log.info("username ::: " + username + "pwd::" + password);
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            // contextSource.getContext("uid=" + username + ",ou=people," + env.getRequiredProperty("ldap.partitionSuffix"), password);


            //Boolean authenticate = ldapTemplate.authenticate("ou=people,dc=springframework,dc=org", filter.toString(), password);
            log.info("authentication result ::" + auth.isAuthenticated());
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }


    @GetMapping("/people/{username}")
    public List<String> search(@PathVariable String username) {
        return ldapTemplate
                .search(
                        "ou=people,dc=springframework,dc=org",
                        "uid=" + username,
                        (AttributesMapper<String>) attrs -> (String) attrs.get("sn").get());
    }

    @RequestMapping(value = "/ldapauthenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        log.info("authentication successful done ..");

        User user = new User();
        user.setEmail(authenticationRequest.getUsername());
        user.setPassword(authenticationRequest.getPassword());
        final UserDetails userDetails = user;
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

//    private void authenticate(String username, String password) throws Exception {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//        } catch (DisabledException e) {
//            throw new Exception("USER_DISABLED", e);
//        } catch (BadCredentialsException e) {
//            throw new Exception("INVALID_CREDENTIALS", e);
//        }
//    }

    @GetMapping("/users")
    public Iterable<UserModel> getAllUsers() {
        UserModel userModel = apacheDSRepository.findAll().iterator().next();
        try {
            authenticate(userModel.getUid(), userModel.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        LdapQuery query = query()
                .searchScope(SearchScope.SUBTREE)
                .timeLimit(10)
                .countLimit(10)
                .attributes("cn")
                .base(LdapUtils.emptyLdapName())
                .where("objectclass").is("person")
                .and("sn").not().is("lastName")
                .and("sn").like("h*n")
                .and("uid").isPresent();
        return apacheDSRepository.findAll(query);
    }
}