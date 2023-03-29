package ducnh.springboot.controller;

import ducnh.springboot.dto.RoleDTO;
import ducnh.springboot.projection.IRoleCount;
import ducnh.springboot.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleRestController {
    @Autowired
    IRoleService roleService;

    @GetMapping("/{id}")
    @Secured("ROLE_HR")
    @Cacheable("role")
    public ResponseEntity<RoleDTO> findById(@PathVariable Long id) {
        return new ResponseEntity<>(roleService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/find")
    @Secured("ROLE_HR")
    @Cacheable("role")
    public ResponseEntity<Slice<RoleDTO>> findByName(@RequestParam String name, @RequestParam int page) {
        return new ResponseEntity<>(roleService.findByNameContaining(name, PageRequest.of(page - 1, 5)), HttpStatus.OK);
    }

    @GetMapping("/count-em")
    @Secured("ROLE_HR")
    @Cacheable("role")
    public ResponseEntity<List<IRoleCount>> CountEmByRole() {
        return new ResponseEntity<>(roleService.RoleCountEm(), HttpStatus.OK);
    }

    @Secured("ROLE_HR")
    @PostMapping
    @CachePut("role")
    public ResponseEntity<RoleDTO> addRole(@RequestBody RoleDTO role) {
        return new ResponseEntity<>(roleService.save(role), HttpStatus.OK);
    }

    @DeleteMapping
    @Secured("ROLE_HR")
    @CacheEvict("role")
    public ResponseEntity<String> deleteRole(@RequestBody Long[] ids) {

        return new ResponseEntity<>("deleted successfully" + roleService.delete(ids) + " roles", HttpStatus.OK);
    }
}
