package ducnh.springboot.controller;

import ducnh.springboot.dto.RequestOffDTO;
import ducnh.springboot.enumForEntity.Status;
import ducnh.springboot.model.entity.RequestOffEntity;
import ducnh.springboot.service.IRequestOffService;
import ducnh.springboot.specifications.FilterSpecification;
import ducnh.springboot.specifications.SearchCriteria;
import ducnh.springboot.utils.DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/request-off")
public class RequestOffRestController {

    @Autowired
    IRequestOffService requestOffService;

    @GetMapping("/find-all")
    @Secured({"HR", "STAFF"})
    public List<RequestOffDTO> findAll(@RequestBody Map<String, Timestamp> json,
                                       @RequestParam(value = "status", required = false) Optional<String> status) throws ParseException {
        Specification<RequestOffEntity> spec = new FilterSpecification<>(new SearchCriteria("id", SearchCriteria.Operation.GREATER, 0));
        if (json.get("start") == null) {
            json.put("start",Timestamp.valueOf("2018-08-29")) ;
        }
        if (json.get("end") == null) {
            json.put("end",Timestamp.valueOf("2035-08-29")) ;
        }
        if(json.get("start") != null || json.get("end") != null){
            FilterSpecification<RequestOffEntity> spec1 = new FilterSpecification<>(
                    new SearchCriteria("createdDate", SearchCriteria.Operation.BETWEEN,
                            new SimpleDateFormat(DateFormat.y_Md).parse(json.get("start").toString()),
                            new SimpleDateFormat(DateFormat.y_Md).parse(json.get("end").toString())));
            spec = spec.and(spec1);
        }
        if (status.isPresent()) {
            FilterSpecification<RequestOffEntity> spec2 = new FilterSpecification<>(new SearchCriteria("status", SearchCriteria.Operation.EQUAL, Status.valueOf(status.get())));
            spec = spec.and(spec2);
        }
        return requestOffService.findAll(spec);
    }

    @PostMapping
    public RequestOffDTO createNewRequest(@RequestBody RequestOffEntity entity) {
        System.out.println("------------------------------------------\n------------------------------------------");
        if (entity == null)
            System.out.println("entity null");
        else
            System.out.println("user id :" + "\n" + entity);
        return requestOffService.save(entity);
    }

    @PutMapping("/update-status")
    @Secured({"PM", "HR"})
    public List<RequestOffDTO> updateStatus(@RequestParam String status, @RequestBody Long[] ids) {
        return requestOffService.updateStatus(ids, status);
    }

}
