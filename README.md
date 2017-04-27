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
