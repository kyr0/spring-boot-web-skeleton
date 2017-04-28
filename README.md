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
 
## How to generate a nice title for banner.txt?

[Use patorjk.com ASCII art generator](http://patorjk.com/software/taag/#p=display&h=0&v=0&w=%20&f=ANSI%20Shadow&t=%20s%20k%20e%20l%20e%20t%20o%20n)

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
