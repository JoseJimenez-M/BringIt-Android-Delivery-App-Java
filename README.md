# 📦 BringIt: On-Demand Logistics and Delivery Platform

[![GitHub language count](https://img.shields.io/github/languages/count/JoseJimenez-M/BringIt-Android-Delivery-App-Java)](https://github.com/JoseJimenez-M/BringIt-Android-Delivery-App-Java)
[![GitHub top language](https://img.shields.io/github/languages/top/JoseJimenez-M/BringIt-Android-Delivery-App-Java)](https://github.com/JoseJimenez-M/BringIt-Android-Delivery-App-Java)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**BringIt** is a full-featured, native Android mobile application designed to simulate a modern, on-demand delivery and logistics service. It was developed to master key concepts in native Android development, focusing on robust architecture, secure communication, and real-time user experiences.

## 🛠️ Tech Stack & Key Technologies

This project showcases expertise in integrating advanced Android and third-party services.

| Category | Technology/Library | Description |
| :--- | :--- | :--- |
| **Primary Language** | **Java** | Core development language for the native Android application. |
| **Platform** | **Native Android** | Built for stability and performance across Android devices. |
| **Geolocation** | **Google Maps SDK** | Real-time map display, route calculation, and driver tracking. |
| **Location Services** | **Location Awareness & Places API** | Handling location permissions and address lookups. |
| **Data Communication** | **RESTful API Calls** | Asynchronous data fetching and updates using **JSON** payloads. |
| **Concurrency** | **`AsyncTasks` / `Threads`** | Efficient management of background operations (API calls) to keep the UI responsive. |
| **Data Layer** | **CRUD Operations** | Implemented against a persistent backend for user data and order management. |
| **UI/UX** | **Material Design** | Clean, intuitive interface with a high focus on activity flow (minimum 10 screens/fragments). |
| **Security** | **Authentication Modules** | Secure user login via **Email** and **SMS/Call** verification protocols. |

---

## ✨ Features Implemented

BringIt covers the entire lifecycle of an on-demand service, from user authentication to real-time tracking.

* **Secure Authentication:** Multi-factor authentication support (Email & Phone verification).
* **Real-Time Tracking:** Users can track the delivery vehicle on the map using integrated location services.
* **Persistent Data Management:** Full **CRUD** functionality for managing user profiles, order creation, and status updates.
* **Notification System:** Uses the **Notification Manager** to send timely alerts about order changes and driver arrival.
* **Complex Navigation Flow:** A deep structure featuring a minimum of **10 distinct activities/fragments/ListViews** to manage the ordering process.
* **Data Display:** Dynamic data presentation using `ListView` components, loading data asynchronously from the API.

---

## 🎨 Design & Prototype

The entire application flow, user journeys, and mock-ups were designed using **Figma** prior to development, ensuring a streamlined and intuitive user experience.

> **[View the Design Prototype on Figma](https://www.figma.com/design/jU5CDig08ld0ylWkXgtQNQ/ZE-DELIVERY?node-id=0-1&t=8vTOI3QXz7Tox8qk-1)**


## 🔑 Security & Configuration

This project relies on Google Services (Firebase/Google Maps). For security reasons, the configuration files are not included in the repository. The necessary configuration file, google-services.json, must be manually downloaded from your own Firebase project.

1. Create a Firebase Project: Set up a new project in the Firebase Console.

2. Register Android App: Register a new Android application within your Firebase project, ensuring the package name matches the one in this repository (com.example.bringit).

3. Download google-services.json: Download the generated google-services.json file.

4. Place the File: Place the downloaded file into the app/ directory of this project.

   Important Security Note: When enabling the Google Maps API key, ensure you add Android app restrictions to limit its use solely to the package name of this application. This prevents unauthorized usage and protects against unexpected charges.

Once configured:

1. Open the project in Android Studio.

2. Build and run on an Android emulator or device (API 21+ recommended).
---
## DEMO
1.Youtube:https://youtube.com/shorts/5cXU52d-EQY?feature=share

### Screenshots 

**1:**
<p align="center">
  <img src="https://github.com/user-attachments/assets/2407196f-004b-41ec-a71d-c27d1aad4797" width="30%" alt="Captura 4: Creación de Pedido"/>
  <img src="https://github.com/user-attachments/assets/3d110306-7d0a-47b8-aa6c-e933c0d1c039" width="30%" alt="Captura 5: Detalles del Ítem"/>
  <img src="https://github.com/user-attachments/assets/4144108d-2bda-455d-8471-e10f1dc9c0f8" width="30%" alt="Captura 6: Confirmación de Pago"/>
</p>

**2:**
<p align="center">
  <img src="https://github.com/user-attachments/assets/0be98932-1c03-4175-84e1-5530a4123c50" width="30%" alt="Captura 7: Perfil de Usuario"/>
  <img src="https://github.com/user-attachments/assets/4dc4026f-edcf-40d6-b5b0-c255711b42b6" width="30%" alt="Captura 8: Historial de Pedidos"/>
  <img src="https://github.com/user-attachments/assets/da6c6b41-70a7-4674-960f-51298140dd75" width="30%" alt="Captura 9: Configuración de Notificaciones"/>
</p>




