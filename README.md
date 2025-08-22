# lightblue-be

## Docker Setup

To run the application using Docker Compose, follow these steps:

1.  **Build and Run with Docker Compose:**
    ```bash
    docker-compose up --build
    ```
    This command will build the `lightblue-be` image using the `Dockerfile` and start both the `db` and `be` services.

2.  **Run in Background (Optional):**
    ```bash
    docker-compose up -d --build
    ```
    Use the `-d` flag to run the containers in the background.

## Naver Social Login
- authorization: /oauth2/authorization/naver
- redirect-uri: /login/oauth2/code/naver