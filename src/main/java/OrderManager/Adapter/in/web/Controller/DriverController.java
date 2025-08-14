package OrderManager.Adapter.in.web.Controller;

import OrderManager.Application.Service.DriverService;
import OrderManager.Database.DatabaseConnection;
import OrderManager.Domain.Model.User;
import OrderManager.Domain.Model.UserAddress;
import OrderManager.Shared.Dto.DriverDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import OrderManager.Domain.Model.Driver;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/Driver")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService){
        this.driverService = driverService;
    }

    @PostMapping
    public ResponseEntity<Driver> CreateDriver(@RequestBody Driver driver){
        Optional<Driver> result = driverService.CreateDriver(driver);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Driver> GetDriver(@PathVariable UUID driverId){
        Optional<Driver> result = driverService.FindDriver(driverId);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Driver>> GetAllDrivers(){
        Optional<List<Driver>> drivers =Optional.of(driverService.FindAllDrivers());
        return drivers.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping
    public ResponseEntity<Driver> UpdateDriver(@RequestBody Driver driver){
        Optional<Driver> result = driverService.CreateDriver(driver);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> DeleteDriver(@PathVariable UUID driverId){
        boolean isDeleted = driverService.DeleteDriver(driverId);
        if(isDeleted)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }

}
