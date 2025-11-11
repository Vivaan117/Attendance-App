# 🧾 Attendance Manager (Java Swing Edition)

**Attendance Manager Swing** is a modern **desktop-based attendance tracking system** built entirely using **Java Swing**.
It provides an intuitive GUI for marking, managing, analyzing, and predicting student attendance — designed for teachers, institutions, and students to monitor attendance efficiently.

---

## 🚀 Features

✅ **Secure Login System**
Simple authentication gateway to access the main dashboard.

✅ **Interactive Dashboard**

* Displays **Total Classes**, **Classes Attended**, and **Attendance Percentage**.
* Provides real-time stats and insights.

✅ **Mark Attendance**

* Choose the **subject** and **status (Present/Absent)** easily.
* Automatically logs **timestamped entries**.

✅ **Smart Attendance Table**

* View recent attendance records in a scrollable table.
* Delete selected entries instantly.

✅ **Attendance Predictor**

* Predict future attendance based on your current rate.
* Enter your **target percentage** and **upcoming classes** — get actionable insights like
  *“You need to attend 3 more classes to reach 75%.”*

✅ **Responsive Toast Notifications**
Stylish in-app messages for user feedback (e.g., login success, errors, etc.).

✅ **Modern UI & Experience**

* Inspired by Google’s **Material Design** colors.
* Smooth layouts with **clean fonts, padding, and shadows** for better readability.

---

## 🧰 Tech Stack

| Layer               | Technology Used                                              |
| ------------------- | ------------------------------------------------------------ |
| **Frontend GUI**    | Java Swing (`JFrame`, `JPanel`, `JTable`, `JTabbedPane`)     |
| **Backend Logic**   | Core Java (`Collections`, `UUID`, `LocalDateTime`)           |
| **UI Styling**      | Custom Swing Components, `CardLayout`, Borders, Color Themes |
| **Data Management** | In-memory list (`ArrayList<AttendanceRecord>`)               |

---

## 🧩 Class Structure

```
AttendanceManagerSwing.java
│
├── buildLoginPanel()         → Handles login interface
├── buildMainPanel()          → Contains Dashboard and Predictor tabs
│   ├── buildDashboardPanel() → Attendance marking, table, and stats
│   └── buildPredictorPanel() → Attendance prediction calculator
│
├── markAttendanceAction()    → Adds attendance entries
├── refreshAttendanceTable()  → Updates the JTable
├── updateDashboard()         → Recomputes attendance metrics
├── performPrediction()       → Attendance prediction logic
├── showToast()               → On-screen feedback notification
│
└── AttendanceRecord (Inner Class)
```

---

## 📸 Screenshots (Optional)

> You can add actual screenshots once you run the program —
> name them like `screenshot_login.png`, `screenshot_dashboard.png`, etc.

|                 Login Screen                 |                   Dashboard                   |                   Predictor                   |
| :------------------------------------------: | :-------------------------------------------: | :-------------------------------------------: |
| ![Login Screen](assets/screenshot_login.png) | ![Dashboard](assets/screenshot_dashboard.png) | ![Predictor](assets/screenshot_predictor.png) |

---

## ⚙️ Setup & Run Instructions

### 1️⃣ Prerequisites

Make sure you have:

* **Java JDK 17+** installed
* Any IDE that supports Java (like IntelliJ IDEA / Eclipse / VS Code)

### 2️⃣ Clone the Repository

```bash
git clone https://github.com/<your-username>/AttendanceManagerSwing.git
cd AttendanceManagerSwing
```

### 3️⃣ Compile and Run

If you’re using terminal:

```bash
javac AttendanceManagerSwing.java
java AttendanceManagerSwing
```

Or simply **run the file in your IDE** (`AttendanceManagerSwing.java`).

---

## 🧮 Attendance Prediction Formula

The predictor calculates attendance needs based on:

```
Required_Classes = ceil((Target% / 100) * (Total + Upcoming))
Need_To_Attend = Required_Classes - Current_Attended
```

Example:

> Current: 60% (6/10)
> Target: 75%
> Upcoming: 5
> Result → “You need to attend 4 more classes to reach 75%.”

---

## 🧠 Learning Highlights

This project demonstrates:

* Event-driven programming in Java
* Swing component hierarchy and layout management
* MVC-style design using separate data model (`AttendanceRecord`)
* Dynamic UI updates and data binding
* Real-world logic implementation (attendance calculation + prediction)

---

## 🌈 UI Design Principles

* **Consistency:** Fonts, margins, and colors follow a unified theme.
* **Minimalism:** Focus on content clarity over visual clutter.
* **Accessibility:** High contrast labels and button colors for readability.
* **Responsiveness:** Works well across different window sizes.

---

## 🔒 Security Notes

* Basic login is **not connected to a database** — it’s a UI simulation.
* Can be extended easily with:

  * JDBC integration
  * Encrypted password storage
  * Multi-user roles (Student / Teacher)

---

## 🧱 Future Enhancements

🔹 Add **database support** (SQLite / MySQL)
🔹 Export attendance data to **CSV or PDF**
🔹 Add **graphical analytics (charts)**
🔹 Enable **multi-user authentication system**
🔹 Add **email notifications or reminders**

---

## 👨‍💻 Author

**👤 Vivaan Sheth**
🎓 B.Tech CSE (Data Science), SRM University
📍 Surat, Gujarat, India
⚽ Passionate about football, culture, and software development.

---

## 🪪 License

This project is licensed under the **MIT License** — you’re free to use, modify, and distribute it with attribution.

