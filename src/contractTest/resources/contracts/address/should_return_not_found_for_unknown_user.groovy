package contracts.address

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return not found for unknown user"

    request {
        method POST()
        url "/addresses"
        headers {
            contentType(applicationJson())
        }
        body(
                userId: "99999999-9999-9999-9999-999999999999",
                name: "addressName",
                city: "erbil",
                description: "my Description",
                type: "House",
                street: "1st Street",
                residentialNo: "405",
                isPrimary: true
        )
    }

    response {
        status NOT_FOUND()
        headers {
            contentType(applicationJson())
        }
        body(
                message: "User Not Found"
        )
    }
}