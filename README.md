# Universe Application

## Prerequisites

### Locally:

When running Universe, you need to have Docker installed on your machine.
See installation guides here for Windows: [Docker for Windows](https://docs.docker.com/desktop/install/windows-install/) and here for Ubuntu: [Docker for Ubuntu](https://docs.docker.com/engine/install/ubuntu/).

Have `kubectl` in your machine. See the guide here: [kubectl Installation](https://kubernetes.io/docs/tasks/tools/).

Also, run a working Kubernetes cluster. You can use `kind`. See the guide here: [Kind Quick Start](https://kind.sigs.k8s.io/docs/user/quick-start/).

Furthermore, you need to have a MySQL instance running locally. See the guide for Ubuntu here: [MySQL Installation on Ubuntu](https://dev.mysql.com/doc/refman/8.4/en/linux-installation.html) and the guide for Windows here: [MySQL Installation on Windows](https://dev.mysql.com/doc/refman/8.4/en/windows-installation.html).

### Setting Up the Database:

1. Login to your MySQL server via a terminal:

    ```sh
    mysql -u root -p
    ```

2. Create a user with credentials `username: universe` and `password: universe`:

    ```sql
    CREATE USER 'universe'@'%' IDENTIFIED BY 'universe';
    GRANT ALL PRIVILEGES ON universe.* TO 'universe'@'%';
    FLUSH PRIVILEGES;
    ```

3. Create the necessary tables:

    ```sql
    CREATE TABLE users (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        email VARCHAR(255) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL
    );

    CREATE TABLE roles (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255) NOT NULL
    );

    CREATE TABLE users_roles (
        user_id BIGINT NOT NULL,
        role_id BIGINT NOT NULL,
        PRIMARY KEY (user_id, role_id),
        FOREIGN KEY (user_id) REFERENCES users(id),
        FOREIGN KEY (role_id) REFERENCES roles(id)
    );
    ```

This is required as in the next major release, OIDC will be implemented to be able to connect to a remote cluster and when choosing to run locally you won't be required to login.

## Running It:

1. Clone the repository and ensure you have Maven installed on your machine.

2. Build the project:

    ```sh
    mvn clean package
    ```

3. Run the JAR file:

    ```sh
    java -jar target/Universe-1.0-SNAPSHOT.jar
    ```

4. Open your browser to [localhost:8080](http://localhost:8080)
