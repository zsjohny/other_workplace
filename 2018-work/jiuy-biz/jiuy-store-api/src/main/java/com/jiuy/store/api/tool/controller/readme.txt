规范：
1. 如果一个实体类的某个字段，在任何情况下都不参与json序列化，标记为@JsonIgnore。
2. 所有controller的方法默认不允许返回json，除非这个方法上有ResponseBody注解，或者这个方法的返回值类型是JsonResponse或其子类。

========================================================================================================================

RESTful URL可能是一个伪命题。

管理后台URL的设计，考虑按以下两个方向：
1. RESTful resource：URI必须表示某个资源，不允许出现动词。CRUD分别对应方法POST、GET、PUT、DELETE。
2. RPC-like operation：URI可以出现动词。方法全部使用POST。

以下内容引用自 http://stackoverflow.com/questions/1619152/how-to-create-rest-urls-without-verbs

General principles for good URI design:

    Don't use query parameters to alter state
    Don't use mixed-case paths if you can help it; lowercase is best
    Don't use implementation-specific extensions in your URIs (.php, .py, .pl, etc.)
    Don't fall into RPC with your URIs
    Do limit your URI space as much as possible
    Do keep path segments short
    Do prefer either /resource or /resource/; create 301 redirects from the one you don't use
    Do use query parameters for sub-selection of a resource; i.e. pagination, search queries
    Do move stuff out of the URI that should be in an HTTP header or a body

(Note: I did not say "RESTful URI design"; URIs are essentially opaque in REST.)

General principles for HTTP method choice:

    Don't ever use GET to alter state; this is a great way to have the Googlebot ruin your day
    Don't use PUT unless you are updating an entire resource
    Don't use PUT unless you can also legitimately do a GET on the same URI
    Don't use POST to retrieve information that is long-lived or that might be reasonable to cache
    Don't perform an operation that is not idempotent with PUT
    Do use GET for as much as possible
    Do use POST in preference to PUT when in doubt
    Do use POST whenever you have to do something that feels RPC-like
    Do use PUT for classes of resources that are larger or hierarchical
    Do use DELETE in preference to POST to remove resources
    Do use GET for things like calculations, unless your input is large, in which case use POST

General principles of web service design with HTTP:

    Don't put metadata in the body of a response that should be in a header
    Don't put metadata in a separate resource unless including it would create significant overhead
    Do use the appropriate status code
        201 Created after creating a resource; resource must exist at the time the response is sent
        202 Accepted after performing an operation successfully or creating a resource asynchronously
        400 Bad Request when someone does an operation on data that's clearly bogus; for your application this could be a validation error; generally reserve 500 for uncaught exceptions
        401 Unauthorized when someone accesses your API either without supplying a necessary Authorization header or when the credentials within the Authorization are invalid; don't use this response code if you aren't expecting credentials via an Authorization header.
        403 Forbidden when someone accesses your API in a way that might be malicious or if they aren't authorized
        405 Method Not Allowed when someone uses POST when they should have used PUT, etc
        413 Request Entity Too Large when someone attempts to send you an unacceptably large file
        418 I'm a teapot when attempting to brew coffee with a teapot
    Do use caching headers whenever you can
        ETag headers are good when you can easily reduce a resource to a hash value
        Last-Modified should indicate to you that keeping around a timestamp of when resources are updated is a good idea
        Cache-Control and Expires should be given sensible values
    Do everything you can to honor caching headers in a request (If-None-Modified, If-Modified-Since)
    Do use redirects when they make sense, but these should be rare for a web service
