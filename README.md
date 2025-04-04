# OpenFGA + Spring Boot Demo

A proof of concept application demonstrating fine-grained authorization with [OpenFGA](https://openfga.dev/) and Spring Boot.

## Overview

This application showcases how to implement robust authorization patterns using OpenFGA with a Spring Boot backend. It provides a document management system where users can create, share, and manage documents with different permission levels.

## Features

- **Fine-grained Authorization**: Uses OpenFGA to implement complex authorization rules
- **Document Management**: Create, read, update, delete operations with proper access control
- **User Management**: Authentication and role-based access control
- **Group-based Permissions**: Admin, Editor, and Viewer group capabilities
- **API Documentation**: OpenAPI documentation with Scalar UI

## Authorization Model

The application uses OpenFGA's relationship-based authorization model:

```
type user

type group
  relations
    define member: [user, group#member]

type document
  relations
    # roles
    define owner: [user, group#member]
    define editor: [user, group#member]
    define viewer: [user, group#member]

    # permissions
    define can_change_owner: owner
    define can_delete: owner
    define can_share: owner or editor
    define can_write: owner or editor
    define can_read: owner or editor or viewer
```

This model allows for intuitive permission management:
- Document owners can do anything with their documents
- Editors can modify and share documents
- Viewers can only read documents
- Group members inherit permissions from their group

## Technology Stack

- **Backend**: Spring Boot 3.3.x
- **Security**: Spring Security + JWT
- **Database**: PostgreSQL
- **Authorization**: OpenFGA
- **Documentation**: SpringDoc OpenAPI + Scalar UI
- **Database Migration**: Liquibase
- **Build Tool**: Gradle (Kotlin DSL)

## Getting Started

### Prerequisites

- JDK 21+
- Docker and Docker Compose
- PostgreSQL
- OpenFGA server running

### Configuration

The application uses the following configuration (in `application.yaml`):

```yaml
server:
  port: 8090

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/fga-demo-app
    username: postgres
    password: r7QPQ9gugRnjXntO
  
openfga:
  api-url: http://localhost:8080
  store-id: 01JQA1QA887XK0S1PVNHAGJKQG
```

### Running the Application

1. **Start OpenFGA**:
   ```bash
   docker run -p 8080:8080 openfga/openfga run
   ```

2. **Create an OpenFGA Store**:
   ```bash
   curl -X POST http://localhost:8080/stores \
     -H 'Content-Type: application/json' \
     -d '{"name":"fga-demo-app"}'
   ```

3. **Start the Application**:
   ```bash
   ./gradlew bootRun
   ```

## API Endpoints

### Authentication
- `POST /api/auth/v1/signup` - Register a new user
- `POST /api/auth/v1/signin` - Authenticate and get JWT token
- `GET /api/auth/v1/roles` - Get current user roles
- `GET /api/auth/v1/roles/{userId}` - Get roles for a specific user

### Documents
- `GET /api/documents/v1/{id}` - Get document by ID
- `GET /api/documents/v1/search` - Search documents
- `GET /api/documents/v1/by-owner/{ownerId}` - Get documents by owner
- `POST /api/documents/v1` - Create a new document
- `PUT /api/documents/v1/{id}` - Update an existing document
- `DELETE /api/documents/v1/{id}` - Delete a document

### Documentation
- `GET /docs/scalar` - Scalar API documentation
- `GET /docs/api` - OpenAPI specification
- `GET /docs/ui` - Swagger UI

## Authorization Flow

1. User authenticates with credentials and receives a JWT token
2. Protected endpoints use `@PreAuthorize` annotations with OpenFGA checks:
   ```java
   @PreAuthorize("@fga.check('document', #id, 'can_write', 'user', @authUtils.getCurrentUserId())")
   ```
3. When documents are created, the OpenFGA service creates appropriate relationships:
   ```java
   openFgaService.createRelationship(FgaTuple.of(
           FgaObject.of(FgaObjectType.USER, owner.getId().toString()),
           FgaRelation.OWNER,
           FgaObject.of(FgaObjectType.DOCUMENT, savedDocument.getId().toString())));
   ```

## Project Structure

- `models/openfga` - OpenFGA domain models
- `services` - Business logic and OpenFGA integration
- `net/controller` - REST endpoints
- `data` - Database entities and repositories
- `resources/fga` - OpenFGA authorization model

## License

[MIT License](LICENSE)

## Acknowledgements

- [OpenFGA](https://openfga.dev/) for the authorization framework
- [Spring Boot](https://spring.io/projects/spring-boot) for the application framework
- [Scalar](https://scalar.com/) for the API documentation UI 