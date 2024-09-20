package online.nostrium.servers.chatbot;


import com.github.tjake.jlama.model.AbstractModel;
import com.github.tjake.jlama.model.ModelSupport;
import com.github.tjake.jlama.model.functions.Generator;
import com.github.tjake.jlama.safetensors.DType;
import com.github.tjake.jlama.safetensors.SafeTensorSupport;
import com.github.tjake.jlama.safetensors.prompt.PromptContext;

import java.io.File;
import java.util.UUID;
import online.nostrium.logs.Log;

import online.nostrium.main.Folder;
import online.nostrium.servers.Server;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.utils.screens.Screen;
import online.nostrium.utils.screens.ScreenLocalCLI;
import online.nostrium.utils.time;

/**
 * @author Brito
 * @date: 2024-09-11
 * @location: Germany
 */
public class ServerChatBot extends Server {
    
    private AbstractModel model;
    
    public ServerChatBot() {
        this.keepRunning = true;
    }

    @Override
    public String getId() {
        return "Server_ChatBot";
    }

    @Override
    public int getPort() {
        return -1; // Since this isn't an HTTP server, port is not used
    }

    @Override
    @SuppressWarnings("UseSpecificCatch")
    protected void boot() {
        this.isRunning = false;
        try {
            // Initialize the chatbot model once in the boot method
            String modelName = "tjake/TinyLlama-1.1B-Chat-v1.0-Jlama-Q4";
            File workingDirectory = Folder.getFolderGPT();

            // Downloads the model or returns the local path if it's already downloaded
            File localModelPath = SafeTensorSupport.maybeDownloadModel(workingDirectory.getAbsolutePath(), modelName);

            // Loads the quantized model and specifies the use of quantized memory
            model = ModelSupport.loadModel(localModelPath, DType.F32, DType.I8);
            this.isRunning = true;

        } catch (Exception e) {
            Log.write("CHATBOT", TerminalCode.DENIED, "Unable to start", "Missing JDK experimental flags");
            Log.write("CHATBOT", TerminalCode.INFO, "Please start nostrium with the following flags", "--add-modules=jdk.incubator.vector --enable-native-access=ALL-UNNAMED --enable-preview");
            keepRunning = false;
            this.isRunning = false;
            return;
        }

        // Main logic to run the chatbot
        while (keepRunning) {
            try {
                // Keeps the server running, awaiting calls to `chat()`
            } catch (Exception e) {
                Log.write("CHATBOT", TerminalCode.DENIED, "Error occurred while running the chatbot", e.getMessage());
                keepRunning = false;
            }
        }

        this.isRunning = false;
    }

    @Override
    public void stop() {
        keepRunning = false;
        String text = getId() + " stopped on port " + getPort();
        System.out.println(text);
        shutdown();
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    protected void shutdown() {
        System.out.println("ChatBotServer is shutting down.");
    }

    // Non-static chat method to send messages to the chatbot
    public void chat(Screen screen, String inputPrompt) throws Exception {

        if (model == null) {
            Log.write("CHATBOT", TerminalCode.DENIED, "Chatbot model not loaded.", "<UNKNOWN MODEL>");
            return;
        }

        // Create prompt context without memory
        PromptContext ctx;
        if (model.promptSupport().isPresent()) {
            ctx = model.promptSupport()
                    .get()
                    .builder()
                    .addSystemMessage("You are a helpful chatbot who writes short responses.")
                    .addUserMessage(inputPrompt)
                    .build();
        } else {
            ctx = PromptContext.of(inputPrompt);
        }

        // Streaming the generated response word by word
        screen.write("Bot: ");
        Generator.Response r = model.generate(UUID.randomUUID(), ctx, 0.7f, 256, (token, isFinal) -> {
            // Print each token as it is generated to the screen
            screen.write(token);
        });

        screen.writeln(""); // Move to the next line after response is fully generated
    }

    public static void main(String[] args) {
        
        ServerChatBot server = new ServerChatBot();
        server.start();
        
        // wait for server to boot up
        while(server.isRunning == false){
            time.waitMs(100);
        }
        
        Screen screen = new ScreenLocalCLI();
        try {
            // Example of calling the non-static chat method to send a message
            server.chat(screen, "Write a hello world in Java");
        } catch (Exception e) {
            Log.write("CHATBOT", TerminalCode.DENIED, "Error while chatting", e.getMessage());
        }

        //System.exit(0);
        // Optionally stop the server after some condition or time, e.g., on another thread
        // server.stop();
    }

    @Override
    public int getPortSecure() {
        return -1;
    }
}
