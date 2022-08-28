# Project Structure
## Layers(DDD like):
### Application Layer

Contains system's use-cases.
Interact with domain services to get domain entities and apply domain logic to it.
Interact with infrastructure services to handle transactions and domain eventing
### Infrastructure Layer
Provide data storing capabilities, eventing system: domain, event based RPC
### Presentation Layer
Contain user-interaction classes: web-controllers, DTOs.
### Domain
Domain entities and services which encapsulate domain rules.
