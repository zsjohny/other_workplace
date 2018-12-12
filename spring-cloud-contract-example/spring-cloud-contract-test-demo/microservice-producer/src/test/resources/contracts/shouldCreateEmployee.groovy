

import org.springframework.cloud.contract.spec.Contract

Contract.make {

    request {
        method 'POST'
        url '/employee/create'
        body("""
            {
                "name":"ARUN",
                "id":1
            }
            """)
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 200
        body("""
            {
                "message": "Saved details of employee ARUN",
            }
            """)
        headers {
            header('Content-Type': 'application/json;charset=UTF-8')
        }
    }
}