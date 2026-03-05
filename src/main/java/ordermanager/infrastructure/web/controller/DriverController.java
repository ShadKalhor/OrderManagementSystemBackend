package ordermanager.infrastructure.web.controller;

import ordermanager.domain.model.DriverDomain;
import ordermanager.infrastructure.service.DriverService;
import ordermanager.infrastructure.web.dto.driver.CreateDriverRequest;
import ordermanager.infrastructure.web.dto.driver.DriverResponse;
import ordermanager.infrastructure.web.dto.driver.UpdateDriverRequest;
import ordermanager.infrastructure.mapper.DriverMapper;
import ordermanager.infrastructure.web.exception.ErrorStructureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService driverService;
    private final DriverMapper driverMapper;

    public DriverController(DriverService driverService, DriverMapper driverMapper){
        this.driverService = driverService;
        this.driverMapper = driverMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverResponse CreateDriver(@Valid @RequestBody CreateDriverRequest driverBody){

        DriverDomain driver = driverMapper.createDomain(driverBody);
        return driverService.CreateDriver(driver)
                .map(driverMapper::domainToResponse)
                .getOrElseThrow(ErrorStructureException::new);

    }

    @PutMapping("/{driverId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public DriverResponse UpdateDriver(@PathVariable UUID driverId,@Valid @RequestBody UpdateDriverRequest driverBody){

        DriverDomain driver = driverMapper.updateDomain(driverBody);
        return driverService.UpdateDriver(driverId,driver).map(driverMapper::domainToResponse).getOrElseThrow(ErrorStructureException::new);

    }

    @GetMapping("/{driverId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public DriverResponse GetDriver(@PathVariable UUID driverId){

        return driverService.GetDriverById(driverId).map(driverMapper::domainToResponse).getOrElseThrow(ErrorStructureException::new);

    }


    //TODO:Double Check Endpoint Later.
    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<DriverResponse> GetAllDrivers(){

        return driverService.GetAllDrivers().stream()
                .map(driverMapper::domainToResponse)
                .toList();
    }


    @DeleteMapping("/{driverId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void DeleteDriver(@PathVariable UUID driverId){

        driverService.DeleteDriver(driverId).getOrElseThrow(ErrorStructureException::new);

    }

}
