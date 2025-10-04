## High-Level Architecture Overview

The application is structured as a full-stack solution with a clear separation between the frontend and backend:

- **Frontend**:
    - Built with Angular 20, utilizing TypeScript for type safety and maintainability.
    - Uses PrimeNG as the UI component library and TailwindCSS for utility-first styling.
    - OpenAPI is used to generate client code for API communication, ensuring strong typing and consistency.
    - Spectator is leveraged for simplified and robust component testing.
    - Error handling is centralized for consistency across the UI.

- **Backend**:
    - Developed with Spring Boot, using Java.
    - H2 in-memory database is used for development and testing.
    - RESTful APIs are documented with OpenAPI/Swagger.
    - Testing is performed using Mockito for unit tests and integration tests for end-to-end validation.
    - Maven is used for dependency management and build automation.

- **Communication**:
    - The frontend communicates with the backend via RESTful APIs.
    - API contracts are shared and validated using OpenAPI specifications.

- **Development Workflow**:
    - The project uses Git for version control.
    - Both frontend and backend have their own setup and build processes, but are designed to work seamlessly together.

This architecture ensures modularity, scalability, and maintainability, supporting rapid development and easy future enhancements.

## Project setup
Both the frontend and the backend have only been developed as of yet for local development, that means, no prod backend configuration, no 
production environment file and no build pipelines. To run the apps locally:
- **Backend**:
    - Run the command: `mvn spring-boot:run -Dspring-boot.run.profiles=dev`

- **Frontend**:
    - Run the command: `ng serve` or `npm run start`

## Challenges faced
I always wanted to use PrimeNG as a UI Library in an Angular project. However, I struggled with the 'complex' code that they provide for components and therefore also struggled with the component testing.

## Potential future improvements
    - the FE edit details page now gets the data from the table, however, the getClientById should be used.
    - The Frontend client table and client details is too complex and now has multiple responsibilities, that should be improved by using more smaller components.
    - the export of the clients is now always csv, but that can be a request param with a certain format, then a factory can be used to create the correct format
        - Also add the tests for this in both the FE and the BE.

## Example API Usage
- Run the command: `mvn spring-boot:run -Dspring-boot.run.profiles=dev`
- Go to the URL: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Additional features (How I would implement those)
    - Data export: Started with that, have to finish the unit testing on the backend and implement a proper front end solution, see the potential future improvements for the xlsx.
	- For the notifications I would use websockets, but also curious how HTTP2 works. That should work too.
    - Data import: Spring batch processing
    - Authentication & authorization: add keycloak and use springboot compose with a docker file to boot the keycloak during local development
    - E2E testing: implement playwright in the FE. Which has nice features to click through your application and it makes test automatically.
    - Save as draft: I would use the session storage for that.
