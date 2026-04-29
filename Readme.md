#run http command once httpie is installed
brew install httpie
http --stream :8080/stream

http :8080/monk/chat message=="Tell me about Manali"
http :8080/image-to-text

#Play Audio With AfPlay
afplay audio/generated/audio_1777292546780.mp3

# Get models chunk,embedding and vector search
http :8080/rag/models message=="Get me all anthropic models"

#Tools calling
http :8080/tools

#Test MCP from snapshot created
#https://modelcontextprotocol.io/docs/tools/inspector :
npx -y @modelcontextprotocol/inspector npx @modelcontextprotocol/server-filesystem /Users/deepakpanwar/Desktop
#Once above command runs use: Open inspector with token pre-filled:
http://localhost:6274/?MCP_PROXY_AUTH_TOKEN=03935c1759b5f4ef42de4e8e2eb18bf7057976aefe8216d288295f4999f4e6fe

Keep Transport Type: STDIO
Command: java
Arguments: -jar /Users/deepakpanwar/Documents/Java_AI_Samples/target/travelmonk-0.0.1-SNAPSHOT.jar
path to snapshot: eg: /Users/deepakpanwar/Documents/Java_AI_Samples/target/travelmonk-0.0.1-SNAPSHOT.jar

          

