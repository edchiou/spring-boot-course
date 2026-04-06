### What's this?
Simple Spring Boot project following https://www.udemy.com/course/master-microservices-with-spring-docker-kubernetes

Being at Amazon for a long time means lack of exposure to external tools and frameworks, so I'm taking the time to
refresh my knowledge of tools I've used before and learn new ones.

### Learnings

- Claude Code with Sonnet is capable of generating boilerplate efficiently and accurately.
- So far not a whole lot is different between Spring and Jersey besides naming conventions used for annotations.
- @ControllerAdvice is essentially the same as JAX-RS/Jersey's ExceptionMapper interface.
- There's a lot of auto-wiring in Spring compared to what I'm normally familiar with - manual Guice DI

#### Thread Safety
- Statelessness must hold across the entire call chain — controllers, services, and repositories are all singletons, so injected dependencies don't count as mutable state only if they are themselves stateless
- `ThreadLocal` belongs at the filter/interceptor layer, not in services or controllers — always call `remove()` in `finally` to prevent stale data leaking across thread pool reuse
- Shared mutable state belongs in a dedicated `@Component` (e.g. `ConcurrentHashMap`) — `@Component` ensures a single shared instance; without it each injected copy would have its own isolated map
- A `UNIQUE` constraint on the database is the correct fix for race conditions like duplicate registration — not `synchronized` blocks, which don't protect across multiple app instances

#### Date and Time
- `ZonedDateTime` is necessary over `Instant` when the timezone is part of the business meaning — e.g. "9am New York time" stored as `Instant` will shift by an hour after DST, but `ZonedDateTime` with `America/New_York` preserves the wall clock time
- MySQL `TIMESTAMP` silently converts values on read/write using the session timezone — the same row can return different values depending on the connection. `DATETIME` with application-enforced UTC is more predictable

#### JPA Auditing
- `AuditingEntityListener` is a thin JPA lifecycle bridge — `@PrePersist` and `@PreUpdate` delegate to `AuditingHandler` which does the actual field population via reflection; the listener itself contains no field-setting logic
- `AuditorAware<T>` is the single extension point for resolving "who is performing this operation" — only one can be designated per application, but it can route internally (e.g. `SecurityContextHolder` for authenticated users, fallback for system processes)

#### Spring Bean Model
- Constructor injection is preferred over field injection — dependencies are explicit, cannot be null at construction time, fields can be `final`, and tests can pass mocks directly without a Spring context