package online.nostrium.apps.gpt;

import com.github.tjake.jlama.model.AbstractModel;
import com.github.tjake.jlama.model.ModelSupport;
import com.github.tjake.jlama.model.functions.Generator;
import com.github.tjake.jlama.safetensors.DType;
import com.github.tjake.jlama.safetensors.SafeTensorSupport;
import com.github.tjake.jlama.safetensors.prompt.PromptContext;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import online.nostrium.main.Folder;

public class ChatExample {

    // Method to interact with the chatbot (no memory) with streaming output
    public static void chat() throws IOException {
        String model = "tjake/TinyLlama-1.1B-Chat-v1.0-Jlama-Q4";
        File workingDirectory = Folder.getFolderGPT();
        
        // Downloads the model or just returns the local path if it's already downloaded
        File localModelPath = SafeTensorSupport
                .maybeDownloadModel(workingDirectory.getAbsolutePath(), model);

        // Loads the quantized model and specifies the use of quantized memory
        AbstractModel m = ModelSupport.loadModel(localModelPath, DType.F32, DType.I8);

        Scanner scanner = new Scanner(System.in);
        String inputPrompt;

        System.out.println("Start chatting with the chatbot (type 'exit' to stop):");

        while (true) {
            // Read user input
            System.out.print("You: ");
            inputPrompt = scanner.nextLine();

            if ("exit".equalsIgnoreCase(inputPrompt)) {
                break;
            }

            // Create prompt context without memory
            PromptContext ctx;
            if (m.promptSupport().isPresent()) {
                ctx = m.promptSupport()
                        .get()
                        .builder()
                        .addSystemMessage("You are a helpful chatbot who writes short responses."
                                + " If the question is an order, please a line below with "
                                + "the expection intention on the following format:"
                                + "<verb> <subject> <value when applicable>")
                        .addUserMessage(inputPrompt)
                        .build();
            } else {
                ctx = PromptContext.of(inputPrompt);
            }

            // Streaming the generated response word by word
            System.out.print("Bot: ");
            Generator.Response r = m.generate(UUID.randomUUID(), ctx, 0.7f, 256, (token, isFinal) -> {
                // Print each token as it is generated
                System.out.print(token);
                System.out.flush(); // Ensure immediate display on screen
            });

            System.out.println(); // Move to the next line after response is fully generated
        }

        scanner.close();
    }

    public static void main(String[] args) {
        try {
            chat();
        } catch (IOException ex) {
            Logger.getLogger(ChatExample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
