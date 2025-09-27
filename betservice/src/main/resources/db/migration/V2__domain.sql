-- Users
CREATE TABLE IF NOT EXISTS users (
  id UUID PRIMARY KEY,
  email TEXT NOT NULL UNIQUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Events (sports fixtures)
CREATE TABLE IF NOT EXISTS events (
  id UUID PRIMARY KEY,
  sport TEXT NOT NULL,
  home_team TEXT NOT NULL,
  away_team TEXT NOT NULL,
  start_time TIMESTAMPTZ NOT NULL
);

-- Odds (current market prices per event/selection)
CREATE TABLE IF NOT EXISTS odds (
  event_id UUID NOT NULL REFERENCES events(id) ON DELETE CASCADE,
  market TEXT NOT NULL,          -- e.g. MATCH_WINNER
  selection TEXT NOT NULL,       -- e.g. HOME / AWAY / DRAW
  price_decimal NUMERIC(6,2) NOT NULL CHECK (price_decimal > 1.00),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  PRIMARY KEY (event_id, market, selection)
);

-- Bets
CREATE TABLE IF NOT EXISTS bets (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
  event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
  market TEXT NOT NULL,
  selection TEXT NOT NULL,
  stake NUMERIC(12,2) NOT NULL CHECK (stake > 0),
  price_at_bet NUMERIC(6,2) NOT NULL CHECK (price_at_bet > 1.00),
  status TEXT NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING','WON','LOST','VOID')),
  placed_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Helpful indexes
CREATE INDEX IF NOT EXISTS idx_bets_user ON bets(user_id);
CREATE INDEX IF NOT EXISTS idx_bets_event ON bets(event_id);
CREATE INDEX IF NOT EXISTS idx_odds_event ON odds(event_id);
