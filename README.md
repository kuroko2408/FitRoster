# FitRoster

FitRoster is a full-stack fitness and nutrition platform designed to bridge the connection between coaches and athletes. It features a robust workout builder, detailed set/weight tracking, and macro/nutrition monitoring.

## 🛠 Tech Stack

* **Backend:** Kotlin, Spring Boot 3.x, Spring Data JPA (Hibernate)
* **Database:** PostgreSQL (Containerized)
* **Infrastructure:** Docker & Docker Compose
* **Frontend:** Android (Kotlin, Retrofit, Gson)

---

## 🚀 Getting Started (Backend)

The backend is fully containerized. You do not need to install PostgreSQL locally on your host machine—Docker handles the database and volume mapping automatically.

### Prerequisites
* [Docker Desktop](https://www.docker.com/products/docker-desktop/) (or Docker Engine + Compose plugin on Linux)
* Java 17+ (JDK)
* Git

### 1. Clone the Repository
```bash
git clone [https://github.com/YOUR_GITHUB_USERNAME/FitRoster.git](https://github.com/YOUR_GITHUB_USERNAME/FitRoster.git)
cd FitRoster/backend
2. Build the Application
Before spinning up the containers, build the Spring Boot .jar file.

Linux/Mac: ./gradlew clean build -x test

Windows: .\gradlew.bat clean build -x test

3. Start the Server and Database
Use Docker Compose to build the server image and boot up the PostgreSQL database.

Bash
docker compose up -d --build
The API will be available at http://localhost:8080.

Database Reset Command
If you ever need to completely wipe the database (useful for schema changes or clearing test data), run:

Bash
docker compose down -v
📱 Getting Started (Android Frontend)
The Android app communicates with the backend via Retrofit. When testing on a physical device, the app must be pointed to the IP address of the machine hosting the backend.

1. Find the Host IP Address
If both your phone and the hosting computer are on the same Wi-Fi network (or a Mobile Hotspot), find the computer's local IP address (e.g., 192.168.43.x):

Windows: Run ipconfig in Command Prompt.

Linux/Mac: Run ip a or ifconfig in Terminal.

2. Update the Base URL
Open the Android project in Android Studio. Locate your Retrofit API client builder or your build.gradle.kts file and update the base URL to match the host machine:

Kotlin
// Example
baseUrl("[http://192.168.43.](http://192.168.43.)X:8080/") 
3. Run the App
Sync Gradle and deploy the application to your physical device or emulator.

🌐 Local Network Deployment (Mobile Hotspot Setup)
If you are deploying this to a secondary machine (like a friend's laptop) in an environment with restrictive Wi-Fi (e.g., a university campus), use a mobile hotspot to bypass firewalls:

Turn on the Mobile Hotspot on your Android device.

Connect the laptop to the hotspot.

Run ipconfig (Windows) or ip a (Linux) on the laptop to find its new IP address assigned by your phone.

Update the Android BASE_URL to this new IP address.

Run docker compose up -d on the laptop.

Install the Android app to your phone and connect.
