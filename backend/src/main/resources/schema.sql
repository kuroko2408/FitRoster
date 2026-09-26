CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    role VARCHAR(50) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE workouts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    coach_id UUID REFERENCES users(id),
    athlete_id UUID REFERENCES users(id),
    title VARCHAR(255) NOT NULL,
    scheduled_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sets (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    workout_id UUID REFERENCES workouts(id) ON DELETE CASCADE,
    exercise_name VARCHAR(255) NOT NULL,
    reps INT NOT NULL,
    target_weight DECIMAL(5,2),
    target_rpe INT,
    display_order INT NOT NULL,
    actual_reps INT,
    actual_weight DECIMAL(5,2),
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE macro_targets (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    coach_id UUID NOT NULL REFERENCES users(id),
    athlete_id UUID NOT NULL UNIQUE REFERENCES users(id),
    protein_target DECIMAL(8,2) NOT NULL,
    carb_target DECIMAL(8,2) NOT NULL,
    fat_target DECIMAL(8,2) NOT NULL,
    calorie_target DECIMAL(8,2) NOT NULL
);

CREATE TABLE meal_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    athlete_id UUID NOT NULL REFERENCES users(id),
    food_name VARCHAR(255) NOT NULL,
    protein DECIMAL(8,2) NOT NULL,
    carbs DECIMAL(8,2) NOT NULL,
    fats DECIMAL(8,2) NOT NULL,
    calories DECIMAL(8,2) NOT NULL,
    logged_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (id, name, email) values ('00000000-0000-0000-0000-000000000001','Test Coach','coach@test.com')
