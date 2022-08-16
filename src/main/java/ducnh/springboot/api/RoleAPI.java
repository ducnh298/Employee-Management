package ducnh.springboot.api;

import ducnh.springboot.dto.RoleDTO;
import ducnh.springboot.projection.IRoleCount;
import ducnh.springboot.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleAPI {
    @Autowired
    IRoleService roleService;

    @PostMapping
    @Secured("HR")
    public RoleDTO addRole(@RequestBody RoleDTO role) {
        return roleService.save(role);
    }

    @GetMapping("/{id}")
    @Secured("HR")
    public RoleDTO findById(@PathVariable Long id) {
        return roleService.findById(id);
    }

    @GetMapping("/find-by-name/page{page}")
    @Secured("HR")
    public Slice<RoleDTO> findByName(@RequestParam String name, @PathVariable int page) {
        return roleService.findByNameContaining(name, PageRequest.of(page-1,5));
    }

    @GetMapping("/count-em")
    @Secured("HR")
    public List<IRoleCount> CountEmByRole() {
        return roleService.RoleCountEm();
    }
}
