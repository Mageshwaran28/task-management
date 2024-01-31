// package com.zerp.taskmanagement.security;

// import java.util.HashSet;
// import java.util.Set;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;

// import com.zerp.taskmanagement.dbentity.User;
// import com.zerp.taskmanagement.dbrepository.UserRepository;

// public class UserDetail implements UserDetailsService {

//     @Autowired
//     UserRepository userRepository;

//     @Override
//     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//         User user = userRepository.findByEmailIgnoreCase(username);
//         if (user == null) {
//             throw new UsernameNotFoundException("User " + username + " not found");
//         }

//         Set<GrantedAuthority> authorities = new HashSet<>();
//         authorities.add(new SimpleGrantedAuthority(user.getRole().getRole()));
        
//         return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
//     }

// }
