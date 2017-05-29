# Spring Boot Web Skeleton

Welcome to the Spring Boot Web Skeleton project.
The idea of this project is to provide a boilerplate implementation
for modern Enterprise Java Web Applications based on Spring Boot.

Another goal of this project is to enable less-experienced developers to self-educate themselves on how to use Spring Boot.

Whatever you plan to implement: Using this skeleton, you can ramp up 
your next Spring Boot project in almost no time.

## Bugs

Please beware: For the time being, this is *BETA* software. The following bugs are known:

- The first requests encountering an error results in a 0 byte response
- Subsequent errors result in a response that lacks the last } of the JSON response message

## Features

- Domain driven design
- Undertow high-performance web-server
- Tomcat (if you exclude the Undertow dependency)
- YAML based application configuration & profiles
- Lombok: Auto-generated getters, setters, log, toString, equals
- SLF4J logging facade
- JWT / token based authentication
- Authorization layer with Users, Client, Roles, Permissions
- Easy to extend User/Client domain data entities
- Spring Security AuditAware for User, UserData, Client, ClientData
- RESTful web services, Jackson automated JSON object mapper
- Pre-configured web security
- Hibernate, JPA repositories
- UUID based record identification
- HirakiCP database connection pooling
- Pre-configured PostgreSQL database connectivity (+Docker)
- Liquibase - source control for your database
- Swagger based automated API documentation generation
- Springfox Swagger documentation UI

## Integration

#### How to run the PostgreSQL database server?

Just install [docker](https://docs.docker.com/engine/installation/) and [docker-compose](https://docs.docker.com/compose/install/) then run:

Attached to current shell (you'll see the logs):

> `docker-compose -f docker/postgresql.yml up`

In daemon-mode:

> `docker-compose -f docker/postgresql.yml up -d`

PostgreSQL will start in an isolated linux container on port 5432. 
The isolated container's internal port will be forwarded to the local host machine port. Thus, you can connect to the PostgreSQL database server using `tcp://localhost:5432`
 
#### How to create the default schema "skeleton"?

Download and install pgAdmin 4 (platform independent).
Connect to your local PostgreSQL database (see above).

Navigate to: Servers > postgres-local > Databases 
Right-click and click: Create > Database...
Enter Database name: skeleton
Click "Save".

#### How to build & run the project?

Either:
 
> `./mvnw spring-boot:run`

Or using your IDE.
 
#### I want my own nice banner on startup. How?
 
Generate a nice title banner using [patorjk.com ASCII art generator](http://patorjk.com/software/taag/#p=display&h=0&v=0&w=%20&f=ANSI%20Shadow&t=%20s%20k%20e%20l%20e%20t%20o%20n) 
and save it's content to `resources/banner.txt`

## Concepts

#### Authentication

Authentication is about knowing a user. It is implemented using JSON Web Tokens (JWT).

For user authentication just call `POST /api/auth/login` and provide username and password in JSON format:

```
    curl -X POST -H "Content-Type: application/json" -H "Cache-Control: no-cache" -d '{  
        "username": "user",
        "password": "user"
    }' "http://localhost:8080/api/auth/login"
```

The JSON response provides a JSON Web Token (JWT) that enlists the username and the authorities allowed:

``` json
    {
      "expiration" : "2017-05-08T15:22:07.025+0000",
      "accessToken" : "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJEMEU5RERBQS01OEI2LTRENjEtOTQ5Qy00NTg0NDNDQTZEMDUiLCJpc3MiOiJzcHJpbmctYm9vdC13ZWItc2tlbGV0b24iLCJpYXQiOjE0OTQ0MjcxNjgsImV4cCI6MTQ5NDQyODA2OH0.95b3gD8n-lEQLlYl3HgYFhQjSIy3wLXgMorUJ-RzV3-NvPg57HtuNQ13h5zsC7pNh7u7UUi_3v4gHTpMb31Q3Q"
    }
```

Every JWT expires after a certain time. Token expiry equivalent to a traditional user session life-time configuration.

In case of *invalid user credentials*, status 401 (Unauthorized) will i.e. look like:
``` json
     {
       "message" : "Authentication failed",
       "errorCode" : 1010001,
       "errorName" : "AUTHENTICATION_GENERAL",
       "httpStatus" : "UNAUTHORIZED",
       "timestamp" : "2017-05-08T13:29:53.674+0000"
     }
```
> ###### How to set the token expiration time?
>
> Just change the value of `token.expiration-time-in-minutes` in `resources/config/application*.yml`.

#### Authorization

Authorization is about knowing what a user is allowed to do.

Since the JWT contains the list of role authorities the user has been assigned to, the API just requires you to set the `Authorization` header including a valid, non-expired `accessToken`.
```
    curl -X GET \
         -H "Content-Type: application/json" \
         -H "Cache-Control: no-cache" \
         -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJEMEU5RERBQS01OEI2LTRENjEtOTQ5Qy00NTg0NDNDQTZEMDUiLCJpc3MiOiJzcHJpbmctYm9vdC13ZWItc2tlbGV0b24iLCJpYXQiOjE0OTQ0MjcxNjgsImV4cCI6MTQ5NDQyODA2OH0.95b3gD8n-lEQLlYl3HgYFhQjSIy3wLXgMorUJ-RzV3-NvPg57HtuNQ13h5zsC7pNh7u7UUi_3v4gHTpMb31Q3Q" \
         "http://localhost:8080/api/heartbeat"
```
> ###### How to control API access?
>
> There are *roles* and *permissions*. *Roles* belong to *Users*. *Permissions* belong to *Roles*. 
> Roles and permissions come with unique names e.g. `ROLE_USER` or `PERM_FOOBAR`.
>
> All `/api/*` API endpoints are protected by default. Fine grained access control is available thru `@PreAuthorize`.

> You can simply check for a role being granted:
>
>     @PreAuthorize("hasAuthority('ROLE_USER')")
>     @GetMapping(value = "/api/foobar", produces = MediaType.APPLICATION_JSON_VALUE)
>     public ResponseEntity<FoobarViewModel> foobar() {
>         ...
>     }
>
> ...or a permission being granted:
>    
>     @PreAuthorize("hasAuthority('PERM_FOOBAR')")
>
> ...or even check for logical combinations:
>
>     @PreAuthorize("hasAuthority('ROLE_FOOBAR') OR hasAuthority('ROLE_BAZBAZ')")
>
> If you are not familiar with SpEL and the power of Spring Security 3 expressions, 
> please [read on...](https://dzone.com/refcardz/expression-based-authorization)
 
## Skeleton API endpoints

#### Login: `POST /api/auth/login`

###### Access level: Public

This API endpoint authenticates a user by username and password. It issues one JWT token at a time.
```
    curl -X POST \
         -H "Content-Type: application/json" \
         -H "Cache-Control: no-cache" \
         -d '{  
            "username": "admin",
            "password": "admin"
         }' \
         "http://localhost:8080/api/auth/login"
 ```
If you provide a *valid username/password combination*, the response should look like:
``` json
    {
      "expiration" : "2017-05-08T15:22:07.025+0000",
      "accessToken" : "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJEMEU5RERBQS01OEI2LTRENjEtOTQ5Qy00NTg0NDNDQTZEMDUiLCJpc3MiOiJzcHJpbmctYm9vdC13ZWItc2tlbGV0b24iLCJpYXQiOjE0OTQ0MjcxNjgsImV4cCI6MTQ5NDQyODA2OH0.95b3gD8n-lEQLlYl3HgYFhQjSIy3wLXgMorUJ-RzV3-NvPg57HtuNQ13h5zsC7pNh7u7UUi_3v4gHTpMb31Q3Q"
    }
```    
In case of *invalid user credentials*, status 401 looks like:
``` json 
     {
       "message" : "Authentication failed",
       "errorCode" : 1010001,
       "errorName" : "AUTHENTICATION_GENERAL",
       "httpStatus" : "UNAUTHORIZED",
       "timestamp" : "2017-05-08T13:29:53.674+0000"
     }
```    
#### Heartbeat: `/api/heartbeat`

######  Access level: Public

An example for how to use the authorization model (the role based permission model) is provided by the `heartbeat` endpoint:
```
    curl -X GET \
         -H "Content-Type: application/json" \
         -H "Cache-Control: no-cache" \
         -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJEMEU5RERBQS01OEI2LTRENjEtOTQ5Qy00NTg0NDNDQTZEMDUiLCJpc3MiOiJzcHJpbmctYm9vdC13ZWItc2tlbGV0b24iLCJpYXQiOjE0OTQ0MjcxNjgsImV4cCI6MTQ5NDQyODA2OH0.95b3gD8n-lEQLlYl3HgYFhQjSIy3wLXgMorUJ-RzV3-NvPg57HtuNQ13h5zsC7pNh7u7UUi_3v4gHTpMb31Q3Q" \
         "http://localhost:8080/api/heartbeat"
```
In case of successful authentication and authorization, the service responds the timestamp of service:

    {
      "timestamp" : "2017-05-10T06:40:08.542+0000"
    }

#### Users: `/api/users`

###### Access level: Protected | Authority required: PERM_USERS_READ_ALL
```
    curl -X GET \
         -H "Content-Type: application/json" \
         -H "Cache-Control: no-cache" \
         -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJEMEU5RERBQS01OEI2LTRENjEtOTQ5Qy00NTg0NDNDQTZEMDUiLCJpc3MiOiJzcHJpbmctYm9vdC13ZWItc2tlbGV0b24iLCJpYXQiOjE0OTQ0MjcxNjgsImV4cCI6MTQ5NDQyODA2OH0.95b3gD8n-lEQLlYl3HgYFhQjSIy3wLXgMorUJ-RzV3-NvPg57HtuNQ13h5zsC7pNh7u7UUi_3v4gHTpMb31Q3Q" \
         "http://localhost:8080/api/users"
```

Returns all users mapped as view models:

```
    [ {
      "id" : "B691BDDF-5EA5-470B-A286-B10DC1FABA60",
      "username" : "system",
      "firstName" : "System",
      "lastName" : "System",
      "email" : "system@localhost",
      "activated" : true,
      "data" : {
        "address" : "Foo square 1, NY"
      },
      "roles" : [ "2F17AF79-50BA-433B-B7B3-3649436BA9E2", "30193EDC-96BF-47F1-BA97-E8C37709E1AC", "E5B83654-635C-4011-AEE1-1DFB8695A5C1", "7D7D15DC-9060-4C1D-903A-BF08F8ECEFF5" ],
      "clients" : [ "9FE7E40D-07E8-4F2E-97AB-617C0C8EBD36" ]
    }, ... ]
```

## Roadmap

The following features are planned for implementation (ordered by dependency):

#### Template service

- General-purpose template service that allows arbitrary text file-types to be abstracted as templates. 

  - Based on the awesome JTwig template engine
  - Unit tested

#### Mail service

- General-purpose mail service that is able to trigger the sending of pretty emails. Will be based on Spring Mail.

  - SMTP and IMAP server support
  - TLS-transport layer encryption (default configuration)
  - E-mail template support based on template service
  - Transparent HTML/plaintext fallback
  - Attachments
  - Inline resources
  - Custom headers
  - All settings configurable using application.yml
  - Unit tested
  
#### Secure token service

- Secure token generator and persistence service. The service generates secure on-time tokens using Spring Security and persists them in the database for later use. 

  - Secure token generation
  - Database persistence
  - Expire time 
  - Token invalidation after on method call
  - Automatic, scheduled invalidation of expired tokens
  - All settings configurable using application.yml
  - Unit tested

### Request logging service 

- Allows the logging the whole request and response flow using Spring Actuator's WebRequestTraceFilter.

  - Configurable to persist all request and response header + body data
  - Thus enables a full request audit-trail out-of-the-box
  - Integration tested
  
#### Workflow service

- General-purpose workflow service and entity to keep track of workflow steps and state.

  - Ability to register a workflow operation 
    - Ability to set an expiry time and expiry state name
    - Support for automatic operation expiry (scheduled)
  - User defined workflow name (e.g. "UserAccountCreation")
  - User defined workflow steps (named e.g. "TriggerAccountCreation", "ConfirmAccountCreation")
  - User-defined workflow state data (POJO, e.g. UserAccountCreateInitialState, holding desired user account data)
  - Ability to fetch latest state of an operation 
  
#### RESTful API based user account management 

- Enterprise-grade, fully configurable user account management implemented as domain services and featuring a decent, secure RESTful API. 
  
  It uses the workflow service to handle the workflow state management.
  
  ##### Available for anonymous users:
  
  - Secure workflow for creating user accounts
    - API method to trigger account creation
      - Generates a secure one-time token
      - Send a confirmation e-mail that contains the secure token and a link to confirm using the token
      - Ability to send a new secure token on confirmation page (per email)
    - API method to confirm account creation using secure token and user-defined password 
    - Google Captcha support
    - Final confirmation mail for account creation
    - Integration tested
    - All mailing based on templates

  - Secure workflow for password reset
    - API method to trigger password reset
      - Generates a secure one-time token
      - Send a confirmation e-mail that contains the secure token and a link to confirm using the token
      - Ability to send a new secure token on confirmation page (per email)
    - API method to confirm password reset using secure token and new, user-defined password 
    - Google Captcha support
    - Final confirmation mail for password reset
    - Integration tested
    - All mailing based on templates
    
  ##### Available for registered users only:
    
  - Secure log-out workflow
    - API endpoint to log-out
    - Erases AuthorizationContext *and* AuthenticationContext
    
  - Secure User Account Data change workflow
    - API endpoint to change user data
    - Sends a confirmation E-Mail that user data has been changed
    
  - Secure User Account close workflow
    - API endpoint to trigger an Account close
      - Generates a secure one-time token
      - Send a confirmation e-mail that contains the secure token and a link to confirm using the token
      - Ability to send a new secure token on confirmation page (per email)
    - API endpoint to confirm account close
    - Google Captcha support
    - Final confirmation mail for account close
    - Integration tested
    - All mailing based on templates
    
  ##### Also: Administrative APIs to manage Users, Client, Roles, Permissions and their relations
  
  TODO: Description  
    
#### Angular 2 + Bootstrap 3 responsive UI
    
- UI code and development infrastructure with a focus on code quality, ease of developing and testability.

  - Package management using yarn, integrated with maven build chain
  - Based on Angular 2 + TypeScript
    - Clean code, following best practices
    - Modular MVP design
    - URL-safe routing with support for browser history (# hash based dynamic routing)
  - Webpack based hot-deployment in "dev" profile
  - SASS support, live-reload in "dev" profile
  - Clean material design (materialize.css)
  - Responsive 
  - ACL permission control based on User roles and permissions
  - i18n support
  - Focus on security, testability, performance
  - Integration tests
  - Unit tests
    
#### Also: Management UI for Users, Client, Roles, Permissions and their relations

TODO: Description

#### Country service

TODO: Description

#### Currency service

TODO: Description

#### DoS protection

- Injected in the filter chain, ordered to sit in the front-row and activated by default, the DoS protection filter identifies the remote client by their IP:port tuple's. 
  The filter tracks the amount of requests in memory and is able to reject requests from further processing for certain time.

  - 3 configurable time-windows
  - Configuration of "amount of requests" per time-window until limit is reached
  - Configuration of request rejection time configurable per time-window
  - Integration tested
