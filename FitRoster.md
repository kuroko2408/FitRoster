# Features Of FitRoster

1. Coach and athlete connect together. 
2.  Coach sets and workout the exercise the reps sets everything and athlete sees them does and logs them the logging system is like strava the heart rate calories burned etc  and the visual photo of what was worked is also shown .
3.  The coach can set a nutrition goal like the athlete has to complete this much protein ,carbs,fibre,fats etc in the day and athlete can add the same macro after his meals so coach can see his progress .
4. When the athlete doesn't know the exercise the coach can tag a video from the collection in the app or upload his own video shown how the exercise is done and athlete can watch that and follow it .
5. Athlete can add runs he has logged on like strava or Adidas running club etc and coach can see that and plan recovery ,exercise etc according to that .
# The app 
1.  The app has two option; log in as a coach or log in as athlete like google classroom .
2.  The app in divided into three section the workout ,nutrition and the progress,
	1. In coach side workout has option like add a exercise,add sets ,reps etc and make a preset use an old preset use app made preset already present .In athlete side we can see the workout or the exercise we have to do the amount of sets and reps and can log the workout in set by set when done the set .
	2. In coach side we can see being able to add nutrition add the daily macro goals .In athlete side we can log the macro and meal name or number .
	3. in progress both see the same page when the can see the workout completion and macro progress bar or pie chart .
3. Basic instruction like keep the app not full screen in the sense that the black bar in the top of battery and time the app should start below that .
4. The app should have a home button where there are settings permissions athlete all the athlete he is connect with and option to add new athletes .In athlete there should be option to send request to coach or accept request from coach cancel the coach.
5. Don't use purple colour or any close resemblance.
6. Add progress graph in the sense that whe nthe athlete logs his exercise he will mention if he could do the weight and reps and sets mention and than track his progress how he climbs in the weight.
7. Add a option to track weight and also progress graph of weight according to the weight goal of the athlete .

# Business Model
The model is that we sell to coaches .We sell them the subscription per athlete 1 st is free for a month  like a demo and than needed subscription for amount of users .The athlete doesn't have to pay he pays the coach or the gym that don't matter to us we take money from coaches per athlete .

# Marketing Strategy
Start with going to local gyms and asking them to use and beta test also by letting their coaches use the app .the sell to those coaches later partner with gyms etc further .Then add collabs with coach plus fitness influencers  .gym also like badminton gym etc .

# Proposed Tech stack

this is a proposed tech stack only not the final implementation

# Frontend - React Native

Reasons - Can do for both iOS and Android with typescript.  
Good for existing web development experience while sharing the application logic.

## State Management

**Zustland**  
Light weight  
Handles complex state required for active workout sessions (timers , current set, rep counts) seamlessly.

## UI-Library "UI-Library")UI Library

**NativeWind**  
It is kind of an tailwind for reactnative

## Why this Set of Choices?

## ReactNative

- Suitable for both android and iOS
- Both Trainers and Trainee have exact same experience .
- Native Bridging for AI
- low-level hardware access
- High performance frame processors
- We can run only the MediaPipe BlazePose directly on the device GPU and pass only the processed skeletal coordinates back too the UI thread from preventing the app from freezing.
- Rich Ecosystem (Mature libraries)

## Zustland

- Managing nested data
- High-frequency Updates
- Transient updates without making entire react tree to re-render , keeping data logic clean and easy to update.
- Seamless Profile Switching

## NativeWind

- Rapid UI protoytping
- Tailwind CSS uitlities into ReactNative
- Speeding up NativeWind
- Good to data-dense screens
- Design System Consistency
- single tailwind.config.js file for unified look