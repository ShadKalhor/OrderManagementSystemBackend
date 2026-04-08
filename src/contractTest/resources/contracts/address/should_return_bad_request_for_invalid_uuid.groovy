package contracts.address

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 404 for invalid user"

    request {
        method POST()
        url "/addresses"
        headers {
            contentType(applicationJson())
        }
        body(
                userId: "9999-999999999999",
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
        status BAD_REQUEST()
        headers {
            contentType(applicationJson())
        }
        body(
                message: "INVALID_REQUEST"
        )
    }
}