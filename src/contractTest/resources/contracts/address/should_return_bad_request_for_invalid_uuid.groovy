package contracts.address


import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return bad request for invalid uuid"

    request {
        method POST()
        url "/addresses"
        headers {
            contentType(applicationJson())
        }
        body(
                userId: "not-a-uuid",
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
        status BAD_REQUEST()
        headers {
            contentType(applicationJson())
        }
        body(
                message: "INVALID_REQUEST"
        )
    }
}