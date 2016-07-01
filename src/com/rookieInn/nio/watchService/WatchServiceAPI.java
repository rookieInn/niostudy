package com.rookieInn.nio.watchService;

/*
The Watch Service API Classes
The java.nio.file.WatchService interface is the starting point of this API.
It has multiple implementations for different file systems and operating systems.
You use this interface together with three classes to develop a system that has file system watch capability.
These classes are overviewed by the below bullets:
        a. Watchable object: An object is "watchable" iff it represents an instance of a class that implements the java.nio.file.Watchable interface.
           In our case, this is the most important class of NIO 2, the well-know Path class.
        b. Event types: This is the list of events we are interested in monitoring.
           Events trigger a notification only if they are specified in the register call.
           The standard supported events are represented by the java.nio.file.StandardWatchEventKinds class and include create, delete, and modify.
           This class implements the WatchEvent.Kind<T> interface.
        c. Even modifier. The qualifies how a Watchable is registered with a WatchService.
           As of the time of this writing, NIO.2 does not define any standard modifiers.
        d. Watcher. The watcher watchers watchable! In our examples, the watch is WatchService and it monitors the file system changes (the file system is a FileSystem instance).
           As you will see, the WatchService will be created through the FileSystem class.
           It will work away silently in the background watching the registered Path.
 */
/*
Implementing a Watch Service
Creating a WatchService
    WatchService watchService = FileSystems.getDefault().newWatchService();

Registering Objects with the Watch Service
Every object that should be watch must be explicitly registered with the watch service.
We can register any object that implements the Watchable interface.
For example, we will register directories that are instance of the Path class.
Beside the watched objects, the registration process requires identification of the events for which the service should watch and nofify.
The supported types of events are mapped under the StandardWatchEventKinds class as constants of type Kind<Path>:
        a. StandardWatchEventKinds.ENTRY_CREATE: A directory entry is created.
           An ENTRY_CREATE event is also triggered when a file is renamed or moved into this directory.
        b. StandardWatchEventKinds.ENTRY_DELETE: A directory entry is deleted.
           An ENTRY_DELETE event is also triggered when a file is renamed or moved out of this directory.
        c. StandardWatchEventKinds.ENTRY_MODIFY: A directory entry is modified.
           Which events constitute a modification is somewhat platform-specific, but actually modifying the contents of a file always triggers a modify event.
           On some platforms, changing attributes of files can also trigger this event.
        d. StandardWatchEventKinds.OVERFLOW: Indicates that events might have been lost discarded.
           You do not have to register for the OVERFLOW event to receive it.
 Since the Path class implements the Watchable interface, it provides the Watchable.register() methods.
 There are two such methods dedicated for registering objects with the watch service.
 One of them receives two arguments representing the watch service to which this object is to be registered and arguments also.
 and a third argument that specifies modifiers that qualify how the directory is registered.
 At the time of this writing, NIO.2 does not provide any standard modifiers.

 The following code snippet registers the Path E:/tmp with the watch service(the monitored the events will be create, delete, and modify):

        import static java.nio.file.StandardWatchEventKinds.*;

        final Path path = Path.get("E:/tmp");
        WatchService watchService = FileSystem.getDefault().newWatchService();

        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEvent.ENTRY_DELETE);
        watchService.close();
 You receive a WatchKey instance for each directory that you register;
 this is a token representing the registration of a watchable object with a WatchService.
 It is you choice whether or not to hang onto this reference, because the WatchService returns the relevant WatchKey to you when an event is triggered.
 */
/*
Waiting for the Incoming Events
Waiting for the incoming events requires an infinite loop.
When an event occurs, the watch service is responsible for signaling the corresponding watch key and placing it into the watcher's queue,
from where we can retrieve it --we say that the watch keys was queued.
Therefore, our infinite loop may be of the following type:
        while(true) {
            //retrieve and process the incoming events
        }
Or it may be of the following type:
        for(;;) {
            //retrieve and process the incoming event
        }
 */

import java.io.IOException;
import java.nio.file.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;

/*
Getting a Watch Key
Retrieving a queued key can be accomplished by calling one of the following three methods of the WatchService class.
All three methods retrieve the next key and remove it from the queue.
They differ in how they respond if no key is available, as described here:
        a. poll(): If no key is available, it returns immediately a null value.
        b. poo(long, TimeUnit): If no key is available, it waits the specified time and tries again.
           If still no key is available, then it returns null.
           The time period is indicated as a long number, while the TimeUnit argument determines whether the specified time is minutes, seconds, milliseconds, or some other unit of time.
        c. take(): If no key is available, it waits until a key is queued or the infinite loop is stopped for any of several different reasons.
The following three code snippets show you each of these methods called inside the infinite loop:
        //poll method, without arguments
        while(true) {
            //retrieve and remove the next watch key
            final WatchKey key = watchService.poll();
            //the thread flow gets here immediately with an available key or a null value
        }

        //poll method, with arguments
        while(true) {
            //retrieve and remove the next watch key
            final WatchKey key = watchService.poll(10, TimeUnit.SECONDS);
            //the thread flow gets here immediately if a key is available, or after 10 seconds
            //with an available key or null value
        }

        //take method
        while(true) {
            //retrieve and remove the next watch key
            final WatchKey key = watchService.take();
            //the thread flow gets here immediately if a key is available, or it will wait until a
            //key is available, or the loop breaks
        }
Keep in mind that a key always has a state, which can be either ready, signaled, or invalid:
        a. Ready: When it is first created, a key is in the ready state, which means that it is ready to accept events.
        b. Signaled: When a key is in the signaled state, it means that at least one event has occurred and the key was queued,
           so it available to be retrieved by poll() or take() methods. (It is analogous to fishing: the key is the float,
           and the events are the fish. When you have a fish on the hook, the float(key) signals you to pull the line out of the water.)
           Once signaled, the key remains in the state until its reset() method is invoked to return the key to the ready state.
           If other event occur while the key is signaled, they are queued without requeuing the key itself(this never happens when fishing).
        c. Invalid: When a key is in the invalid state, it means that it is no longer active.A key remains valid until either it is cancelled by explicitly calling the cancel() method,
           the directory becomes inaccessible, or the watch service is closed. You can test whether a key is valid by calling the WatchKey.isValid() method, which will return a corresponding boolean value.
 */
/*
Retrieving Pending Events for a Key
When the key is signaled, we have one or more pending events waiting for us to taken action.
We can retrieve and remove all pending events for a specific watch key by calling the WatchKey.pollEvents() method.
It gets no arguments and returns a List containing the retrieved pending events.
We can iterate this List to extract and process each pending event individually.
The List type is WatchService:
        public List<WatchEvent<?>> pollEvents()
The following code snippet iterates the pending events for our key:
        while(true) {
            //retrieve and remove the next watch key
            final WatchKey key = watchService.take();

            //get list of pending events for the watch key
            for(WatchEvent<?> watchEvent : key.pollEvents()) {
                ...
            }
            ...
        }
The following code snippet will list the type of each event provided by the pollEvents() method:
        //get list of pending events for the watch key
        for(WatchEvent<?> watchEvent : key.pollEvents()) {
            //get the kind of event (create, modify, delete)
            final kind<?> kind = watchEvent.kind();

            //handle OVERFLOW event
            if (kind == StandardWatchEventKinds.OVERFLOW) {
                continue;
            }
            System.out.println(kind);
        }
Besides the event type, we can also get the number of times that the event has been observed(repeated events).
This is possible if we call the WatchEvent.count() method, which return an int:
        System.out.println(watchEvent.count());
 */
/*
Retrieving the File Name Associated with an Event
When a delete, create or modify event occurs on a file, we can find out its names by getting the event context
(the file name is stored as the context of the event).
This task can be accomplished by calling the WatchEvent.context() method:
        final WatchEvent<Path> watchEventPath = (WatchEvent<Path>) watchEvent;
        final Path filename = watchEventPath.context();

        System.out.println(filename);
 */
/*
Putting the Key Back in Ready State
Once signaled, the key remains in this state until its reset() method is invoked to return the key to the ready state.
In then resumes waiting for events. The reset() method returns true if the watch key is valid and has been reset, and return false if the watch key could not be reset because it is no longer valid.
In some cases, the infinite loop should be broken if the key is no longer valid;
for example, if we have a single key, there is no reason to stay in the infinite loop.
Following is the code that is used to break the loop if the key in no longer valid:
        while(true) {
            //reset the key
            boolean valid = key.reset();

            //exit loop if the key is not valid (if the directory was delete, for example)
            if (!valid) {
                break;
            }
        }
 */
/*
Closing the Watch Service
The watch service exits either when the thread exits or when the service is closed.
It should be closed by explicitly calling the WatchService.close()
 */
public class WatchServiceAPI {

    public void watchRNDir(Path path) throws IOException, InterruptedException {
        //创建一个 watch service
        WatchService watchService = FileSystems.getDefault().newWatchService();
        //给path注册watchservice
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

        //开启一个永久循环
        while(true) {
            //获取和删除下一个 watch key
            final WatchKey key = watchService.take();
            //获取 事件列表
            for (WatchEvent<?> watchEvent : key.pollEvents()) {
                 //获取事件类型(create, modify, delete)
                final WatchEvent.Kind<?> kind = watchEvent.kind();
                //处理 OVERFLOW 事件
                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }

                //获取 事件的 文件名
                final WatchEvent<Path> watchEventPath = (WatchEvent<Path>) watchEvent;
                final Path filename = watchEventPath.context();

                //输出文件名
                System.out.println(kind + " -> " + filename);
            }

            //重置 key
            boolean valid = key.reset();

            //当key无效时退出循环 (例如 文件夹被删除)
            if (!valid) {
                break;
            }
        }
    }


    public static void main(String[] args) throws NoSuchAlgorithmException {
        final Path path = Paths.get("E:/tmp");
        WatchServiceAPI watch = new WatchServiceAPI();
        try {
            watch.watchRNDir(path);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
