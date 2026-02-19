package ordermanager.infrastructure.web.controller;

import ordermanager.infrastructure.service.DriverService;
import ordermanager.infrastructure.web.dto.driver.CreateDriverRequest;
import ordermanager.infrastructure.web.dto.driver.DriverResponse;
import ordermanager.infrastructure.web.dto.driver.UpdateDriverRequest;
import ordermanager.infrastructure.mapper.DriverMapper;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<DriverResponse> CreateDriver(@Valid @RequestBody CreateDriverRequest driverBody){

        var driver = driverMapper.toDomain(driverBody);
        return driverService.CreateDriver(driver)
                .map(driverMapper::toResponse)
                .map(ResponseEntity::ok)
                .getOrElse(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{driverId}")
    public ResponseEntity<DriverResponse> UpdateDriver(@PathVariable UUID driverId,@Valid @RequestBody UpdateDriverRequest driverBody){

        var driver = driverMapper.update(driverBody);
        return driverService.UpdateDriver(driverId, driver).map(driverMapper::toResponse)
                .map(ResponseEntity::ok)
                .getOrElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{driverId}")
    public ResponseEntity<DriverResponse> GetDriver(@PathVariable UUID driverId){

        return driverService.GetDriverById(driverId)
                .map(driverMapper::toResponse)
                .map(ResponseEntity::ok)
                .getOrElse(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<DriverResponse>> GetAllDrivers(){

        var drivers = driverService.GetAllDrivers().stream()
                .map(driverMapper::toResponse)
                .toList();
        return ResponseEntity.ok(drivers);

    }


    @DeleteMapping("/{driverId}")
    public ResponseEntity<Void> DeleteDriver(@PathVariable UUID driverId){
        boolean isDeleted = driverService.DeleteDriver(driverId);
        if(isDeleted)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.badRequest().build();
    }

}
