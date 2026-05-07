# Java AI Samples - TravelMonk

A Spring Boot application exploring AI capabilities including RAG, MCP servers, tool calling, and streaming.

---

## Prerequisites

```bash
# Install HTTPie for API testing
brew install httpie
```

---

## Running the Application

```bash
# Build and run the MCP server
./mvnw clean package -DskipTests && java -jar target/*.jar
```

---

## API Endpoints

```bash
# Streaming chat
http --stream :8080/stream

# Chat with message
http :8080/monk/chat message=="Tell me about Manali"

# Image to text
http :8080/image-to-text

# RAG - vector search with embeddings
http :8080/rag/models message=="Get me all anthropic models"

# Tool calling
http :8080/tools
```

---

## Audio Playback

```bash
# Play generated audio on macOS
afplay audio/generated/audio_1777292546780.mp3
```

---

## MCP Server Setup

### Option 1: STDIO Transport (MCP Inspector)

Test with the MCP Inspector:

```bash
npx -y @modelcontextprotocol/inspector npx @modelcontextprotocol/server-filesystem /Users/deepakpanwar/Desktop
```

Open inspector (token pre-filled):
```
http://localhost:6274/?MCP_PROXY_AUTH_TOKEN=03935c1759b5f4ef42de4e8e2eb18bf7057976aefe8216d288295f4999f4e6fe
```

Inspector settings:
- **Transport Type:** STDIO
- **Command:** `java`
- **Arguments:** `-jar /Users/deepakpanwar/Documents/Java_AI_Samples/target/travelmonk-0.0.1-SNAPSHOT.jar`

Add to Claude Desktop (`Settings > Developer > Edit Config`):

```json
{
  "preferences": {
    "coworkWebSearchEnabled": true,
    "coworkScheduledTasksEnabled": false,
    "ccdScheduledTasksEnabled": true
  },
  "mcpServers": {
    "spring-io-sessions": {
      "command": "/Users/deepakpanwar/Library/Java/JavaVirtualMachines/azul-17.0.16/Contents/Home/bin/java",
      "args": [
        "-jar",
        "/Users/deepakpanwar/Documents/Java_AI_Samples/target/travelmonk-0.0.1-SNAPSHOT.jar"
      ]
    }
  }
}
```

---

### Option 2: Streamable HTTP Transport

Enable in `application.yaml`:

```yaml
mcp:
  server:
    enabled: true
    protocol: STREAMABLE
```

Add to `~/.claude.json`:

```json
{
  "projects": {
    "/Users/deepakpanwar": {
      "allowedTools": [],
      "mcpContextUris": [],
      "mcpServers": {
        "http-spring-io-server": {
          "type": "http",
          "url": "http://localhost:8085/mcp"
        }
      }
    }
  }
}
```

# Option to run Docker

## Option 1: Docker Desktop (Easiest)

- Download the latest Docker Desktop from:
  - https://www.docker.com/products/docker-desktop

---
## Option 2: Colima (Recommended for Intel Mac)

Lightweight, free, no GUI overhead, and works well on Intel.
qemu needs in Intel Macs.
Install dependencies:

```bash
brew install colima docker docker-compose   
brew install qemu                                                                                                                                                     
colima start                                                                                                                                                       
```
If you want to run it with specific resources suited for your 16GB RAM:
! colima start --cpu 4 --memory 8 --disk 60
This allocates 4 CPUs, 8GB RAM, 60GB disk — good balance for your machine. 

That's it — docker compose up -d will work immediately after.
What this compose.yaml does:
- Prometheus (port 9090) — scrapes metrics using ./docker/prometheus.yml config
- Grafana (port 3000) — dashboards UI, login: admin/admin, loads a pre-built AI metrics dashboard

Once started:
- Grafana: http://localhost:3000
- Prometheus: http://localhost:9090
---

## Next Steps / Exploration
--Reference: https://www.danvega.dev/newsletter/ai-for-java-developers-course
- [ ] **Beehiiv MCP Server** - Explore building an MCP server for the Beehiiv newsletter platform
  - Reference: https://github.com/danvega/beehiiv-mcp-server/tree/master
  - Pattern: Real-world MCP server exposing an external API (Beehiiv) as tools for AI agents
  - Key concepts: tool definitions, API integration, authentication handling in MCP context
  - Test with opensource models
  - https://ollama.com/
  - https://docs.openwebui.com/getting-started/quick-start
  - https://docs.docker.com/ai/model-runner/openwebui-integration/
