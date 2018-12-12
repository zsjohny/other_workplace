

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url '/employee/1'
    }
    response {
        status 200
        body([
                "message":"Employee{name='ARUN', id=1}"

        ])
        headers {
            header('Content-Type': 'application/json;charset=UTF-8')
        }
    }
}
