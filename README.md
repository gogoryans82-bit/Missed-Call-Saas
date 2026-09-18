# LeadBack — Missed-Call Text-Back SaaS (Java / Spring Boot)

A multi-tenant SaaS that automatically texts missed callers back,
captures lead details, and alerts the business owner.

## Features
- Multi-tenant (many businesses, one install)
- Twilio voice + SMS webhooks
- Conversation state machine for lead capture
- Owner SMS alerts on qualified leads
- Spring Security (user + admin roles)
- Stripe subscription billing
- Optional Twilio subaccount auto-provisioning
- Docker + docker-compose
- H2 (dev) / PostgreSQL (prod)

## Quick start
```bash
cp .env.example .env
# fill in your Twilio and Stripe keys
docker compose up --build
