-- Seed one user
INSERT INTO users (id, email, created_at) VALUES
    ('11111111-1111-1111-1111-111111111111', 'demo@sportsbetmini.dev', NOW());

-- Seed one event (tomorrow)
INSERT INTO events (id, sport, home_team, away_team, start_time) VALUES
    ('22222222-2222-2222-2222-222222222222', 'FOOTBALL', 'Team A', 'Team B', NOW() + INTERVAL '1 day');

-- Seed odds for that event
INSERT INTO odds (event_id, market, selection, price_decimal, updated_at) VALUES
                                                                              ('22222222-2222-2222-2222-222222222222','MATCH_WINNER','HOME',1.90, NOW()),
                                                                              ('22222222-2222-2222-2222-222222222222','MATCH_WINNER','DRAW',3.40, NOW()),
                                                                              ('22222222-2222-2222-2222-222222222222','MATCH_WINNER','AWAY',2.10, NOW());
