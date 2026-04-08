package contracts.address

import org.springframework.cloud.contract.spec.Contract


Contract.make {
    description "should return bad request when user is unknown"

    request {
        method POST()
        url "/addresses"
        headers {
            contentType(applicationJson())
        }
        body(
                userId: "99999999-9999-9999-9999-999999999998",
                name: "namely",
                city: "erbil",
                description: "my Description",
                type: "house",
                street: "405",
                residentialNo: "102",
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