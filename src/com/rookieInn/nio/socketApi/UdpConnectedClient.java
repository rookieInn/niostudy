package com.rookieInn.nio.socketApi;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Created by gxy on 2016/6/29.
 */
public class UdpConnectedClient {

    public static void main(String[] args) {
        final int REMOTE_PORT = 5555;
        final String REMOVE_IP = "127.0.0.1";
        final int MAX_PACKET_SIZE = 65507;

        CharBuffer charBuffer = null;
        Charset charset = Charset.defaultCharset();
        CharsetDecoder decoder = charset.newDecoder();

        ByteBuffer textToEcho = ByteBuffer.wrap("Echo this: I'm a bit and ugly server!".getBytes());
        ByteBuffer echoedText = ByteBuffer.allocateDirect(MAX_PACKET_SIZE);

        try(DatagramChannel datagramChannel = DatagramChannel.open(StandardProtocolFamily.INET)) {
            //set some options
            datagramChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
            datagramChannel.setOption(StandardSocketOptions.SO_SNDBUF, 4 * 1024);

            //check if the channel was successfully opened
            if (datagramChannel.isOpen()) {
                datagramChannel.connect(new InetSocketAddress(REMOVE_IP, REMOTE_PORT));

                //check if the channel was successfully connected
                int sent = datagramChannel.write(textToEcho);
                System.out.println("I have successfully sent " + sent + " bytes to the Echo Server!");

                datagramChannel.read(echoedText);

                echoedText.flip();
                charBuffer = decoder.decode(echoedText);
                System.out.println(charBuffer.toString());
                echoedText.clear();
            } else {
                System.out.println("The channel cannot be connected!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
