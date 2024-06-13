# Partner Universities API

This README provides an overview of the API endpoints, example JSON structures for creating and updating resources, and instructions for running the application and testing it with Postman.

This application provides a RESTful API for managing partner universities and their modules. It uses HATEOAS for creating links between resources. 
Made by Serhii Radkovskyi (Mtr.5121005) as the portfolio assingment for Distributed systems lecture 

---

## Running the Application

### Start the Application

After the build is complete, you need to start the application. Follow these steps:

1. Open your IDE and wait for all Maven dependencies to be resolved and downloaded.
2. Locate the `PartnerUniversitiesApplication` class in the `com.example.partneruniversities` package.
3. Right-click on the `PartnerUniversitiesApplication` class and select `Run` to start the application. Alternatively, you can run the application from the command line using:

    ```sh
    mvn spring-boot:run
    ```

This will start an embedded Tomcat server on `localhost` with port `8080` and set up an in-memory database.

### Run the Tests

To run the integration tests, follow these steps:

1. Ensure the application server is running by following the steps in the "Start the Application" section above.
2. Navigate to the test folder in your IDE: `src/test/java/com/example/partneruniversities/`.
3. Find the `PartnerUniversitiesClientIntegrationTest` class.
4. Right-click on the `PartnerUniversitiesClientIntegrationTest` class and select `Run` to execute the tests.

The tests will interact with the running server, sending requests to the application and verifying the responses.

For a more detailed explanation and visual guide, refer to the instructional video provided.

## Testing with Postman

Use the provided JSON structures to test the API endpoints via Postman. You can create, retrieve, update, and delete resources by sending requests to the appropriate endpoints.

--- 


## Base URL

`http://localhost:8080`


## Endpoints

### Universities

#### Get All Universities
- **URL**: `/universities`
- **Method**: `GET`
- **Description**: Retrieves a list of all universities.
- **Response Example**:
    ```json
    {
        "_embedded": {
            "universities": [
                {
                    "id": 1,
                    "name": "Test University",
                    "country": "Test Country",
                    "departmentName": "Test Department",
                    "departmentUrl": "http://test.example.com",
                    "contactPerson": "Test Contact Person",
                    "maxOutgoingStudents": 10,
                    "maxIncomingStudents": 10,
                    "nextSpringSemesterStart": "2023-01-15",
                    "nextAutumnSemesterStart": "2023-09-15",
                    "_links": {
                        "self": {
                            "href": "http://localhost:8080/universities/1"
                        },
                        "modules": {
                            "href": "http://localhost:8080/universities/1/modules"
                        },
                        "departmentUrl": {
                            "href": "http://test.example.com"
                        }
                    }
                }
            ]
        },
        "_links": {
            "self": {
                "href": "http://localhost:8080/universities"
            },
            "search": {
                "href": "http://localhost:8080/universities/search"
            }
        }
    }
    ```

#### Get University by ID
- **URL**: `/universities/{id}`
- **Method**: `GET`
- **Description**: Retrieves a university by its ID.
- **Response Example**:
    ```json
    {
        "id": 1,
        "name": "Test University",
        "country": "Test Country",
        "departmentName": "Test Department",
        "departmentUrl": "http://test.example.com",
        "contactPerson": "Test Contact Person",
        "maxOutgoingStudents": 10,
        "maxIncomingStudents": 10,
        "nextSpringSemesterStart": "2023-01-15",
        "nextAutumnSemesterStart": "2023-09-15",
        "_links": {
            "self": {
                "href": "http://localhost:8080/universities/1"
            },
            "modules": {
                "href": "http://localhost:8080/universities/1/modules"
            },
            "departmentUrl": {
                "href": "http://test.example.com"
            }
        }
    }
    ```

#### Create University
- **URL**: `/universities`
- **Method**: `POST`
- **Description**: Creates a new university.
- **Request Body Example**:
    ```json
    {
        "name": "New University",
        "country": "Country",
        "departmentName": "Department",
        "departmentUrl": "http://example.com",
        "contactPerson": "Contact Person",
        "maxOutgoingStudents": 10,
        "maxIncomingStudents": 10,
        "nextSpringSemesterStart": "2023-01-15",
        "nextAutumnSemesterStart": "2023-09-15"
    }
    ```

#### Update University
- **URL**: `/universities/{id}`
- **Method**: `PUT`
- **Description**: Updates an existing university.
- **Request Body Example**:
    ```json
    {
        "name": "Updated University",
        "country": "Updated Country",
        "departmentName": "Updated Department",
        "departmentUrl": "http://updated.example.com",
        "contactPerson": "Updated Contact Person",
        "maxOutgoingStudents": 15,
        "maxIncomingStudents": 15,
        "nextSpringSemesterStart": "2023-02-01",
        "nextAutumnSemesterStart": "2023-10-01"
    }
    ```

#### Delete University
- **URL**: `/universities/{id}`
- **Method**: `DELETE`
- **Description**: Deletes a university by its ID.

### Modules

#### Get All Modules
- **URL**: `/modules`
- **Method**: `GET`
- **Description**: Retrieves a list of all modules.
- **Response Example**:
    ```json
    {
        "_embedded": {
            "modules": [
                {
                    "id": 1,
                    "name": "Test Module",
                    "semester": 1,
                    "creditPoints": 5,
                    "university": {
                        "id": 1,
                        "name": "Test University"
                    },
                    "_links": {
                        "self": {
                            "href": "http://localhost:8080/modules/1"
                        },
                        "modules": {
                            "href": "http://localhost:8080/modules"
                        }
                    }
                }
            ]
        },
        "_links": {
            "self": {
                "href": "http://localhost:8080/modules"
            }
        }
    }
    ```

#### Get Module by ID
- **URL**: `/modules/{id}`
- **Method**: `GET`
- **Description**: Retrieves a module by its ID.
- **Response Example**:
    ```json
    {
        "id": 1,
        "name": "Test Module",
        "semester": 1,
        "creditPoints": 5,
        "university": {
            "id": 1,
            "name": "Test University"
        },
        "_links": {
            "self": {
                "href": "http://localhost:8080/modules/1"
            },
            "modules": {
                "href": "http://localhost:8080/modules"
            }
        }
    }
    ```

#### Create Module
- **URL**: `/modules`
- **Method**: `POST`
- **Description**: Creates a new module.
- **Request Body Example**:
    ```json
    {
        "name": "New Module",
        "semester": 4,
        "creditPoints": 6,
        "university": {
            "id": 1
        }
    }
    ```

#### Update Module
- **URL**: `/modules/{id}`
- **Method**: `PUT`
- **Description**: Updates an existing module.
- **Request Body Example**:
    ```json
    {
        "name": "Updated Module",
        "semester": 2,
        "creditPoints": 6,
        "university": {
            "id": 1
        }
    }
    ```

#### Delete Module
- **URL**: `/modules/{id}`
- **Method**: `DELETE`
- **Description**: Deletes a module by its ID.

### Search Universities
- **URL**: `/universities/search`
- **Method**: `GET`
- **Description**: Searches for universities based on query parameters.
- **Query Parameters**:
    - `name`: Name of the university.
    - `country`: Country of the university.
    - `departmentName`: Department name of the university.
    - `page`: Page number for pagination.
    - `size`: Page size for pagination.
    - `sortBy`: Field to sort by.
    - `direction`: Sort direction (`asc` or `desc`).
- **Example**:
    ```
    http://localhost:8080/universities/search?name=Test%20University&country=&departmentName=&page=0&size=10&sortBy=name&direction=asc
    ```

---
