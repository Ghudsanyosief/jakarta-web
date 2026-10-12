# Jakarta EE Pet Management Web Application

This repository contains a production-ready RESTful Web Service constructed via Jakarta EE 11, JAX-RS, CDI, and Bean Validation running on a WildFly application server. The architecture handles in-memory persistence inside a thread-safe environment governed by a `ReentrantLock` for atomic mutator operations. It features localized JSON structure enforcement and advanced server-side dataset operations.

## Prerequisites
- Java 21 or higher
- IntelliJ IDEA (Ultimate Edition)
- WildFly Application Server (configured locally)

## Compilation & Manual Deployment Workflow

To activate changes, clean the repository compile trees and assemble a fresh application payload using the integrated Maven wrapper:

```bash
.\mvnw clean package
```

Deploy the newly generated artifact by overwriting the target runtime server deployment pipeline space (replace `C:\path\to\wildfly\` with the actual path to WildFly on your computer):

```cmd
copy .\target\api.war "C:\path\to\wildfly\standalone\deployments\api.war"
```
*(Respond with `yes` if prompted to overwrite the existing package structure).*

Once the application server deployment lifecycle completes under the Debug or Run profile inside IntelliJ, the master API target root will be accessible at: `http://localhost:8080/api/pets`

---

## Part 1 & 2: Core REST API & Thread-Safe Operations

Execute the following network loop routines within your terminal environment to validate basic persistence storage states.

### 1. Adopt a New Pet (POST)
Submits a fresh payload to the persistence layer. Internal logic maps attributes while stripping raw entity tracking keys to enforce specific visual mapping sequences.
```bash
curl -X POST http://localhost:8080/api/pets -H "Content-Type: application/json" -d "{\"name\": \"Charlie\", \"species\": \"dog\", \"hungerLevel\": 50, \"happiness\": 50}"
```

### 2. List All Registered Pets (GET)
Returns the comprehensive set of data nodes stored inside the thread-safe collection array mappings.
```bash
curl -X GET http://localhost:8080/api/pets
```
You can also open this link directly inside your Web Browser: `http://localhost:8080/api/pets`

### 3. Fetch Individual Pet Status (GET with Path Variable)
Queries a specific object signature configuration map directly by its key identifier.
```bash
curl -X GET http://localhost:8080/api/pets/1
```

### 4. Feed the Pet (PUT)
Triggers locked state modification routines reducing the hunger tracker properties by 15 units.
```bash
curl -X PUT http://localhost:8080/api/pets/1/feed
```

### 5. Play with the Pet (PUT)
Triggers locked state modification routines increasing the happiness tracker properties by 15 units.
```bash
curl -X PUT http://localhost:8080/api/pets/1/play
```

### 6. Release Pet (DELETE)
Evicts the target object map properties permanently from runtime memory storage arrays.
```bash
curl -X DELETE http://localhost:8080/api/pets/1
```

---

## Part 3: Advanced Bean Validation & Custom Exception Mappers

The application employs `@Provider` architecture to intercept error exceptions globally, completely overriding default server HTML rendering.

You can validate these error structures using both the Web Browser and your terminal:

### Test 1: Structural Request Payload Failures (HTTP 400 Bad Request)
Send an intentionally invalid payload containing values breaking constraints (e.g., an short name and an overflowing hunger metric configuration parameter):
```bash
curl -i -X POST http://localhost:8080/api/pets -H "Content-Type: application/json" -d "{\"name\": \"A\", \"species\": \"Cat\", \"hungerLevel\": 200, \"happiness\": 50}"
```
The server will reject the request lifecycle and instantly pass down an explicit structured JSON object:
```json
{
  "errors": [
    "Hunger level cannot be higher than 100",
    "Pet name must be between 2 and 50 characters"
  ],
  "message": "Validation failed for the incoming request payload."
}
```

### Test 2: Target Entity Missing Lookups (HTTP 404 Not Found)
Attempt to call an unallocated pointer address sequence index directly from your web browser:
👉 `http://localhost:8080/api/pets/999`

The mapped provider instantly generates an optimized single error schema array block:
```json
{
  "message": "Could not find any pet with ID: 999",
  "status": 404
}
```

---

## Higher Grades Bonus Features: Filtering, Sorting, and Pagination

The list endpoint features robust, high-performance query string analysis handled by `UriInfo` layers. This completely isolates datasets according to parameters specified inside your browser's address field.

Populate the database layer with multiple distinct records (e.g., "cat", "dog", "horse") and perform the following test configurations:

### 1. Server-Side Dataset Filtering (?species={value})
Isolates and matches entries corresponding directly to the target request query while omitting foreign nodes.
- View dogs only: `http://localhost:8080/api/pets?species=dog`
- View cats only: `http://localhost:8080/api/pets?species=cat`

### 2. Dynamic Attribute Sorting (?sortBy={field}&order={asc|desc})
Rearranges array positions using variable conditions. Supports sorting fields like `happiness`, `hungerLevel`, or `name`.
- Sort by happiness descending: `http://localhost:8080/api/pets?sortBy=happiness&order=desc`
- Sort by hunger level ascending: `http://localhost:8080/api/pets?sortBy=hungerLevel&order=asc`

### 3. Query Bound Pagination (?offset={index}&limit={size})
Splits raw records into explicit data windows to manage rendering loads by controlling how many pets are visible per page view bounds.
- Show first two items only (limit dataset page visibility size): `http://localhost:8080/api/pets?offset=0&limit=2`
- Advance page view context bounds (skip elements and fetch remainder): `http://localhost:8080/api/pets?offset=2&limit=2`
