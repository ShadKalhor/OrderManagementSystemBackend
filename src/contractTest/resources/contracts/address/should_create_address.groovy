package contracts.address
import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should create address"

    request {
        method POST()
        url "/addresses"
        headers {
            contentType(applicationJson())
        }
        body(
                userId: "112961f9-462e-49bd-85dd-b56041ec65d2",
                name: "addressName",
                city: "erbil",
                description: "my Description",
                type: "house",
                street: "405",
                residentialNo: "102",
                isPrimary: true
        )
    }

    response {
        status CREATED()
    }
}
