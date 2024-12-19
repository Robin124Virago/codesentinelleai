# CodeSentinelleAi

CodeSentinelleAi is a system designed to monitor, analyze, and validate code changes in real-time. It integrates with GitHub webhooks to detect code modifications, performs static and dynamic code analysis, and provides logging and validation to enhance security and code quality.

---

## **Features**

1. **Code Monitoring**
    - Listens to GitHub webhooks for `push` events.
    - Extracts and processes commit details (added, modified, and removed files).

2. **Static Code Analysis**
    - Integrates Checkstyle with custom rules to validate coding standards.
    - Automatically runs analysis on modified files.

3. **Dynamic Code Analysis**
    - Validates API inputs and detects unauthorized behavior.
    - Logs all critical operations for auditing.

4. **Critical Change Detection**
    - Scans for sensitive keywords (e.g., `password`, `API_KEY`) in modified files.

5. **Global Exception Handling**
    - Captures and logs unexpected exceptions, returning clear error messages.

6. **Database Integration**
    - Uses an in-memory H2 database to store commit details and user data.

---

## **Requirements**

- **Java**: Version 21
- **Gradle**: Installed via the provided Gradle Wrapper
- **ngrok**: For exposing the local application to GitHub webhooks
- **Git**: Repository access for integration

---

## **Installation and Setup**

### **1. Clone the Repository**
```bash
git clone <your-repository-url>
cd codesentinelleai
```

### **2. Start ngrok**
Run the following command to expose your local application to the internet:
```bash
ngrok http http://localhost:8080
```
Copy the `ngrok` URL and update the GitHub webhook settings accordingly.

### **3. Build and Run the Application**
```bash
./gradlew build
./gradlew bootRun
```
The application will start on `http://localhost:8080`.

---

## **Endpoints**

### **1. Code Monitor**
- **POST** `/code-monitor/webhook`
    - Processes `push` payloads from GitHub.
    - Analyzes commit details and runs static analysis.
    - **Example Payload**:
      ```json
      {
        "ref": "refs/heads/main",
        "repository": {
          "name": "codesentinelleai"
        },
        "commits": [
          {
            "id": "123abc",
            "message": "Initial commit",
            "added": ["src/Main.java"],
            "modified": ["src/Utils.java"],
            "removed": []
          }
        ]
      }
      ```

- **GET** `/code-monitor/commits`
    - Retrieves all stored commit details.

### **2. User Management**
- **POST** `/users`
    - Creates a new user with validated input.
    - Example Request:
      ```json
      {
        "username": "validuser",
        "email": "validuser@example.com"
      }
      ```

- **GET** `/users`
    - Fetches all registered users.

---

## **Static Code Analysis**

The project uses Checkstyle for static code analysis, configured with custom rules:
- Whitespace around operators and keywords.
- Enforcement of `final` for local variables.
- Detection of unused imports and empty blocks.
- Validation of Javadoc presence.

Run Checkstyle manually:
```bash
./gradlew check
```
Reports are generated in `build/reports/checkstyle/`.

---

## **Dynamic Code Analysis**

Dynamic validations include:
- Blocking unauthorized usernames (e.g., containing `admin`).
- Scanning for critical keywords in modified files.

---

## **Logging**

Uses SLF4J with Lombok for structured logging. Logs are generated for:
- Incoming webhooks.
- User creation.
- Static analysis results.
- Critical keyword detections.

---

## **Database**

- **H2 in-memory database** is used for storing:
    - Commit details
    - User data
- Accessible at `http://localhost:8080/h2-console`
    - **JDBC URL**: `jdbc:h2:mem:testdb`
    - **Username**: `sa`
    - **Password**: (leave blank)

---

## **CI/CD Pipeline**

The project includes a GitHub Actions pipeline (`ci.yml`) with:
1. Java 21 setup and Gradle caching.
2. Build and static code analysis (`./gradlew build check`).
3. Optional application run during pull requests.

---

## **Future Enhancements**

- Integration of Machine Learning models for anomaly detection.
- Migration to a persistent database (e.g., PostgreSQL).
- Enhanced static analysis with SonarQube.

---

## **Known Limitations**

- Unit tests are avoided due to hardware constraints.
- The application relies on `ngrok` for webhook testing.

