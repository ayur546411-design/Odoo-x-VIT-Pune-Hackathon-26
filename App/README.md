<p align="center">
  <img src="https://img.shields.io/badge/ExpenseFlow-Smart%20Reimbursement-0D9488?style=for-the-badge&logoColor=white" alt="ExpenseFlow" height="40"/>
</p>

<h1 align="center">💸 ExpenseFlow</h1>
<p align="center">
  <strong>AI-Powered Expense Reimbursement & Multi-Level Approval Engine</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-Jetpack%20Compose-7F52FF?style=flat-square&logo=kotlin&logoColor=white" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/FastAPI-0.100+-009688?style=flat-square&logo=fastapi&logoColor=white" alt="FastAPI"/>
  <img src="https://img.shields.io/badge/MySQL-8.0+-4479A1?style=flat-square&logo=mysql&logoColor=white" alt="MySQL"/>
  <img src="https://img.shields.io/badge/Python-3.11+-3776AB?style=flat-square&logo=python&logoColor=white" alt="Python"/>
  <img src="https://img.shields.io/badge/ML%20Kit-On%20Device%20OCR-4285F4?style=flat-square&logo=google&logoColor=white" alt="ML Kit"/>
  <img src="https://img.shields.io/badge/OCR-Tesseract%20%7C%20PaddleOCR-FF6F00?style=flat-square" alt="OCR"/>
  <img src="https://img.shields.io/badge/License-MIT-green?style=flat-square" alt="License"/>
</p>

<p align="center">
  <a href="#-problem-statement">Problem</a> •
  <a href="#-solution">Solution</a> •
  <a href="#-architecture">Architecture</a> •
  <a href="#-features">Features</a> •
  <a href="#-tech-stack">Tech Stack</a> •
  <a href="#-getting-started">Setup</a> •
  <a href="#-api-reference">API</a> •
  <a href="#-database-schema">Schema</a>
</p>

---

## 🔴 Problem Statement

Companies lose **thousands of hours annually** on manual expense reimbursement. The existing process is:

| Pain Point | Impact |
|:---|:---|
| **Manual form filling** | Employees waste 30+ mins per claim |
| **Email-based approvals** | Claims get lost, no audit trail |
| **No threshold-based routing** | Same flow for ₹500 and ₹5,00,000 |
| **Single-level approvals** | No multi-step verification for large amounts |
| **No conditional logic** | Can't define "if CFO approves, skip rest" |
| **Manual currency handling** | Global teams struggle with multi-currency claims |
| **Paper receipts** | Manual data entry from physical bills |

> **There is no simple, configurable system that supports multi-level approvals, conditional rules (percentage-based, specific approver, hybrid), OCR receipt scanning, and multi-currency conversion — all in one platform.**

---

## ✅ Solution

**ExpenseFlow** is a mobile-first expense reimbursement platform (Android) backed by a Python FastAPI server, with an intelligent approval engine that supports:

```
┌─────────────────────────────────────────────────────────────────┐
│                                                                 │
│   📱 Scan Receipt → 🤖 OCR Auto-Fill → 📝 Submit Expense      │
│          ↓                                                      │
│   💱 Auto Currency Conversion (SGD → INR)                       │
│          ↓                                                      │
│   🔀 Smart Approval Routing                                    │
│      ├── Sequential: Manager → Finance → Director               │
│      ├── Conditional: 60% approve = Done                        │
│      ├── Override: CFO approves = Auto-approved                 │
│      └── Hybrid: Sequential + Conditional combined              │
│          ↓                                                      │
│   ✅ Approved / ❌ Rejected (with comments & audit trail)       │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🏗 Architecture

```
┌──────────────────────────────────────────────────────────────┐
│                     CLIENT LAYER                             │
│         ┌──────────────────────────────────────┐             │
│         │     Kotlin + Jetpack Compose          │            │
│         │     (Android App)                     │            │
│         │                                       │            │
│         │  • Expense submission & history        │            │
│         │  • On-device OCR (ML Kit)              │            │
│         │  • Approval queue (Manager/Admin)       │           │
│         │  • User & workflow management (Admin)   │           │
│         └──────────────────┬───────────────────┘             │
└────────────────────────────┼─────────────────────────────────┘
                             │  REST API (JWT Auth)
                             ▼
┌──────────────────────────────────────────────────────────────┐
│                     API LAYER (FastAPI)                       │
│                                                              │
│  ┌──────────┐ ┌───────────────┐ ┌─────────┐ ┌────────────┐  │
│  │ Auth &   │ │  Approval     │ │ OCR     │ │ Currency   │  │
│  │ Users    │ │  Engine       │ │ Service │ │ Service    │  │
│  └──────────┘ └───────────────┘ └─────────┘ └────────────┘  │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐    │
│  │              Workflow State Machine                    │   │
│  │  PENDING → IN_REVIEW → [STEP 1..N] → APPROVED/REJECTED│  │
│  └──────────────────────────────────────────────────────┘    │
└──────────────────────────┬───────────────────────────────────┘
                           │
            ┌──────────────┼──────────────┐
            ▼              ▼              ▼
     ┌────────────┐ ┌────────────┐ ┌─────────────────┐
     │   MySQL    │ │ REST       │ │ Exchange Rate   │
     │  Database  │ │ Countries  │ │ API             │
     │            │ │ API        │ │                 │
     └────────────┘ └────────────┘ └─────────────────┘
```

---

## ✨ Features

### 🔐 Authentication & Company Management
- **Auto-provisioning**: First signup creates a Company + Admin automatically
- **Country-aware**: Company currency set from selected country via [REST Countries API](https://restcountries.com/v3.1/all?fields=name,currencies)
- **Role-Based Access**: Admin / Manager / Employee with granular permissions
- **Manager relationships**: Admin defines reporting hierarchy

### 📝 Expense Submission
- **Multi-currency support**: Submit in any currency, auto-converted to company default
- **Real-time conversion**: Powered by [Exchange Rate API](https://api.exchangerate-api.com/v4/latest/USD)
- **Rich metadata**: Amount, category, description, date, receipt attachment
- **History tracking**: View approved, rejected, and pending expenses

### 🔀 Multi-Level Approval Workflow

```
                    ┌──────────────┐
                    │   Employee   │
                    │   Submits    │
                    └──────┬───────┘
                           │
                    ┌──────▼───────┐     ┌─────────────────┐
                    │ IS_MANAGER   │────▶│ Direct Manager  │
                    │ APPROVER? ✓  │     │ (Auto Step 1)   │
                    └──────┬───────┘     └────────┬────────┘
                           │ No                   │ Approve
                           ▼                      ▼
                    ┌──────────────────────────────────┐
                    │     SEQUENTIAL CHAIN              │
                    │  Step 1 → Step 2 → ... → Step N  │
                    │  (Admin-defined order)            │
                    └──────────────┬───────────────────┘
                                   │
                    ┌──────────────▼───────────────────┐
                    │     CONDITIONAL RULES             │
                    │                                   │
                    │  📊 Percentage: 60% approve → ✅  │
                    │  👤 Specific: CFO approve → ✅    │
                    │  🔄 Hybrid: 60% OR CFO → ✅      │
                    └──────────────┬───────────────────┘
                                   │
                    ┌──────────────▼───────────────┐
                    │   ✅ APPROVED  or  ❌ REJECTED │
                    │   (with comments + audit log) │
                    └──────────────────────────────┘
```

### 🧾 OCR Receipt Scanning (AI-Powered)
- **On-device OCR** (Android): ML Kit Text Recognition for offline-first scanning
- **Server-side fallback**: PaddleOCR / Tesseract with preprocessing pipeline
- **Auto-extracted fields**: Amount, date, vendor name, expense type, line items
- **Snap & Submit**: Take a photo from camera or pick from gallery → AI reads the receipt

```
Receipt Image (Camera / Gallery)
    │
    ▼
┌──────────────────┐
│  Preprocessing   │  Grayscale → Denoise → Threshold → Deskew
└────────┬─────────┘
         ▼
┌──────────────────┐
│  OCR Engine      │  ML Kit (on-device) / Tesseract (server fallback)
└────────┬─────────┘
         ▼
┌──────────────────┐
│  Field Extractor │  Regex + NLP patterns for:
│                  │  • Amount (₹, $, total, grand total)
│                  │  • Date (DD/MM/YYYY, Mon DD YYYY, etc.)
│                  │  • Vendor (top-of-receipt text)
│                  │  • Category (restaurant→Food, uber→Travel)
└────────┬─────────┘
         ▼
┌──────────────────┐
│  Auto-Fill Form  │  Pre-populated expense submission
└──────────────────┘
```

### 💱 Multi-Currency Engine
- Fetches country list + currencies from REST Countries API
- Real-time conversion rates from Exchange Rate API
- Manager always sees amounts in company's default currency
- Supports 160+ currencies

---

## 🛠 Tech Stack

| Layer | Technology | Purpose |
|:---|:---|:---|
| **Mobile App** | Kotlin + Jetpack Compose | Full-featured Android app (all roles) |
| **Backend API** | Python + FastAPI | REST API, business logic, auth |
| **Database** | MySQL 8.0+ + SQLAlchemy ORM | Relational data, workflow state |
| **Auth** | JWT (PyJWT) + bcrypt | Token-based authentication |
| **OCR (On-Device)** | Google ML Kit Text Recognition | Offline receipt scanning on Android |
| **OCR (Server)** | Tesseract / PaddleOCR + OpenCV | Server-side fallback for complex receipts |
| **Currency** | Exchange Rate API | Real-time forex conversion |
| **Countries** | REST Countries API | Country → currency mapping |
| **Networking** | Retrofit + OkHttp | Type-safe API client on Android |
| **DI** | Hilt (Dagger) | Dependency injection on Android |
| **Image Loading** | Coil | Receipt image loading & caching |

---

## 📂 Project Structure

```
expenseflow/
├── backend/                        # Python FastAPI server
│   ├── app/
│   │   ├── main.py                 # FastAPI app entry point
│   │   ├── config.py               # Environment config
│   │   ├── database.py             # SQLAlchemy engine & session (MySQL)
│   │   ├── models/                 # SQLAlchemy ORM models
│   │   │   ├── user.py
│   │   │   ├── company.py
│   │   │   ├── expense.py
│   │   │   ├── workflow.py
│   │   │   └── approval.py
│   │   ├── schemas/                # Pydantic request/response schemas
│   │   ├── routers/                # API route handlers
│   │   │   ├── auth.py
│   │   │   ├── users.py
│   │   │   ├── expenses.py
│   │   │   ├── workflows.py
│   │   │   └── ocr.py
│   │   ├── services/               # Business logic layer
│   │   │   ├── approval_engine.py  # Core workflow engine
│   │   │   ├── currency_service.py # Forex conversion
│   │   │   └── ocr_service.py      # Receipt parsing
│   │   └── utils/
│   │       ├── auth.py             # JWT helpers
│   │       └── permissions.py      # RBAC decorators
│   ├── alembic/                    # Database migrations
│   ├── tests/
│   └── requirements.txt
│
├── android/                        # Kotlin Android app
│   ├── app/src/main/
│   │   ├── java/com/expenseflow/
│   │   │   ├── di/                 # Hilt dependency injection modules
│   │   │   ├── data/
│   │   │   │   ├── remote/         # Retrofit API service & DTOs
│   │   │   │   ├── local/          # Room DB (offline cache)
│   │   │   │   └── repository/     # Repository implementations
│   │   │   ├── domain/
│   │   │   │   ├── model/          # Domain models
│   │   │   │   └── usecase/        # Business use cases
│   │   │   ├── ui/
│   │   │   │   ├── auth/           # Login & Signup screens
│   │   │   │   ├── expense/        # Submit, history, detail screens
│   │   │   │   ├── approval/       # Approval queue & actions
│   │   │   │   ├── admin/          # User mgmt, workflow config
│   │   │   │   ├── ocr/            # Camera capture + OCR pipeline
│   │   │   │   ├── components/     # Shared Compose components
│   │   │   │   ├── navigation/     # NavHost + role-based routing
│   │   │   │   └── theme/          # Material 3 theme & colors
│   │   │   └── ExpenseFlowApp.kt   # Application class (Hilt entry)
│   │   └── res/
│   ├── build.gradle.kts
│   └── gradle/libs.versions.toml   # Version catalog
│
├── .claude/                        # Claude Code configuration
├── .env.example
└── README.md
```

---

## 🗄 Database Schema

```sql
-- Auto-created on first signup
CREATE TABLE companies (
    id              CHAR(36) PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    country         VARCHAR(100) NOT NULL,
    default_currency VARCHAR(3) NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Supports Admin, Manager, Employee roles
CREATE TABLE users (
    id              CHAR(36) PRIMARY KEY,
    company_id      CHAR(36) NOT NULL,
    name            VARCHAR(255) NOT NULL,
    email           VARCHAR(255) UNIQUE NOT NULL,
    password_hash   TEXT NOT NULL,
    role            ENUM('ADMIN','MANAGER','EMPLOYEE') NOT NULL,
    manager_id      CHAR(36) DEFAULT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (company_id) REFERENCES companies(id),
    FOREIGN KEY (manager_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Multi-currency expense claims
CREATE TABLE expenses (
    id              CHAR(36) PRIMARY KEY,
    user_id         CHAR(36) NOT NULL,
    company_id      CHAR(36) NOT NULL,
    amount          DECIMAL(12,2) NOT NULL,
    currency        VARCHAR(3) NOT NULL,
    converted_amount DECIMAL(12,2),
    exchange_rate   DECIMAL(10,6),
    category        VARCHAR(50) NOT NULL,
    description     TEXT,
    expense_date    DATE NOT NULL,
    receipt_url     TEXT,
    status          ENUM('PENDING','IN_REVIEW','APPROVED','REJECTED') DEFAULT 'PENDING',
    current_step    INT DEFAULT 0,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (company_id) REFERENCES companies(id),
    INDEX ix_expenses_status (status),
    INDEX ix_expenses_company_id (company_id),
    INDEX ix_expenses_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Admin-defined approval chains
CREATE TABLE approval_workflows (
    id              CHAR(36) PRIMARY KEY,
    company_id      CHAR(36) NOT NULL,
    name            VARCHAR(255) NOT NULL,
    is_manager_first BOOLEAN DEFAULT FALSE,
    is_active       BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (company_id) REFERENCES companies(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Ordered steps in the chain
CREATE TABLE workflow_steps (
    id              CHAR(36) PRIMARY KEY,
    workflow_id     CHAR(36) NOT NULL,
    step_order      INT NOT NULL,
    approver_id     CHAR(36) NOT NULL,
    step_type       ENUM('SEQUENTIAL','CONDITIONAL') DEFAULT 'SEQUENTIAL',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (workflow_id) REFERENCES approval_workflows(id),
    FOREIGN KEY (approver_id) REFERENCES users(id),
    INDEX ix_steps_workflow_id (workflow_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Conditional approval rules
CREATE TABLE approval_rules (
    id                   CHAR(36) PRIMARY KEY,
    workflow_id          CHAR(36) NOT NULL,
    rule_type            ENUM('PERCENTAGE','SPECIFIC_APPROVER','HYBRID') NOT NULL,
    percentage_threshold DECIMAL(5,2),
    specific_approver_id CHAR(36) DEFAULT NULL,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (workflow_id) REFERENCES approval_workflows(id),
    FOREIGN KEY (specific_approver_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Audit trail for every approval action (INSERT-ONLY)
CREATE TABLE approval_actions (
    id              CHAR(36) PRIMARY KEY,
    expense_id      CHAR(36) NOT NULL,
    step_id         CHAR(36),
    approver_id     CHAR(36) NOT NULL,
    action          ENUM('APPROVED','REJECTED') NOT NULL,
    comment         TEXT,
    acted_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (expense_id) REFERENCES expenses(id),
    FOREIGN KEY (step_id) REFERENCES workflow_steps(id),
    FOREIGN KEY (approver_id) REFERENCES users(id),
    INDEX ix_actions_expense_id (expense_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### Entity Relationship

```
companies  1──────M  users
companies  1──────M  expenses
companies  1──────M  approval_workflows
users      1──────M  expenses              (submitter)
users      1──────M  users                 (manager → reports)
users      1──────M  workflow_steps         (approver)
users      1──────M  approval_actions       (actor)
approval_workflows  1──M  workflow_steps
approval_workflows  1──M  approval_rules
expenses   1──────M  approval_actions
```

---

## 📡 API Reference

### Authentication
| Method | Endpoint | Description |
|:---|:---|:---|
| `POST` | `/api/v1/auth/signup` | Register → auto-create company + admin |
| `POST` | `/api/v1/auth/login` | Login → returns JWT access token |

### User Management (Admin only)
| Method | Endpoint | Description |
|:---|:---|:---|
| `POST` | `/api/v1/users` | Create employee or manager |
| `GET` | `/api/v1/users` | List all company users |
| `PATCH` | `/api/v1/users/{id}/role` | Change user role |
| `PATCH` | `/api/v1/users/{id}/manager` | Assign manager |

### Expenses
| Method | Endpoint | Description |
|:---|:---|:---|
| `POST` | `/api/v1/expenses` | Submit expense (auto currency convert) |
| `GET` | `/api/v1/expenses/mine` | Employee's expense history |
| `GET` | `/api/v1/expenses/pending` | Manager's approval queue |
| `GET` | `/api/v1/expenses/all` | Admin view: all company expenses |
| `POST` | `/api/v1/expenses/{id}/approve` | Approve with comment |
| `POST` | `/api/v1/expenses/{id}/reject` | Reject with comment |

### Workflow Configuration (Admin only)
| Method | Endpoint | Description |
|:---|:---|:---|
| `POST` | `/api/v1/workflows` | Create approval workflow |
| `POST` | `/api/v1/workflows/{id}/steps` | Add approval step |
| `POST` | `/api/v1/workflows/{id}/rules` | Add conditional rule |
| `GET` | `/api/v1/workflows` | List workflows |

### OCR & Utilities
| Method | Endpoint | Description |
|:---|:---|:---|
| `POST` | `/api/v1/ocr/scan` | Upload receipt → returns extracted fields |
| `GET` | `/api/v1/countries` | List countries with currencies |
| `GET` | `/api/v1/exchange-rate/{base}` | Get conversion rates |

---

## 🚀 Getting Started

### Prerequisites
- Python 3.11+
- MySQL 8.0+
- Android Studio Hedgehog+ (for the app)

### Backend Setup

```bash
# Clone the repository
git clone https://github.com/bluedorsey/expenseflow.git
cd expenseflow/backend

# Create virtual environment
python -m venv venv
source venv/bin/activate          # Linux/Mac
# venv\Scripts\activate           # Windows

# Install dependencies
pip install -r requirements.txt

# Create MySQL database
mysql -u root -p -e "CREATE DATABASE expenseflow CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# Set up environment variables
cp .env.example .env
# Edit .env with your DATABASE_URL, JWT_SECRET, etc.

# Run database migrations
alembic upgrade head

# Start the server
uvicorn app.main:app --reload --port 8000

# Swagger docs available at: http://localhost:8000/docs
```

### Android Setup

```bash
cd expenseflow/android

# Open in Android Studio
# 1. File → Open → select the android/ folder
# 2. Wait for Gradle sync to complete
# 3. Update BASE_URL in local.properties:
#    BASE_URL=http://10.0.2.2:8000/api/v1    (emulator)
#    BASE_URL=http://YOUR_IP:8000/api/v1     (physical device)
# 4. Run on device/emulator (min SDK 26, target SDK 34)
```

---

## 🔑 Role-Based Permissions

| Capability | Admin | Manager | Employee |
|:---|:---:|:---:|:---:|
| Create company (auto on signup) | ✅ | — | — |
| Manage users & roles | ✅ | — | — |
| Configure approval workflows | ✅ | — | — |
| Configure conditional rules | ✅ | — | — |
| View all company expenses | ✅ | — | — |
| Override approvals | ✅ | — | — |
| Approve/reject expenses | ✅ | ✅ | — |
| View team expenses | ✅ | ✅ | — |
| Escalate per rules | ✅ | ✅ | — |
| Submit expenses | ✅ | ✅ | ✅ |
| Scan receipts (OCR) | ✅ | ✅ | ✅ |
| View own expenses & status | ✅ | ✅ | ✅ |

---

## 🧠 Approval Engine Algorithm

```python
def process_approval(expense_id, approver_id, action, comment):
    expense = get_expense(expense_id)
    workflow = get_workflow(expense.company_id)

    # Record the action
    save_action(expense_id, approver_id, action, comment)

    if action == "REJECTED":
        expense.status = "REJECTED"
        notify_employee(expense, comment)
        return

    # Check conditional rules
    rules = get_rules(workflow.id)
    for rule in rules:
        if rule.type == "SPECIFIC_APPROVER":
            if approver_id == rule.specific_approver_id:
                expense.status = "APPROVED"      # CFO override
                return

        if rule.type == "PERCENTAGE":
            approval_count = count_approvals(expense_id)
            total_approvers = count_total_approvers(workflow.id)
            if (approval_count / total_approvers) >= rule.threshold:
                expense.status = "APPROVED"       # 60% threshold met
                return

        if rule.type == "HYBRID":
            if approver_id == rule.specific_approver_id:
                expense.status = "APPROVED"
                return
            if (approval_count / total_approvers) >= rule.threshold:
                expense.status = "APPROVED"
                return

    # No conditional rule triggered → move to next step
    next_step = get_next_step(workflow.id, expense.current_step)
    if next_step:
        expense.current_step += 1
        create_approval_request(expense, next_step.approver_id)
    else:
        expense.status = "APPROVED"               # All steps complete
```

---

## 🧪 Testing

```bash
# Backend tests
cd backend
pytest tests/ -v --cov=app

# Android unit tests
cd android
./gradlew test

# Android instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest
```

---



<p align="center">
  <strong>Built with 💚 for smarter expense management</strong>
  <br/>
  <sub>If you found this useful, give it a ⭐ on GitHub!</sub>
</p>