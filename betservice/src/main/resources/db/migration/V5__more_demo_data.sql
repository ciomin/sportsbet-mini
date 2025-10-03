-- More events in the near future
INSERT INTO events (id, sport, home_team, away_team, start_time) VALUES
                                                                     ('33333333-3333-3333-3333-333333333333', 'FOOTBALL', 'Team C', 'Team D', NOW() + INTERVAL '2 days'),
                                                                     ('44444444-4444-4444-4444-444444444444', 'FOOTBALL', 'Team E', 'Team F', NOW() + INTERVAL '3 days')
    ON CONFLICT (id) DO NOTHING;

-- Odds for the above events
INSERT INTO odds (event_id, market, selection, price_decimal, updated_at) VALUES
                                                                              ('33333333-3333-3333-3333-333333333333','MATCH_WINNER','HOME',1.85, NOW()),
                                                                              ('33333333-3333-3333-3333-333333333333','MATCH_WINNER','DRAW',3.50, NOW()),
                                                                              ('33333333-3333-3333-3333-333333333333','MATCH_WINNER','AWAY',2.20, NOW()),
                                                                              ('44444444-4444-4444-4444-444444444444','MATCH_WINNER','HOME',2.05, NOW()),
                                                                              ('44444444-4444-4444-4444-444444444444','MATCH_WINNER','DRAW',3.30, NOW()),
                                                                              ('44444444-4444-4444-4444-444444444444','MATCH_WINNER','AWAY',1.95, NOW())
    ON CONFLICT DO NOTHING;