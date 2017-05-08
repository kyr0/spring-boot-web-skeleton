# spring-boot-web-skeleton

Skeleton implementation for enterprise web applications following best practices.

## How to run the PostgreSQL database server?

Just install [docker](https://docs.docker.com/engine/installation/) and [docker-compose](https://docs.docker.com/compose/install/) then run:

Attached to current shell (you'll see the logs):

> `docker-compose -f docker/postgresql.yml up`

In daemon-mode:

> `docker-compose -f docker/postgresql.yml up -d`

PostgreSQL will start in an isolated linux container on port 5432. 
The isolated container's internal port will be forwarded to the local host machine port. Thus, you can connect to the PostgreSQL database server using `tcp://localhost:5432`
 
## How to create the default schema "skeleton"?

Download and install pgAdmin 4 (platform independent).
Connect to your local PostgreSQL database (see above).

Navigate to: Servers > postgres-local > Databases 
Right-click and click: Create > Database...
Enter Database name: skeleton
Click "Save".

## How to build & run?

Either:
 
> `./mvnw spring-boot:run`

Or using the IDE.
 
## Integration

### How to generate a nice title for banner.txt?

[Use patorjk.com ASCII art generator](http://patorjk.com/software/taag/#p=display&h=0&v=0&w=%20&f=ANSI%20Shadow&t=%20s%20k%20e%20l%20e%20t%20o%20n)

## API Usage

To give you a short introduction, here are some examples of how to use the User and Roles API.

### Login: `POST /api/auth/login

This API resource is publicly available and can be called by anonymous users.

Please take a look at `users.csv` for the users available. Username == Password.

    curl -X POST -H "Content-Type: application/json" -H "Cache-Control: no-cache" -d '{  
        "username": "user",
        "password": "user"
    }' "http://localhost:8080/api/auth/login"

If you provide a *valid username/password combination*, the response should look like:

    {
      "token" : "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwic2NvcGVzIjpbIlJPTEVfVVNFUiJdLCJpc3MiOiJzcHJpbmctYm9vdC13ZWItc2tlbGV0b24iLCJpYXQiOjE0OTM3NTkxNjQsImV4cCI6MTQ5Mzc2MDA2NH0.YhWhdQsYcrnVRUN96BNXQXr0OxyWGLlegT3aeN-bZivMWyE8XLXJ2pFqGIgmQtCwtE-cZDmfjuheO5gNFYhv7Q",
      "refreshToken" : "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwic2NvcGVzIjpbIlJPTEVfUkVGUkVTSF9UT0tFTiJdLCJpc3MiOiJzcHJpbmctYm9vdC13ZWItc2tlbGV0b24iLCJqdGkiOiJlZjY3YWU0OS01NzY2LTRmNWQtYjVkNS1iMzNhYjgxZjI3NWUiLCJpYXQiOjE0OTM3NTkxNjUsImV4cCI6MTQ5Mzc2Mjc2NX0.lGOEzhU8HSHR91pPgvNtT-eqiF4acdmANL0Br5gnlWVC4VhTqK0YoGLZz_64iRg0Sf-1SXd-t_LUcTopMYJirA"
    }
    
In case of *invalid user credentials*, status 401 looks like:
 
     {
       "status" : 401,
       "message" : "Invalid username or password",
       "errorCode" : 10,
       "timestamp" : "2017-05-02T21:07:42.453+0000"
     }
    
> ##### Authorization header
> Please note that for any further request that points to an API method of access scope other than [anonymous], you should provide the HTTP header Authorization: Bearer ${token}
    
### Logout: `POST /api/auth/logout`

In order to actively log-out:


## Concepts and Design Decisions

Before you start using Spring Boot web skeleto, please take the following design decisions into consideration:

### Security

#### Authentication process

We were looking for a lightweight, easy to understand, yet secure way for Authentication and Authorization.

Therefore we decided to go for JSON Web Tokens (JWT) in order to provide a token based authentication.

Wen an initial login request to the public resource `POST /api/auth/login` provides valid username and password credentials,

    {
        "username": "foo",
        "password": "bar",
        "rememberMe": true
    }

a JWT token is returned using the `Authorization` HTTP response header. 

> ##### DO use HTTPS!
> Since user login credentials are transmitted between client and server in plain-text JSON, you MUST deploy HTTPS in order to protect against Man-in-the-middle (MITM) attacks. It's easy to set up and free of charge when using [LetsEncrypt](https://letsencrypt.org).

Next to the JWT standard claims, the token payload contains only the username as a custom claim.

All tokens are signed and encrypted with a secret key that is defined for the scope of the whole application (`application.security.authorization.jwt.secret-key`).

> ##### Please note
> This secret key MUST be changed prior to a production deployment and MUST remain a secret. The secret key MUST NEVER be exposed.

#### Stateless tokens

Since the whole architecture is service-oriented, there is no such thing like a stateful user session. The Authorization process happens on every API request and the JWT token itself contains the username safely encrypted. 

Such tokens can be issued and verified by multiple instances of the application sharing the same secret key. However, access to the same database is requested by any application instance in order to look up the same User Repository.

#### Sliding-sessions (or: token expiration)

Tokens expire after a configurable validity time (`application.security.authorization.jwt.token-validity-in-seconds`). However, the token is refreshed automatically on every authorized API method call. 

In consequence, API users stay logged-in until they don't call any API method for as long as the validity time is set or the method `POST /api/auth/logout` is called.

### Authorization process

The Authorization process extends the Authentication process by a fine-grained control over which User should be allowed to execute what functionality.

In this implementation, this is covered by an implementation of a User to Role relation. You can grant many Roles to many Users (n:m relation).

Users and Roles data can be seeded using `users.csv`, `roles.csv` and `users_roles.csv` for defining the relations by id.

To protect API methods from being available without prior login or to check that certain Roles are granted to the User, just annotate methods using `@PreAuthorize` and provide an Expression-Based Access Control rule:

    @PreAuthorize("hasRole('USER') and hasRole('MANAGER')")
 
> ##### Expression based authorization
> If you are not familiar with SpEL and the power of Spring Security 3 expressions, please [read on...](https://dzone.com/refcardz/expression-based-authorization)
 
### 

## Roadmap / TODO's

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

#### Authentication and Authorization
  
- General purpose REST API-level authentication and authorization layer implementation based on Spring Web Security and annotations.
    
    - Separation of relevant entities:
       
       - User Account
         - Id
         - Identifier (required, unique, can represent any data)
         - Type (required, anonymous [default], user, system)
         - E-Mail (required, not unique)
         - Display name (required, used to address the user)
         - Activated (required, boolean, true [default])
         
       - User Account Data
         - Id
         - 1:1 relation to User Account
         - Meant to be extended in target application
         
       - Client
         - Id
         - Display name (required, addresses the client)
       
       - Client Data 
         - Id
         - 1:1 relation to Client
         - Meant to be extended in target application
         
       - User Account to Clients
         - Id
         - User_Account_Id
         - Client_Id
         
       - Roles
         - Id
         - Name
         - Description
         - Initial roles (extensible):
           - ANONYMOUS
           - USER
           - ADMINISTRATOR
           - SYSTEM
       
       - Permissions
         - Id
         - Name
         - Role_Id
         - Description
       
       - User Account to Roles
         - Id
         - Role_Id
         - User_Id
         - Initial users (extensible)
           - "system" (Roles: SYSTEM, ADMINISTRATOR, USER, ANONYMOUS)
           - "administrator" (Roles: ADMINISTRATOR, USER, ANONYMOUS)
         
       - AuthorizationContext
         - Contains a Set<Permission> of all permissions granted (cached)
         - Cache is loaded on first call of the getter
         - Meant to be extended in target application 
         - Needs to be updated when roles/permissions of a user are changed (cache invalidation!)
         - Method to check if user has a specific permission (ACL check)
  
       - AuthenticationContext  
         - Holds the User Account object
           - If not authenticated, a default Anonymous User Account
           - If authenticated, the User Account applicable
         - Holds the AuthorizationContext object
         
       - Transparent access to AuthenticationContext 
         - It's just a tiny POJO hold in the HTTP session
         - Thus can be accessed where applicable
         - Used as the only data source for ACL checks
  
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
