package ducnh.springboot.controller;

import ducnh.springboot.dto.RequestWorkingHourDTO;
import ducnh.springboot.enumForEntity.Status;
import ducnh.springboot.model.entity.RequestWorkingHourEntity;
import ducnh.springboot.service.IRequestWorkingHourService;
import ducnh.springboot.specifications.FilterSpecification;
import ducnh.springboot.specifications.SearchCriteria;
import ducnh.springboot.utils.DateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/request-working-hour")
public class RequestWorkingHourRestController {

    @Autowired
    IRequestWorkingHourService requestWorkingHourService;

    Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/find-all")
    @Secured({"ROLE_HR", "ROLE_STAFF"})
    public List<RequestWorkingHourDTO> findAll(@RequestBody Map<String, Timestamp> json,
                                               @RequestParam(value = "status", required = false) Optional<String> status) throws ParseException {
        Specification<RequestWorkingHourEntity> spec = new FilterSpecification<>(
                new SearchCriteria("id", SearchCriteria.Operation.GREATER, 0));
        if (json.get("start") == null) {
            json.put("start", Timestamp.valueOf("2018-08-29"));
        }
        if (json.get("end") == null) {
            json.put("end", Timestamp.valueOf("2035-08-29"));
        }
        if (json.get("start") != null || json.get("end") != null) {
            FilterSpecification<RequestWorkingHourEntity> spec1 = new FilterSpecification<>(
                    new SearchCriteria("createdDate", SearchCriteria.Operation.BETWEEN,
                            new SimpleDateFormat(DateFormat.y_Md).parse(json.get("start").toString()),
                            new SimpleDateFormat(DateFormat.y_Md).parse(json.get("end").toString())));
            spec = spec.and(spec1);
        }
        if (status.isPresent()) {
            FilterSpecification<RequestWorkingHourEntity> spec2 = new FilterSpecification<>(new SearchCriteria("status", SearchCriteria.Operation.EQUAL, Status.valueOf(status.get())));
            spec = spec.and(spec2);
        }
        return requestWorkingHourService.findAll(spec);
    }

    @GetMapping("/find")
    @Secured({"ROLE_HR", "ROLE_STAFF"})
    public List<RequestWorkingHourDTO> findMyRequestWorkingHour(@RequestParam long userId){
        return requestWorkingHourService.findByUserId(userId);
    }

    @GetMapping("/find-my-request-working-hour")
    public List<RequestWorkingHourDTO> findMyRequestWorkingHour(){
        return requestWorkingHourService.findMyRequestWorkingHour();
    }

    @PostMapping
    public RequestWorkingHourDTO createNewRequest(@RequestBody RequestWorkingHourEntity entity) {
        if (entity == null)
            log.info("entity null");
        else
            log.info("user id :" + "\n" + entity);
        return requestWorkingHourService.save(entity);
    }

    @PutMapping("/update-request")
    public RequestWorkingHourDTO updateRequest(@RequestBody RequestWorkingHourEntity entity) {
        return requestWorkingHourService.save(entity);
    }

    @PutMapping("/cancel-request")
    public RequestWorkingHourDTO cancelRequest(@RequestParam Long requestId) throws ParseException {
        return requestWorkingHourService.updateStatus(new Long[]{requestId}, "CANCEL").get(0);
    }

    @PutMapping("/update-status")
    @Secured({"ROLE_PM", "ROLE_HR"})
    public ResponseEntity<List<RequestWorkingHourDTO>> approveOrRejectRequest(@RequestParam String status, @RequestBody Long[] ids) throws ParseException {
        if (status.equalsIgnoreCase("APPROVED") || status.equalsIgnoreCase("REJECT"))
            return new ResponseEntity<>(requestWorkingHourService.updateStatus(ids, status), HttpStatus.OK);
        else return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

}
