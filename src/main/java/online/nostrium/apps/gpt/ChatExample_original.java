/*
 * Example of chat
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.gpt;

import com.github.tjake.jlama.model.AbstractModel;
import com.github.tjake.jlama.model.ModelSupport;
import com.github.tjake.jlama.model.functions.Generator;
import com.github.tjake.jlama.safetensors.DType;
import com.github.tjake.jlama.safetensors.SafeTensorSupport;
import com.github.tjake.jlama.safetensors.prompt.PromptContext;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.folder.FolderUtils;

/**
 * @author Brito
 * @date: 2024-09-10
 * @location: Germany
 */
public class ChatExample_original {

    public static void sample() throws IOException {
        String model = "tjake/TinyLlama-1.1B-Chat-v1.0-Jlama-Q4";
        File workingDirectory = FolderUtils.getFolderGPT();
                //"./models";

        String prompt = "What is the best season to plant avocados?";

        // Downloads the model or just returns the local path if it's already downloaded
        File localModelPath = SafeTensorSupport
                .maybeDownloadModel(workingDirectory.getAbsolutePath(), model);

        // Loads the quantized model and specified use of quantized memory
        AbstractModel m = ModelSupport.loadModel(localModelPath, DType.F32, DType.I8);

        PromptContext ctx;
        // Checks if the model supports chat prompting and adds prompt in the expected format for this model
        if (m.promptSupport().isPresent()) {
            ctx = m.promptSupport()
                    .get()
                    .builder()
                    .addSystemMessage("You are a helpful chatbot who writes short responses.")
                    .addUserMessage(prompt)
                    .build();
        } else {
            ctx = PromptContext.of(prompt);
        }

        System.out.println("Prompt: " + ctx.getPrompt() + "\n");
        // Generates a response to the prompt and prints it
        // The api allows for streaming or non-streaming responses
        // The response is generated with a temperature of 0.7 and a max token length of 256
        Generator.Response r = m.generate(UUID.randomUUID(), 
                ctx, 0.0f, 256, (s, f) -> {});
        System.out.println(r.responseText);
    }

    public static void main(String[] args){
        try {
            sample();
        } catch (IOException ex) {
            Logger.getLogger(ChatExample_original.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
