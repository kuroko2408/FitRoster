# Product Requirement Document (PRD): Coach-Athlete Fitness & Nutrition Platform

## 1. Executive Summary & Value Proposition

### 1.1 Overview
The platform is a **B2B2C online coaching management ecosystem** designed to streamline interactions between fitness coaches and their clients/athletes. Similar in structural concept to *Google Classroom*, the system provides a centralized dashboard for coaches to create, assign, and monitor training and nutrition programs while offering athletes a friction-free mobile application to log workouts, track daily macro/micronutrients, and receive feedback.

### 1.2 Business & Distribution Model
* **Primary Target Customer:** Independent fitness coaches, personal trainers, online prep coaches, and boutique fitness studios.
* **Secondary End Users:** Athletes and client trainees managed by the coaches.
* **Distribution Strategy (B2B2C Virality):** Acquire the coach as the core paying subscriber. Each onboarded coach acts as a direct distribution channel, inviting 10 to 50+ athletes to download and use the app as part of their service package.

---

## 2. User Roles & Key Workflows

```
┌─────────────────────────────────────────────────────────────────┐
│                          COACH PORTAL                           │
│  • Program Design (Workouts & Macros)                           │
│  • Roster Compliance Dashboard                                  │
│  • Video Form Reviews & Feedback                               │
└────────────────────────────────┬────────────────────────────────┘
                                 │ Invites & Program Push
                                 ▼
┌─────────────────────────────────────────────────────────────────┐
│                           ATHLETE APP                           │
│  • Interactive Gym Checklist & Rest Timers                      │
│  • Food & Macro Tracking (Barcode / Search)                     │
│  • Progress Submissions & Form Clips                            │
└─────────────────────────────────────────────────────────────────┘
```

### 2.1 The Coach (Primary Persona)
* **Goal:** Maximize client retention and scale client volume without increasing administrative overhead.
* **Core Workflows:**
  * Build and maintain a reusable library of workout programs and exercise demonstrations.
  * Assign individual or group training schedules with target loads, set/rep ranges, and target RPE (Rate of Perceived Exertion).
  * Configure custom macro/micronutrient targets (distinguishing between Training Days and Rest Days).
  * Review client compliance metrics and form analysis videos from a unified feed.

### 2.2 The Athlete (Secondary Persona)
* **Goal:** Execute assigned routines with zero ambiguity and track progress effortlessly during workout sessions.
* **Core Workflows:**
  * Open daily agenda, follow workout checklists, and record weight/reps with built-in rest timers.
  * Log daily meals against target nutrition goals set by their coach.
  * Submit weekly check-ins, progress photos, and form-check video clips.

---

## 3. Detailed Feature Specifications

### 3.1 Module A: Workout & Training Management

| Feature | Description | Platform | Priority |
| :--- | :--- | :--- | :--- |
| **Workout Builder** | Drag-and-drop builder supporting sets, reps, target weight, RPE, tempo, and rest intervals. | Web / Tablet | MVP (P0) |
| **Template Library** | Save and clone exercise blocks, workout days, or multi-week phases (e.g., "4-Week Hypertrophy Push/Pull/Legs"). | Web / Mobile | MVP (P0) |
| **Interactive Gym Logger** | Checklist-style interface for athletes with auto-advancing rest timers and target history lookup. | Mobile | MVP (P0) |
| **Offline Sync** | Store workout plans locally and queue submissions when gym cellular signal is weak. | Mobile | P1 |
| **Form Check Integration** | Attach up to 15-second video clips to specific set entries for coach review. | Mobile / Web | P1 |

### 3.2 Module B: Nutrition & Macro Tracking

| Feature | Description | Platform | Priority |
| :--- | :--- | :--- | :--- |
| **Macro Allocator** | Set daily targets for Protein, Carbohydrates, Fats, Fibre, and total Calories. | Web / Mobile | MVP (P0) |
| **Split Target Profiles** | Automated target switching between Training Days, Rest Days, and Refeed Days. | Web / Mobile | MVP (P0) |
| **Food Logging Engine** | Barcode scanning and verified database search for logging meals and custom items. | Mobile | MVP (P0) |
| **Micronutrient Tracking** | Optional tracking for Fibre, Sodium, Water intake, and custom supplement checklists. | Mobile / Web | P1 |

### 3.3 Module C: Coach Dashboard & Client Compliance

| Feature | Description | Platform | Priority |
| :--- | :--- | :--- | :--- |
| **Roster Compliance Score** | Aggregate score (0–100%) showing workout completion rate and nutrition target adherence over 7/30 days. | Web / Mobile | MVP (P0) |
| **Activity Feed** | Real-time notifications when athletes complete workouts, log meals, or request feedback. | Web / Mobile | MVP (P0) |
| **In-App Messaging** | Direct messaging with context tags (link feedback directly to a specific set or meal). | Web / Mobile | P1 |

---

## 4. Technical Architecture & Data Integrations

### 4.1 System Stack Recommendations
* **Frontend:** React / Next.js (Coach Web Portal); React Native or Flutter (Cross-platform Mobile App for iOS/Android).
* **Backend:** Node.js / Python (FastAPI) with GraphQL or REST APIs for real-time mobile sync.
* **Database:** PostgreSQL for relational user and program data; Redis for caching active gym sessions and fast dashboard lookups.

### 4.2 Essential Integrations
1. **Nutrition Data API:** Integration with third-party providers such as **Nutritionix**, **FatSecret**, or **Spoonacular** to grant access to verified global food databases and barcode lookup.
2. **Media Storage:** Cloud Object Storage (AWS S3 or Cloudflare R2) with CDN delivery for low-latency playback of exercise library videos and athlete form-check clips.

---

## 5. Monetization & Growth Strategy

### 5.1 Pricing Tiers (Subscription B2B)
* **Starter Tier:** $29/month — Up to 10 active athletes.
* **Pro Tier:** $79/month — Up to 35 active athletes + custom coach branding.
* **Scale Tier:** $149/month — Up to 80 active athletes + priority video hosting & team accounts.
* *Note:* Athletes receive full mobile access free of charge under their coach's active tier.

### 5.2 Key Success Metrics (KPIs)
* **Coach Retention Rate:** Monthly Churn < 3%.
* **Athlete Onboarding Rate:** % of coach invites successfully accepted by athletes (> 85%).
* **Compliance Activity:** Average weekly workouts logged per active athlete.
