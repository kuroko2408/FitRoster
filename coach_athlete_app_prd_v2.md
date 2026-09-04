# Product Requirement Document (PRD) v2.0: Coach-Athlete Fitness & Nutrition Platform

## 1. Executive Summary & Value Proposition

### 1.1 Overview
The platform is a **B2B2C online coaching management ecosystem** designed to streamline interactions between fitness coaches and their clients/athletes. Similar in structural concept to *Google Classroom*, the system provides a centralized dashboard for coaches to create, assign, and monitor training and nutrition programs while offering athletes a friction-free mobile application to log workouts, track daily macro/micronutrients, and receive feedback.

**What's new in v2.0:** The fitness app market is projected to grow from roughly $22B (2026) toward $57B by 2030, at a >25% CAGR, driven overwhelmingly by AI personalization, wearable integration, and gamified/holistic health monitoring. Coaches are increasingly expected to compete against — or absorb — AI-only coaching apps. This revision repositions the platform around an **"AI co-pilot for the human coach"** model: automation handles repetitive programming and monitoring so the coach can spend their time on the parts clients actually pay for (relationship, accountability, expertise), rather than trying to out-manual-effort an AI-native competitor.

### 1.2 Business & Distribution Model
* **Primary Target Customer:** Independent fitness coaches, personal trainers, online prep coaches, and boutique fitness studios.
* **Secondary End Users:** Athletes and client trainees managed by the coaches.
* **Distribution Strategy (B2B2C Virality):** Acquire the coach as the core paying subscriber. Each onboarded coach acts as a direct distribution channel, inviting 10 to 50+ athletes to download and use the app as part of their service package.
* **Competitive Framing:** Direct-to-consumer AI trainer apps (e.g., adaptive-programming apps, AI form-check apps) are eroding the low end of the market. The platform's defensibility should come from **augmenting** the coach with AI, not replacing them — the human relationship remains the retention driver; AI removes the busywork around it.

---

## 2. User Roles & Key Workflows

```
┌─────────────────────────────────────────────────────────────────┐
│                          COACH PORTAL                           │
│  • Program Design (Workouts & Macros) + AI Assist               │
│  • Roster Compliance Dashboard + Churn-Risk Alerts               │
│  • Video Form Reviews & AI-Assisted Feedback                    │
└────────────────────────────────┬────────────────────────────────┘
                                 │ Invites & Program Push
                                 ▼
┌─────────────────────────────────────────────────────────────────┐
│                           ATHLETE APP                           │
│  • Interactive Gym Checklist & Rest Timers                      │
│  • AI Photo/Barcode Food Logging + Macro Tracking                │
│  • Wearable-Synced Recovery, Sleep & Readiness                  │
│  • Progress Submissions & AI-Assisted Form Clips                │
└─────────────────────────────────────────────────────────────────┘
```

### 2.1 The Coach (Primary Persona)
* **Goal:** Maximize client retention and scale client volume without increasing administrative overhead.
* **Core Workflows:**
  * Build and maintain a reusable library of workout programs and exercise demonstrations, with AI-assisted first-draft generation.
  * Assign individual or group training schedules with target loads, set/rep ranges, and target RPE (Rate of Perceived Exertion).
  * Configure custom macro/micronutrient targets (distinguishing between Training Days, Rest Days, and Refeed Days), with AI-suggested starting targets based on athlete profile.
  * Review client compliance metrics, wearable-derived recovery data, and form analysis videos from a unified feed, prioritized by an AI-generated "needs attention" queue.

### 2.2 The Athlete (Secondary Persona)
* **Goal:** Execute assigned routines with zero ambiguity and track progress effortlessly during workout sessions.
* **Core Workflows:**
  * Open daily agenda, follow workout checklists, and record weight/reps with built-in rest timers and optional voice logging.
  * Log daily meals against target nutrition goals via barcode, search, or AI photo recognition.
  * Sync a wearable device to feed sleep, HRV, resting heart rate, and steps into the coach's dashboard automatically.
  * Submit weekly check-ins, progress photos, and form-check video clips, with instant AI pre-screening before the coach reviews.

### 2.3 The AI Layer (New — Cross-Cutting)
Rather than a separate persona, AI functions as an assistive layer available to both coach and athlete:
* **For coaches:** program-draft generation, auto-progression suggestions (load/rep adjustments based on logged performance), compliance-risk flagging, and message-drafting for check-ins.
* **For athletes:** conversational logging ("I had two eggs and toast"), real-time rep-tempo/form feedback via on-device pose estimation, and a lightweight chat-based check-in when they miss a session.
* **Guardrail:** All AI-generated programming or nutrition targets are surfaced to the coach as *suggestions requiring approval* before being pushed to an athlete — the coach stays the final authority, both for quality control and liability.

---

## 3. Detailed Feature Specifications

### 3.1 Module A: Workout & Training Management

| Feature | Description | Platform | Priority |
| :--- | :--- | :--- | :--- |
| **Workout Builder** | Drag-and-drop builder supporting sets, reps, target weight, RPE, tempo, and rest intervals. | Web / Tablet | MVP (P0) |
| **Template Library** | Save and clone exercise blocks, workout days, or multi-week phases (e.g., "4-Week Hypertrophy Push/Pull/Legs"). | Web / Mobile | MVP (P0) |
| **Interactive Gym Logger** | Checklist-style interface for athletes with auto-advancing rest timers and target history lookup. | Mobile | MVP (P0) |
| **AI Program Draft Assistant** | Generates a first-draft block/mesocycle from a coach's stated goal, equipment, and athlete history; coach edits and approves before publishing. | Web | P1 |
| **Auto-Progression Engine** | Suggests next-session load/rep/RPE adjustments per athlete based on logged performance trends; coach can accept, edit, or set to auto-apply within bounds. | Web / Mobile | P1 |
| **On-Device Form/Rep Analysis** | Pose-estimation-based rep counting and basic form-fault detection (e.g., squat depth, bar path) during logging, flagged for coach review rather than replacing it. | Mobile | P2 |
| **Offline Sync** | Store workout plans locally and queue submissions when gym cellular signal is weak. | Mobile | P1 |
| **Form Check Integration** | Attach up to 15-second video clips to specific set entries for coach review, with AI-generated timestamps highlighting the rep most worth watching. | Mobile / Web | P1 |
| **Voice Logging** | Hands-free set/rep/weight logging via short voice commands during a session. | Mobile | P2 |

### 3.2 Module B: Nutrition & Macro Tracking

| Feature | Description | Platform | Priority |
| :--- | :--- | :--- | :--- |
| **Macro Allocator** | Set daily targets for Protein, Carbohydrates, Fats, Fibre, and total Calories. | Web / Mobile | MVP (P0) |
| **Split Target Profiles** | Automated target switching between Training Days, Rest Days, and Refeed Days. | Web / Mobile | MVP (P0) |
| **Food Logging Engine** | Barcode scanning and verified database search for logging meals and custom items. | Mobile | MVP (P0) |
| **AI Photo Meal Recognition** | Snap a photo (or describe a meal in natural language) to get an estimated macro breakdown for logging, editable before saving. | Mobile | P1 |
| **Micronutrient Tracking** | Optional tracking for Fibre, Sodium, Water intake, and custom supplement checklists. | Mobile / Web | P1 |
| **Restaurant/Menu Matching** | Match logged meals against chain-restaurant nutrition databases for faster, more accurate estimates while eating out. | Mobile | P2 |

### 3.3 Module C: Coach Dashboard & Client Compliance

| Feature | Description | Platform | Priority |
| :--- | :--- | :--- | :--- |
| **Roster Compliance Score** | Aggregate score (0–100%) showing workout completion rate and nutrition target adherence over 7/30 days. | Web / Mobile | MVP (P0) |
| **Activity Feed** | Real-time notifications when athletes complete workouts, log meals, or request feedback. | Web / Mobile | MVP (P0) |
| **In-App Messaging** | Direct messaging with context tags (link feedback directly to a specific set or meal). | Web / Mobile | P1 |
| **Churn/Compliance Risk Alerts** | AI-flagged early-warning list of athletes trending toward disengagement (missed logs, declining adherence, negative check-in sentiment), so coaches intervene before cancellation. | Web / Mobile | P1 |
| **AI Check-In Drafts** | Suggests a personalized weekly check-in message per athlete based on their week's data, which the coach edits and sends. | Web | P2 |

### 3.4 Module D: Wearables, Recovery & Engagement (New)

| Feature | Description | Platform | Priority |
| :--- | :--- | :--- | :--- |
| **Wearable Integrations** | Sync with Apple Health, Google Fit/Health Connect, Garmin, Whoop, and Oura for steps, HRV, resting heart rate, sleep, and workout auto-detection. | Mobile | P1 |
| **Readiness/Recovery Score** | Blend wearable recovery data with logged training load to flag when a session should be adjusted; surfaced to both athlete and coach. | Mobile / Web | P2 |
| **Streaks & Gamification** | Logging streaks, milestone badges, and optional roster leaderboards (coach-configurable, opt-in for athletes). | Mobile | P2 |
| **Community Feed** | Opt-in group feed within a coach's roster for shared PRs, encouragement, and group challenges — reinforces retention without adding coach workload. | Mobile | P2 |

---

## 4. Technical Architecture & Data Integrations

### 4.1 System Stack Recommendations
* **Frontend:** React / Next.js (Coach Web Portal); React Native or Flutter (Cross-platform Mobile App for iOS/Android).
* **Backend:** Node.js / Python (FastAPI) with GraphQL or REST APIs for real-time mobile sync.
* **Database:** PostgreSQL for relational user and program data; Redis for caching active gym sessions and fast dashboard lookups; a vector store (e.g., pgvector, Pinecone) if AI program-search/RAG over a coach's exercise library is implemented.
* **AI/ML Layer:** LLM API (e.g., Claude) for program drafting, meal-description parsing, and check-in message generation; on-device or edge inference (e.g., MediaPipe/TensorFlow Lite) for pose estimation to keep video processing low-latency and to minimize sensitive video leaving the device where possible.

### 4.2 Essential Integrations
1. **Nutrition Data API:** Integration with third-party providers such as **Nutritionix**, **FatSecret**, or **Spoonacular** to grant access to verified global food databases and barcode lookup, plus an image-recognition-capable provider or in-house model for photo meal logging.
2. **Wearable/Health Data APIs:** Apple HealthKit, Google Health Connect, Garmin Connect, Whoop, and Oura APIs for recovery, sleep, and activity sync.
3. **Media Storage:** Cloud Object Storage (AWS S3 or Cloudflare R2) with CDN delivery for low-latency playback of exercise library videos and athlete form-check clips.
4. **AI Provider:** LLM API access (with usage metering, since AI features carry a marginal cost per call — see Section 5.3).

### 4.3 Data Privacy & Compliance (New)
Given the market's growing sensitivity to health-data privacy (2026 surveys show rising consumer wariness of wearable and AI health data use):
* Health and biometric data (HRV, sleep, weight, progress photos) should be encrypted at rest and in transit, with clear, athlete-facing consent screens per data category (separate opt-in for wearable sync, photo storage, and AI processing).
* Athletes should be able to export or delete their data on request; coaches should not retain full data access after a client relationship ends without explicit re-consent.
* Where applicable, design toward relevant regional health-data regulations (e.g., HIPAA-adjacent handling in the US even if not a covered entity, GDPR in the EU/UK) — this should be validated with legal counsel before launch.

---

## 5. Monetization & Growth Strategy

### 5.1 Pricing Tiers (Subscription B2B)
* **Starter Tier:** $29/month — Up to 10 active athletes.
* **Pro Tier:** $79/month — Up to 35 active athletes + custom coach branding.
* **Scale Tier:** $149/month — Up to 80 active athletes + priority video hosting & team accounts.
* *Note:* Athletes receive full mobile access free of charge under their coach's active tier.

### 5.2 New Growth Levers
* **White-Label / Branded App:** Higher tiers unlock a fully coach-branded mobile app experience (icon, splash, push-notification sender name) — a proven lever for boutique-studio retention and premium positioning.
* **Marketplace / Template Sales:** Allow coaches to publish and sell proven program templates to other coaches on the platform, creating a secondary revenue stream and content flywheel.
* **Affiliate/Partner Integrations:** Optional revenue share on supplement or wearable-device referrals surfaced contextually (e.g., recommending a wearable for readiness tracking), disclosed transparently to athletes.

### 5.3 AI Feature Cost Model (New)
AI features (program drafting, photo meal recognition, check-in drafting) carry real per-call inference costs, unlike the rest of the platform. Recommend:
* Bundling a fixed monthly AI-call allowance into Pro/Scale tiers, with metered overage or an add-on "AI Coach Pack."
* Monitoring cost-per-athlete for AI features closely during beta to validate that tier pricing still supports healthy margins as usage scales.

### 5.4 Key Success Metrics (KPIs)
* **Coach Retention Rate:** Monthly Churn < 3%.
* **Athlete Onboarding Rate:** % of coach invites successfully accepted by athletes (> 85%).
* **Compliance Activity:** Average weekly workouts logged per active athlete.
* **AI Adoption Rate (New):** % of coaches actively using AI-assisted drafting/auto-progression, and % of AI suggestions accepted vs. edited/rejected — a proxy for suggestion quality.
* **Wearable Connect Rate (New):** % of athletes who link a wearable device — correlated in market data with higher engagement and retention.

---

## 6. Suggested Roadmap Sequencing (New)

1. **Phase 1 (MVP):** Modules A–C at P0 as originally scoped — core workout builder, macro tracking, food logging, compliance dashboard. Ship without AI to validate core coach/athlete workflow first.
2. **Phase 2:** Wearable integrations (Module D) + AI photo meal recognition + churn-risk alerts — highest-leverage, lowest-risk AI additions (assistive, not generative-programming).
3. **Phase 3:** AI program draft assistant + auto-progression engine, with the coach-approval guardrail from day one.
4. **Phase 4:** On-device form/rep analysis, marketplace, white-label branding, community/gamification — differentiation and expansion once core retention loop is proven.
