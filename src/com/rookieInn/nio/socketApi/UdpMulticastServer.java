package com.rookieInn.nio.socketApi;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Date;

/**
 *  UDP Multicast Server
 *
 *  blocking and unblocking datagrams
 *  Sometimes joining multicast groups can bring to you undesired datagrams (the reasons are not relevant here).
 *  You can block receiving a datagram from a sender by calling the MembershipKey.block() method and passing to the InetAddress of that sender.
 *  In assition, you can unblock the same sender, and start receiving datagrams from it again, by calling the MembershipKey.unblock() method and passing it the same InetAddress.
 *  Usually, you'll be in one of the following two scenarios:
 *      you have a list of senders' addresses that you'd like to join.
 *      Supposing that the addresses are stored in a List, you can loop it and join each address separately, as shown here;
 *
 *          List<InetAddress> like = ...;
 *          DatagramChannel datagramChannel = ...;
 *
 *          if(!like.isEmpty()) {
 *              for(InetAddress source : like) {
 *                  datagramChannel.join(group, network_interface, source);
 *              }
 *          }
 *      You have a list of senders' addresses that you don't want to join.
 *      Supposing that the addressed are stored in a List, then you can loop it and block each address separately, as shown here:
 *          List<InetAddress> dislike = ...;
 *          DatagramChannel datagramChannel = ...;
 *
 *          MembershipKey key = datagramChannel.join(group, network_interface);
 *
 *          if(!dislike.isEmpty()) {
 *              for(InetAddress source : dislike) {
 *                  key.block(source);
 *              }
 *          }
 *
 * Created by gxy on 2016/6/29.
 */
public class UdpMulticastServer {

    public static void main(String[] args) {
        final int DEFAULT_PORT = 5555;
        final String GROUP = "225.4.5.6";
        ByteBuffer datetime;

        //create a new channel
        try(DatagramChannel datagramChannel = DatagramChannel.open(StandardProtocolFamily.INET)) {
            //check if the channel was successfully created
            if (datagramChannel.isOpen()) {
                //get the network interface used for multicast
                NetworkInterface networkInterface = NetworkInterface.getByName("eth3");

                //set some options
                datagramChannel.setOption(StandardSocketOptions.IP_MULTICAST_IF, networkInterface);
                datagramChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

                //bind the channel to the local address
                datagramChannel.bind(new InetSocketAddress(DEFAULT_PORT));
                System.out.println("Date-time server is ready ... shortly I'll start sending ...");

                //transmitting datagrams
                while (true) {
                    //sleep for 10 seconds
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Sending data ...");

                    datetime = ByteBuffer.wrap(new Date().toString().getBytes());
                    datagramChannel.send(datetime, new InetSocketAddress(InetAddress.getByName(GROUP), DEFAULT_PORT));
                    datetime.flip();
                }
            } else {
                System.out.println("The channel cannot be opened!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
