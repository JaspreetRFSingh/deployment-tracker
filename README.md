# Deployment Tracker

A backend service for ingesting and serving deployment event data.

## Run

```bash
mvn spring-boot:run
```

The service starts on **http://localhost:8080** and seeds 34 mock deployment
events automatically on startup.

## API

### List deployments

```
GET /deployments
```

**Query parameters** (all optional):

| Parameter | Description                          | Example        |
|-----------|--------------------------------------|----------------|
| `service` | Filter by service name               | `billing-api`  |
| `status`  | Filter by status                     | `failed`       |
| `page`    | Zero-based page index (default `0`)  | `1`            |
| `size`    | Items per page, max 100 (default 20) | `10`           |

**Example requests:**

```bash
# All deployments
curl http://localhost:8080/deployments

# Filter by service
curl "http://localhost:8080/deployments?service=billing-api"

# Filter by status
curl "http://localhost:8080/deployments?status=failed"

# Combine filters with pagination
curl "http://localhost:8080/deployments?service=billing-api&status=failed&page=0&size=5"
```

**Response shape:**

```json
{
  "data": {
    "data": [
      {
        "id": "deploy_003",
        "service": "billing-api",
        "status": "failed",
        "duration": 320,
        "timestamp": "2025-02-25T10:00:00Z",
        "commit_sha": "c3d4e5f"
      }
    ],
    "meta": {
      "total": 6,
      "page": 0,
      "size": 20,
      "totalPages": 1
    }
  },
  "status": 200
}
```

### Get a single deployment

```
GET /deployments/:id
```

```bash
curl http://localhost:8080/deployments/deploy_001
```

**Response shape:**

```json
{
  "data": {
    "id": "deploy_001",
    "service": "billing-api",
    "status": "success",
    "duration": 185,
    "timestamp": "2025-02-01T10:00:00Z",
    "commit_sha": "a1b2c3d"
  },
  "status": 200
}
```

**Error (not found):**

```json
{
  "error": "Deployment not found: deploy_xyz",
  "status": 404
}
```

### Valid statuses

| Status        | Meaning                    |
|---------------|----------------------------|
| `success`     | Deployment completed       |
| `failed`      | Deployment failed          |
| `rolled_back` | Deployment was rolled back |
| `in_progress` | Currently deploying        |

## Run tests

```bash
mvn test
```

## H2 console (temporary data store)

Browse the raw data at http://localhost:8080/h2-console  
JDBC URL: `jdbc:h2:mem:deploytracker`  Username: `sa`  Password: *(empty)*
