package ducnh.springboot.controller;

import ducnh.springboot.dto.RequestOffDTO;
import ducnh.springboot.enumForEntity.Status;
import ducnh.springboot.model.entity.RequestOffEntity;
import ducnh.springboot.service.IRequestOffService;
import ducnh.springboot.specifications.FilterSpecification;
import ducnh.springboot.specifications.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/request-off")
public class RequestOffRestController {

    @Autowired
    IRequestOffService requestOffService;

    @GetMapping("/find-all")
    @Secured("HR")
    public List<RequestOffDTO> findAll(@RequestParam(value = "start", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start,
                                       @RequestParam(value = "end", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end,
                                       @RequestParam(value = "status", required = false) Optional<String> status) {
        Specification<RequestOffEntity> spec = new FilterSpecification<>(new SearchCriteria("id", SearchCriteria.Operation.GREATER,0));
        if (start.isPresent()) {
            FilterSpecification<RequestOffEntity> spec1 = new FilterSpecification<>(new SearchCriteria("createdDate", SearchCriteria.Operation.GREATER_, new Timestamp(start.get().getTime())));
            spec =  spec.and(spec1);
        }
        if (end.isPresent()) {
            FilterSpecification<RequestOffEntity> spec2 = new FilterSpecification<>(new SearchCriteria("createdDate", SearchCriteria.Operation.LESS, new Timestamp(end.get().getTime())));
            spec =  spec.and(spec2);
        }
        if(status.isPresent()){
            FilterSpecification<RequestOffEntity> spec3 = new FilterSpecification<>(new SearchCriteria("status", SearchCriteria.Operation.EQUAL, Status.valueOf(status.get())));
            spec =  spec.and(spec3);
        }
        return requestOffService.findAll(spec);

    }

    @PostMapping
    @Secured("HR")
    public RequestOffDTO createNewRequest(@RequestBody RequestOffEntity entity) {
        System.out.println("------------------------------------------\n------------------------------------------");
        if(entity==null)
            System.out.println("entity null");
        else
            System.out.println("user id :"+"\n"+entity);
        return requestOffService.save(entity);
    }

    @PutMapping("/update-status")
    @Secured("HR")
    public List<RequestOffDTO> updateStatus(@RequestParam String status,@RequestBody Long[] ids){
        return requestOffService.updateStatus(ids,status);
    }

}
