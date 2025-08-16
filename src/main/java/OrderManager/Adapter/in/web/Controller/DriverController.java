package OrderManager.Adapter.in.web.Controller;

import OrderManager.Application.Service.DriverService;
import OrderManager.Shared.Dto.DriverDtos.CreateDriverRequest;
import OrderManager.Shared.Dto.DriverDtos.DriverResponse;
import OrderManager.Shared.Dto.DriverDtos.UpdateDriverRequest;
import OrderManager.Shared.Mapper.DriverMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import OrderManager.Domain.Model.Driver;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/Driver")
public class DriverController {

    private final DriverService driverService;
    private final DriverMapper driverMapper;

    public DriverController(DriverService driverService, DriverMapper driverMapper){
        this.driverService = driverService;
        this.driverMapper = driverMapper;
    }

    @PostMapping
    public ResponseEntity<DriverResponse> CreateDriver(@Valid @RequestBody CreateDriverRequest driverBody){
    /*    Optional<Driver> result = driverService.CreateDriver(driver);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    */
        var driver = driverMapper.toDomain(driverBody);
        return driverService.CreateDriver(driver)
                .map(driverMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverResponse> UpdateDriver(@PathVariable UUID driverId,@Valid @RequestBody UpdateDriverRequest driverBody){
       /* Optional<Driver> result = driverService.CreateDriver(driver);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    */
        var driverExists = driverService.FindDriver(driverId).orElse(null);

        if (driverExists == null)
            return ResponseEntity.notFound().build();

        driverMapper.update(driverExists, driverBody);
        var updatedUser = driverService.CreateDriver(driverExists);
        return updatedUser.map(driver -> ResponseEntity.ok(driverMapper.toResponse(driver))).orElseGet(() -> ResponseEntity.badRequest().build());


    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> GetDriver(@PathVariable UUID driverId){/*
        Optional<Driver> result = driverService.FindDriver(driverId);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());*/

        return driverService.FindDriver(driverId)
                .map(driverMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<DriverResponse>> GetAllDrivers(){/*
        Optional<List<Driver>> drivers =Optional.of(driverService.FindAllDrivers());
        return drivers.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    */
        var drivers = driverService.FindAllDrivers().stream()
                .map(driverMapper::toResponse)
                .toList();
        return ResponseEntity.ok(drivers);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> DeleteDriver(@PathVariable UUID driverId){
        boolean isDeleted = driverService.DeleteDriver(driverId);
        if(isDeleted)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.badRequest().build();
    }

}
