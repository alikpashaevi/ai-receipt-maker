package alik.receiptmaker.user;

import alik.receiptmaker.user.persistence.Role;
import alik.receiptmaker.user.persistence.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepo roleRepo;

    public Set<Role> getRole(Long id) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepo.findById(id).orElseThrow(() -> new RuntimeException("Role not found")));
        return roles;
    }

}