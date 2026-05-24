# Closira CRM — Native Android Edition

**Closira CRM** is a native dark-themed Customer Relationship Management (CRM) mobile dashboard designed for business owners to track, manage, and respond to incoming customer leads across **WhatsApp**, **Email**, and **Call** channels. 

This application is written entirely in **Kotlin** and built with **Jetpack Compose** following **Material Design 3 (M3)** guidelines. It translates the original dashboard mock specifications into a visually stunning, fully production-grade native Android application.

---

## 🎨 Design Concept & Visuals

Closira CRM sports a locked-in, custom premium dark aesthetic, maximizing negative space and color-coordinated visual visual hierarchy to represent high SLA alerts in real-time.

*   **Dark-Slate Theme:** Background uses deep ambient levels (`#111317` / `#1E2023`) which prevent eye fatigue on intensive CRM monitoring.
*   **Aesthetic Branding:** Accents pair pastel **Lavender** (`#CABEFF`) with vivid **Teal** (`#41EEC2`).
*   **Adaptive Responsive Layouts:** Fully container-sized to scale beautifully from compact handheld phone sizes up to wide tablet displays.
*   **Custom Adaptive App Icon:** Overlapping vector communication bubbles in brand gradients with automated AI sparks, fitting natively with Material You grids.

---

## 🛠️ Folder Structure

The code is organized according to Clean Architecture and MVVM guidelines:

```
app/src/main/
├── AndroidManifest.xml
├── java/com/example/
│   ├── MainActivity.kt           # Central routing stack & DI orchestration
│   ├── data/
│   │   ├── Entities.kt           # Room table definitions (Lead, Escalation, FollowUp)
│   │   ├── Daos.kt               # Reactive Room SQLite Queries (Flow-based)
│   │   ├── Converters.kt         # Moshi serializers for nested Message/Timeline arrays
│   │   ├── AppDatabase.kt        # Room database singleton
│   │   └── mock/
│   │       └── MockData.kt       # Direct static translations of initial spec data
│   │
│   ├── ui/
│   │   ├── CrmViewModel.kt       # Combinatorial search filters & actions engine
│   │   ├── components/
│   │   │   └── Components.kt    # Cohesive, high-touch UI widgets (Badges, Cards)
│   │   │
│   │   ├── screens/
│   │   │   ├── HomeScreen.kt     # Metric cards, quick actions & activity feed
│   │   │   ├── LeadsScreen.kt    # Real-time search engine with channel filter chips
│   │   │   ├── EscalationsScreen.kt # High SLA alerts sorted high-to-low with resolution
│   │   │   ├── FollowUpsScreen.kt   # Dual checklist dividing progress & completed tasks
│   │   │   └── ConversationDetailScreen.kt # Details, AI Insights of summary, timeline tracker
│   │   │
│   │   └── theme/
│   │       ├── Color.kt          # Source-of-truth semantic brand palettes
│   │       ├── Theme.kt          # MaterialTheme mapping for locked-in dark view state
│   │       └── Type.kt           # Spacing-matched typography configurations
│   │
│   └── utils/
│       └── TimeHelpers.kt        # Iso date-stamp and "time ago" formatting math
```

---

## ⚡ Technical Implementations & Flow

Every interaction is fully functional and responsive to state updates:

1.  **Reactive Combines:** Leads searches and category chip filters (`All`, `WhatsApp`, `Email`, `Call`) combine reactively inside `CrmViewModel` using Coroutine `combine` operators on DB flows. Results filter in real-time.
2.  **Room Database Persistence:** This implementation goes above and beyond simple in-memory variables by implementing full SQL desugared persistence. Clicking **Resolve** on an escalation deletes the entry from SQLite, and checking a follow-up task updates it instantly and shifts it to the Completed section. 
3.  **Timeline Progression:** Displays a horizontal progress timeline in detail sheets. The connecting rule bar automatically measures and fills green up to the current progress point.
4.  **AI Insights Summary:** Includes structured diagnostic cards displaying AI-generated summaries and targeted Playbook SOP completions for each client.
5.  **Touch Target Sizing:** All controls use standard component pads to guarantee cozy 48dp+ interactive targets matching the highest accessibility specifications.

---

## 🚀 Building and Running the App

### Compile the Project
The project uses the **Gradle (Kotlin DSL)** build system. You can compile the debug build immediately:

```bash
gradle assembleDebug
```

---

## Jetpack Compose Layouts vs Legacy XML Layout

This application leverages standard modern **Jetpack Compose declarative states** rather than legacy XML layouts to construct its view layers. 
*   **Predictable Performance:** State flows (`StateFlow`) trigger localized composition recalculations only when relevant properties edit, ensuring 60fps performance on budget hardware.
*   **Simplified Styling:** Designing compound elements (like custom badge color palettes) is done using state expressions rather than countless separate XML selector drawables, making layout codes highly legible and simple to maintain.
