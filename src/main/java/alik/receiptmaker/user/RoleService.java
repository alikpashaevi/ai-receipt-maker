package alik.receiptmaker.user;

import alik.receiptmaker.user.persistence.Role;
import alik.receiptmaker.user.persistence.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepo roleRepo;

    public Role getRole(Long id) {
        return roleRepo.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
    }

}