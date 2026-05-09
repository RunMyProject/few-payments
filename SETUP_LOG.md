# `SETUP_LOG.md`

## 💻 Workstation Specifications

* **User:** Edoardo Sabatini
* **Hardware:** HP OMEN Laptop 15-ek0xxx
* **Operating System:** Linux Mint 22.3 "Zena" (Xfce Edition)
* **Kernel:** Linux 6.8.0-110-generic
* **Project:** $FEW (few-payments)

---

## 📅 Session 1: Friday, May 8, 2026

**Objective:** Core Communication and Runtime Environment Setup.

| Timestamp | Category | Action | Terminal Command / Notes |
| --- | --- | --- | --- |
| **16:00** | **Apps** | **WhatsApp Desktop** | Installed desktop client for cross-device communication. |
| **16:15** | **Break** | **Coffee Break ☕** | A pause to reflect. |
| **16:40** | **Runtime** | **NVM Installation** | `curl -o- [https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh](https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh) | bash` |
| **16:45** | **Runtime** | **Node.js v22** | `nvm install 22` |
| **16:55** | **Frontend** | **Angular CLI** | `npm install -g @angular/cli` |
| **17:00** | **Manager** | **SDKMAN Setup** | `curl -s "[https://get.sdkman.io](https://get.sdkman.io)" | bash` |
| **17:15** | **Backend** | **Java 25 Installation** | `sdk install java 25-open` |
| **17:30** | **Brainstorm** | **$FEW Project Init** | Defined enterprise payment simulator using Spring Boot 3.5.x and SAGA Pattern. |
| **18:00** | **End Session**| _ |  _ |

---

## 📅 Session 2: Saturday, May 9, 2026

**Objective:** Infrastructure, IDE Configuration, and Git Synchronization.

| Timestamp | Category | Action | Terminal Command / Notes |
| --- | --- | --- | --- |
| **09:00** | **Virtualization** | **Docker & Compose** | `docker compose up -d` (Starting infrastructure services). |
| **09:15** | **IDE** | **VS Code** | Installation of Visual Studio Code primary editor. |
| **09:30** | **IDE Extension** | **Docker Extension** | Installed "ms-azuretools.vscode-docker" for container management. |
| **09:45** | **Infrastruct.** | **PostgreSQL Setup** | Created `Dockerfile`, `docker-compose.yml`, and `.env` for database orchestration. |
| **10:00** | **IDE Extension** | **Java Extension Pack** | Installed "vscjava.vscode-java-pack" for full JDK 25 support. |
| **10:15** | **IDE Extension** | **Spring Boot Ext.** | Installed "vscjava.vscode-spring-boot-dashboard" for microservices. |
| **10:15** | **AI Tooling** | **GitHub Copilot** | Activated AI pair-programming integration within VS Code. |
| **10:30** | **Git Config** | **Auth & Credentials** | `git config --global user.name "Edoardo Sabatini"` <br>

<br> `git remote set-url origin https://RunMyProject:ghp_***...` |
| **10:45** | **Build Tool** | **Apache Maven** | `sudo apt update && sudo apt install maven -y` |
| **10:45** | **Break** | **Coffee Break ☕** | A pause to reflect. |
| **11:00** | **Docs** | **Final Setup Log** | Compiled and finalized this documentation. |
| **11:15** | **End Session of Setup Log**| git push | _ |

---

## ✅ Environment Verification

As of May 9, 2026, 11:15 AM, the following environment is confirmed:

* **Java:** `OpenJDK 25` (2025-09-16)
* **Maven:** `Apache Maven 3.8.7`
* **Git Status:** `On branch main` (Your branch is up to date with 'origin/main').
* **Working Tree:** Clean (Nothing to commit).

---

