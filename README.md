# Eclipse Sample Data Plugin

This project demonstrates a simple client-server architecture with two components:

## 1. Mock API (Spring Boot)

A basic REST API that serves sample issue data.

**Requirements:** Java 21

**Running:**
```bash
cd mock-api/mock-api
./gradlew :boot:run
```

The API runs on `localhost:8080` with one endpoint:
- `GET /api/issues` - Returns a list of issues

## 2. Sample Data Eclipse Plugin

An Eclipse plugin that adds an "Issues" view with a button to fetch data from the mock API.

**Requirements:** 
- Java 21
- Eclipse IDE with PDE (for RCP and RAP Developers)

**Running:**
1. Import the `sampledata/` folder into Eclipse as an existing project
2. Right-click the project → Run As → Eclipse Application
3. In the new Eclipse instance: Window → Show View → Other → Sample Category → Issues
4. Click "Fetch Data" to load issues from the API

## Notes

Make sure the mock API is running before using the Eclipse plugin. Both projects use Java 21.
