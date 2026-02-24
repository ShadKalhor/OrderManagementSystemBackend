package ordermanager.infrastructure.web.controller;

import ordermanager.infrastructure.service.DriverService;
import ordermanager.domain.dto.driver.CreateDriverRequest;
import ordermanager.domain.dto.driver.DriverResponse;
import ordermanager.domain.dto.driver.UpdateDriverRequest;
import ordermanager.infrastructure.mapper.DriverMapper;
import ordermanager.infrastructure.store.persistence.entity.Driver;
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

        Driver driver = driverMapper.create(driverBody);
        return driverService.CreateDriver(driver)
                .map(driverMapper::toResponse)
                .getOrElseThrow(ErrorStructureException::new);

    }

    @PutMapping("/{driverId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public DriverResponse UpdateDriver(@PathVariable UUID driverId,@Valid @RequestBody UpdateDriverRequest driverBody){

        var driver = driverMapper.update(driverBody);
        return driverService.UpdateDriver(driverId,driver).map(driverMapper::toResponse).getOrElseThrow(ErrorStructureException::new);

    }

    @GetMapping("/{driverId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public DriverResponse GetDriver(@PathVariable UUID driverId){

        return driverService.GetDriverById(driverId).map(driverMapper::toResponse).getOrElseThrow(ErrorStructureException::new);

    }


    //TODO:Double Check Endpoint Later.
    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<DriverResponse> GetAllDrivers(){

        return driverService.GetAllDrivers().stream()
                .map(driverMapper::toResponse)
                .toList();
    }


    @DeleteMapping("/{driverId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void DeleteDriver(@PathVariable UUID driverId){

        driverService.DeleteDriver(driverId).getOrElseThrow(ErrorStructureException::new);

    }

}
