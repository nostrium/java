package online.nostrium.servers.telnet;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.io.IOException;
import java.net.InetSocketAddress;

public class TelnetServer {

    private static final int PORT = 23000;

    // Telnet IAC (Interpret As Command) byte
    private static final byte IAC = (byte) 255;
    private static final byte DO = (byte) 253;
    private static final byte WILL = (byte) 251;
    private static final byte SB = (byte) 250;    // Sub-negotiation
    private static final byte SE = (byte) 240;    // End of sub-negotiation
    private static final byte ECHO = (byte) 1;    // Echo option
    private static final byte LINEMODE = (byte) 34;  // Line mode
    private static final byte MODE = (byte) 1;
    private static final byte MODE_0 = (byte) 0;

    public static void main(String[] args) throws IOException {
        // Create a non-blocking server socket
        NioSocketAcceptor acceptor = new NioSocketAcceptor();

        // Set the handler that will handle all incoming messages
        acceptor.setHandler(new TelnetIoHandler());

        // Add a custom codec filter for encoding/decoding byte arrays
        acceptor.getFilterChain().addLast("codec",
            new ProtocolCodecFilter(new ByteArrayCodecFactory()));

        // Bind the server to the specified port
        acceptor.bind(new InetSocketAddress(PORT));
        System.out.println("Telnet server started on port " + PORT);
    }

    // Define the handler for processing incoming data
    private static class TelnetIoHandler extends IoHandlerAdapter {

        @Override
        public void sessionOpened(IoSession session) {
            // Negotiate Telnet options to enable character mode and echoing
            negotiateTelnetOptions(session);

            // Send a welcome message to the client
            session.write("Welcome to the Apache Mina Telnet server!\n");
            session.write("Type something and see it echoed back to you character by character:\n");
        }

        @Override
        public void messageReceived(IoSession session, Object message) {
            byte[] byteArray = (byte[]) message;
            for (byte b : byteArray) {
                if (b == IAC) {
                    // Handle the negotiation commands, don't print them as characters
                    handleTelnetCommand(byteArray, session);
                } else {
                    // Only print normal characters
                    char receivedChar = (char) b;
                    System.out.print(receivedChar);
                    session.write(String.valueOf(receivedChar));
                }
            }
        }

        private void handleTelnetCommand(byte[] data, IoSession session) {
            // Handle negotiation feedback from the client
            // For now, just log the response for debugging
            System.out.println("Received Telnet negotiation: " + new String(data));
        }

        @Override
        public void exceptionCaught(IoSession session, Throwable cause) {
            cause.printStackTrace();
            session.closeNow();
        }

        // Telnet Option Negotiation with the requested byte sequence
        private void negotiateTelnetOptions(IoSession session) {
            try {
                // Step 1: Send IAC DO LINEMODE (255, 253, 34)
                IoBuffer bufferDoLineMode = IoBuffer.allocate(3);
                bufferDoLineMode.put(IAC);
                bufferDoLineMode.put(DO);
                bufferDoLineMode.put(LINEMODE);
                bufferDoLineMode.flip();
                session.write(bufferDoLineMode);

                // Step 2: Send IAC SB LINEMODE MODE 0 IAC SE (255, 250, 34, 1, 0, 255, 240)
                IoBuffer bufferSbLineMode = IoBuffer.allocate(7);
                bufferSbLineMode.put(IAC);
                bufferSbLineMode.put(SB);
                bufferSbLineMode.put(LINEMODE);
                bufferSbLineMode.put(MODE);
                bufferSbLineMode.put(MODE_0);
                bufferSbLineMode.put(IAC);
                bufferSbLineMode.put(SE);
                bufferSbLineMode.flip();
                session.write(bufferSbLineMode);

                // Step 3: Send IAC WILL ECHO (255, 251, 1)
                IoBuffer bufferWillEcho = IoBuffer.allocate(3);
                bufferWillEcho.put(IAC);
                bufferWillEcho.put(WILL);
                bufferWillEcho.put(ECHO);
                bufferWillEcho.flip();
                session.write(bufferWillEcho);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ByteArrayDecoder: Decode incoming bytes
    private static class ByteArrayDecoder implements ProtocolDecoder {
        @Override
        public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
            byte[] byteArray = new byte[in.remaining()];
            in.get(byteArray);
            out.write(byteArray);  // Write the byte array to the output
        }

        @Override
        public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
            // No additional decoding needed after the message
        }

        @Override
        public void dispose(IoSession session) throws Exception {
            // No resources to dispose
        }
    }

    // Updated ByteArrayEncoder: Encode bytes and strings to send to client
    private static class ByteArrayEncoder implements ProtocolEncoder {
        @Override
        public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
            if (message instanceof byte[]) {
                // Handle byte[] messages (e.g., Telnet negotiation commands)
                byte[] byteArray = (byte[]) message;
                IoBuffer buffer = IoBuffer.allocate(byteArray.length, false);  // Dynamic allocation
                buffer.put(byteArray);
                buffer.flip();
                out.write(buffer);
            } else if (message instanceof String) {
                // Handle String messages (normal data)
                String str = (String) message;
                byte[] strBytes = str.getBytes("UTF-8");  // Get the byte size in UTF-8
                IoBuffer buffer = IoBuffer.allocate(strBytes.length, false);
                buffer.put(strBytes);
                buffer.flip();
                out.write(buffer);
            } else {
                throw new IllegalArgumentException("Unsupported message type: " + message.getClass());
            }
        }

        @Override
        public void dispose(IoSession session) throws Exception {
            // No resources to dispose
        }
    }

    // ByteArrayCodecFactory: Combine encoder and decoder
    private static class ByteArrayCodecFactory implements ProtocolCodecFactory {
        @Override
        public ProtocolEncoder getEncoder(IoSession session) throws Exception {
            return new ByteArrayEncoder();
        }

        @Override
        public ProtocolDecoder getDecoder(IoSession session) throws Exception {
            return new ByteArrayDecoder();
        }
    }
}
