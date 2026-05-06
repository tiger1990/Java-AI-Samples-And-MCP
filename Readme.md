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

---

## Next Steps / Exploration

- [ ] **Beehiiv MCP Server** - Explore building an MCP server for the Beehiiv newsletter platform
  - Reference: https://github.com/danvega/beehiiv-mcp-server/tree/master
  - Pattern: Real-world MCP server exposing an external API (Beehiiv) as tools for AI agents
  - Key concepts: tool definitions, API integration, authentication handling in MCP context
