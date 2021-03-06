package com.example.katharina.hackatum_ui.serverinterface;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.example.katharina.hackatum_ui.CustomApplication;
import com.example.katharina.hackatum_ui.Enter;
import com.example.katharina.hackatum_ui.Question;
import com.example.katharina.hackatum_ui.Result;
import com.example.katharina.hackatum_ui.Start;
import com.example.katharina.hackatum_ui.model.CountdownPacket;
import com.example.katharina.hackatum_ui.model.Packet;
import com.example.katharina.hackatum_ui.model.QuestionPacket;
import com.example.katharina.hackatum_ui.model.SummaryPacket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Queue;

//FIXME kill client on exit
//Feature
public class QuizClient extends Service {
    static boolean initThreadRunning = false;
    Thread readThread = null;
    Thread writeThread = null;
    String lastCmd = "";

    @Override
    public void onCreate() {
        super.onCreate();

        new InitThread().start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }


    public class InitThread extends Thread {

        @Override
        public void run() {
            //super.run();
            //Singleton
            if( writeThread!=null) {
                writeThread.interrupt();
            }
            if( readThread!=null) {
                readThread.interrupt();
            }

            if (QuizClient.initThreadRunning==true) {return;}
            else {QuizClient.initThreadRunning = true;}

            //Wait for socket connection
            Socket socket = null;
            BufferedReader input = null;
            PrintWriter output = null;
            while(true) {
                // establish a connection
                try {
                    socket = new Socket("hackquiz2017-server.ddns.net", 4444);

                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    // sends output to the socket
                    output = new PrintWriter(new DataOutputStream(socket.getOutputStream()));
                    break;
                } catch (UnknownHostException u) {
                    System.out.println("Init debug: "+u);
                } catch (IOException i) {
                    System.out.println("Init debug: "+i);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Connected");


            //=> Socket established, start read and write thread
            writeThread = new WriteThread(socket, input, output);
            writeThread.start();
            readThread = new ReadThread(socket, input, output);
            readThread.start();
            QuizClient.initThreadRunning=false;
            return; //Quit thread
        }

    }


    public class WriteThread extends Thread {
        boolean running = true;
        Socket socket = null;
        BufferedReader input = null;
        PrintWriter output = null;

        public WriteThread(Socket socket, BufferedReader input, PrintWriter output) {
            this.socket = socket;
            this.input = input;
            this.output = output;
        }


        @Override
        public void run() {
            Queue<Packet> toBeSendQueue = ((CustomApplication) getApplication()).getMessageQueue();
            super.run();

            while (running) {

                try {
                    //Write to socket:
                    if (!toBeSendQueue.isEmpty()) {
                        Packet toBeSent = toBeSendQueue.remove();
                        System.out.println("Found packet to be sent:" + toBeSent);
                        String json = Serializer.serializeObject(toBeSent);
                        output.write(json + "\n");
                        output.flush();
                        System.out.println("Sent Json packet: " + json);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    running = false;
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    running = false;
                }
            }

            new InitThread().start();

        }
    }

        public class ReadThread extends Thread {
            Context context;
            Socket socket = null;
            BufferedReader input = null;
            PrintWriter output = null;

            public ReadThread( Socket socket, BufferedReader input, PrintWriter output) {
                this.socket = socket;
                this.input = input;
                this.output = output;
            }

            @Override
            public void run() {
                //super.run();

                boolean running = true;
                while (running) {

                    String line = null;
                    try {
                        //Read from socket
                        System.out.println("Reading buffer");
                        line = input.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                        running = false;
                    }

                    //Receiving code -> transitions in the activities
                    if (line != null && !line.isEmpty()) {
                        System.out.println("Packet received:" + line);
                        lastCmd = line; //???

                        switch (Serializer.getTokenFromPacket(line)) {
                            case CountdownPacket.token:
                                Intent dialogIntent1 = new Intent(QuizClient.this, Start.class);
                                dialogIntent1.putExtra("JSON", line);
                                dialogIntent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(dialogIntent1);

//                                ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//                                List<ActivityManager.RunningTaskInfo> taskInfo = activityManager .getRunningTasks(1);
//                                ComponentName componentInfo = taskInfo.get(0).baseActivity;

                                //TODO IMplement backstack cleaning
                                break;

                            case QuestionPacket.token:
                                Intent dialogIntent2 = new Intent(QuizClient.this, Question.class);
                                dialogIntent2.putExtra("JSON", line);
                                dialogIntent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(dialogIntent2);
                                //TODO IMplement backstack cleaning
                                break;

                            case SummaryPacket.token:
                                Intent dialogIntent3 = new Intent(QuizClient.this, Result.class);
                                dialogIntent3.putExtra("JSON", line);
                                dialogIntent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(dialogIntent3);
                                //TODO IMplement backstack cleaning
                                break;

                        }
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        running = false;
                    }

                }

                new InitThread().start();

            }
        }



}













//package com.example.katharina.hackatum_ui.serverinterface;
//
//import android.app.AlarmManager;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.os.IBinder;
//import android.os.Looper;
//import android.os.Message;
//import android.util.Config;
//import android.util.Log;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//
///**
// * Created by bloeh on 18.11.2017.
// */
//
//public class QuizClient extends Service {
//
//    /**
//     * The source of the log message.
//     */
//    private static final String TAG = "KeepAliveService";
//
//    private static final long INTERVAL_KEEP_ALIVE = 1000 * 60 * 4;
//
//    private static final long INTERVAL_INITIAL_RETRY = 1000 * 10;
//
//    private static final long INTERVAL_MAXIMUM_RETRY = 1000 * 60 * 2;
//
//    private ConnectivityManager mConnMan;
//
//    protected NotificationManager mNotifMan;
//
//    protected AlarmManager mAlarmManager;
//
//    private boolean mStarted;
//
//    private boolean mLoggedIn;
//
//    protected static ConnectionThread mConnection;
//
//    protected static SharedPreferences mPrefs;
//
//    private final int maxSize = 212000;
//
//    private Handler mHandler;
//
//    private volatile Looper mServiceLooper;
//
//    private volatile ServiceHandler mServiceHandler;
//
//    private final class ServiceHandler extends Handler {
//        public ServiceHandler(final Looper looper) {
//            super(looper);
//        }
//
//        @Override
//        public void handleMessage(final Message msg) {
//            onHandleIntent((Intent) msg.obj);
//        }
//    }
//
//    public static void actionStart(final Context context) {
//        context.startService(SystemHelper.createExplicitFromImplicitIntent(context, new Intent(IntentActions.KEEP_ALIVE_SERVICE_START)));
//    }
//
//    public static void actionStop(final Context context) {
//        context.startService(SystemHelper.createExplicitFromImplicitIntent(context, new Intent(IntentActions.KEEP_ALIVE_SERVICE_STOP)));
//    }
//
//    public static void actionPing(final Context context) {
//        context.startService(SystemHelper.createExplicitFromImplicitIntent(context, new Intent(IntentActions.KEEP_ALIVE_SERVICE_PING_SERVER)));
//    }
//
//    @Override
//    public void onCreate() {
//        Log.i(TAG, "onCreate called.");
//        super.onCreate();
//
//        mPrefs = getSharedPreferences("KeepAliveService", MODE_PRIVATE);
//
//        mConnMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//
//        mNotifMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//        mHandler = new Handler();
//
//        final HandlerThread thread = new HandlerThread("IntentService[KeepAliveService]");
//        thread.start();
//
//        mServiceLooper = thread.getLooper();
//        mServiceHandler = new ServiceHandler(mServiceLooper);
//
//        // If our process was reaped by the system for any reason we need to
//        // restore our state with merely a
//        // call to onCreate.
//        // We record the last "started" value and restore it here if necessary.
//        handleCrashedService();
//    }
//
//    @Override
//    public void onDestroy() {
//        Log.i(TAG, "Service destroyed (started=" + mStarted + ")");
//        if (mStarted) {
//            stop();
//        }
//        mServiceLooper.quit();
//    }
//
//    private void handleCrashedService() {
//        Log.i(TAG, "handleCrashedService called.");
//        if (isStarted()) {
//            // We probably didn't get a chance to clean up gracefully, so do it now.
//            stopKeepAlives();
//
//            // Formally start and attempt connection.
//            start();
//        }
//    }
//
//    /**
//     * Returns the last known value saved in the database.
//     */
//    private boolean isStarted() {
//        return mStarted;
//    }
//
//    private void setStarted(final boolean started) {
//        Log.i(TAG, "setStarted called with value: " + started);
//        mStarted = started;
//    }
//
//    protected void setLoggedIn(final boolean value) {
//        Log.i(TAG, "setLoggedIn called with value: " + value);
//        mLoggedIn = value;
//    }
//
//    protected boolean isLoggedIn() {
//        return mLoggedIn;
//    }
//
//    public static boolean isConnected() {
//        return mConnection != null;
//    }
//
//    @Override
//    public void onStart(final Intent intent, final int startId) {
//        final Message msg = mServiceHandler.obtainMessage();
//        msg.arg1 = startId;
//        msg.obj = intent;
//        mServiceHandler.sendMessage(msg);
//    }
//
//    @Override
//    public int onStartCommand(final Intent intent, final int flags, final int startId) {
//        Log.i(TAG, "Service started with intent : " + intent);
//
//        onStart(intent, startId);
//
//        return START_NOT_STICKY;
//    }
//
//    private void onHandleIntent(final Intent intent) {
//        if (IntentActions.KEEP_ALIVE_SERVICE_STOP.equals(intent.getAction())) {
//            stop();
//
//            stopSelf();
//        } else if (IntentActions.KEEP_ALIVE_SERVICE_START.equals(intent.getAction())) {
//            start();
//        } else if (IntentActions.KEEP_ALIVE_SERVICE_PING_SERVER.equals(intent.getAction())) {
//            keepAlive(false);
//        }
//    }
//
//    @Override
//    public IBinder onBind(final Intent intent) {
//        return null;
//    }
//
//    private synchronized void start() {
//        if (mStarted) {
//            Log.w(TAG, "Attempt to start connection that is already active");
//            setStarted(true);
//            return;
//        }
//
//        try {
//            registerReceiver(mConnectivityChanged, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
//        } catch (final Exception e) {
//            Log.e(TAG, "Exception occurred while trying to register the receiver.", e);
//        }
//
//        if (mConnection == null) {
//            Log.i(TAG, "Connecting...");
//            mConnection = new ConnectionThread(Config.PLUGIN_BASE_HOST, Config.PLUGIN_BASE_PORT);
//            mConnection.start();
//        }
//    }
//
//    private synchronized void stop() {
//        if (mConnection != null) {
//            mConnection.abort(true);
//            mConnection = null;
//        }
//
//        setStarted(false);
//
//        try {
//            unregisterReceiver(mConnectivityChanged);
//        } catch (final Exception e) {
//            Log.e(TAG, "Exception occurred while trying to unregister the receiver.", e);
//        }
//        cancelReconnect();
//    }
//
//    /**
//     * Sends the keep-alive message if the service is started and we have a
//     * connection with it.
//     */
//    private synchronized void keepAlive(final Boolean forced) {
//        try {
//            if (mStarted && isConnected() && isLoggedIn()) {
//                mConnection.sendKeepAlive(forced);
//            }
//        } catch (final IOException e) {
//            Log.w(TAG, "Error occurred while sending the keep alive message.", e);
//        } catch (final JSONException e) {
//            Log.w(TAG, "JSON error occurred while sending the keep alive message.", e);
//        }
//    }
//
//
//    /**
//     * Uses the {@link android.app.AlarmManager} to start the keep alive service in every {@value #INTERVAL_KEEP_ALIVE} milliseconds.
//     */
//    private void startKeepAlives() {
//        final PendingIntent pi = PendingIntent.getService(this, 0, new Intent(IntentActions.KEEP_ALIVE_SERVICE_PING_SERVER), PendingIntent.FLAG_UPDATE_CURRENT);
//        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + INTERVAL_KEEP_ALIVE, INTERVAL_KEEP_ALIVE, pi);
//    }
//
//    /**
//     * Removes the repeating alarm which was started by the {@link #startKeepAlives()} function.
//     */
//    private void stopKeepAlives() {
//        final PendingIntent pi = PendingIntent.getService(this, 0, new Intent(IntentActions.KEEP_ALIVE_SERVICE_PING_SERVER), PendingIntent.FLAG_UPDATE_CURRENT);
//        mAlarmManager.cancel(pi);
//    }
//
//    public void scheduleReconnect(final long startTime) {
//        long interval = mPrefs.getLong("retryInterval", INTERVAL_INITIAL_RETRY);
//
//        final long now = System.currentTimeMillis();
//        final long elapsed = now - startTime;
//
//        if (elapsed < interval) {
//            interval = Math.min(interval * 4, INTERVAL_MAXIMUM_RETRY);
//        } else {
//            interval = INTERVAL_INITIAL_RETRY;
//        }
//
//        Log.i(TAG, "Rescheduling connection in " + interval + "ms.");
//
//        mPrefs.edit().putLong("retryInterval", interval).apply();
//
//        final PendingIntent pi = PendingIntent.getService(this, 0, new Intent(IntentActions.KEEP_ALIVE_SERVICE_RECONNECT), PendingIntent.FLAG_UPDATE_CURRENT);
//        mAlarmManager.set(AlarmManager.RTC_WAKEUP, now + interval, pi);
//    }
//
//    public void cancelReconnect() {
//        final PendingIntent pi = PendingIntent.getService(this, 0, new Intent(IntentActions.KEEP_ALIVE_SERVICE_RECONNECT), PendingIntent.FLAG_UPDATE_CURRENT);
//        mAlarmManager.cancel(pi);
//    }
//
//    private synchronized void reconnectIfNecessary() {
//        if (mStarted && !isConnected()) {
//            Log.i(TAG, "Reconnecting...");
//
//            mConnection = new ConnectionThread(Config.PLUGIN_BASE_HOST, Config.PLUGIN_BASE_PORT);
//            mConnection.start();
//        }
//    }
//
//    private final BroadcastReceiver mConnectivityChanged = new BroadcastReceiver() {
//        @Override
//        public void onReceive(final Context context, final Intent intent) {
//            final NetworkInfo info = mConnMan.getActiveNetworkInfo(); //  (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
//
//            final boolean hasConnectivity = info != null && info.isConnected();
//
//            Log.i(TAG, "Connecting changed: connected=" + hasConnectivity);
//
//            if (hasConnectivity) {
//                reconnectIfNecessary();
//            } else if (mConnection != null) {
//                mConnection.abort(false);
//                mConnection = null;
//            }
//        }
//    };
//
//    protected class ConnectionThread extends Thread {
//        private final Socket mSocket;
//
//        private final String mHost;
//
//        private final int mPort;
//
//        private volatile boolean mAbort = false;
//
//        public ConnectionThread(final String host, final int port) {
//            mHost = host;
//            mPort = port;
//            mSocket = new Socket();
//        }
//
//        /**
//         * Returns whether we have an active internet connection or not.
//         *
//         * @return <code>true</code> if there is an active internet connection.
//         * <code>false</code> otherwise.
//         */
//        private boolean isNetworkAvailable() {
//            final NetworkInfo info = mConnMan.getActiveNetworkInfo();
//            return info != null && info.isConnected();
//        }
//
//        @Override
//        public void run() {
//            final Socket s = mSocket;
//
//            final long startTime = System.currentTimeMillis();
//
//            try {
//                // Now we can say that the service is started.
//                setStarted(true);
//
//                // Connect to server.
//                s.connect(new InetSocketAddress(mHost, mPort), 20000);
//
//                Log.i(TAG, "Connection established to " + s.getInetAddress() + ":" + mPort);
//
//                // Start keep alive alarm.
//                startKeepAlives();
//
//                final DataOutputStream dos = new DataOutputStream(s.getOutputStream());
//                final BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));
//
//                // Send the login data.
//                final JSONObject login = new JSONObject();
//
//                // Send the login message.
//                dos.write((login.toString() + "\r\n").getBytes());
//
//                // Wait until we receive something from the server.
//                String receivedMessage;
//                while ((receivedMessage = in.readLine()) != null) {
//                    Log.i(TAG, "Received data: " + receivedMessage);
//                    processMessagesFromServer(dos, receivedMessage);
//                }
//
//                if (!mAbort) {
//                    Log.i(TAG, "Server closed connection unexpectedly.");
//                }
//            } catch (final IOException e) {
//                Log.e(TAG, "Unexpected I/O error.", e);
//            } catch (final Exception e) {
//                Log.e(TAG, "Exception occurred.", e);
//            } finally {
//                setLoggedIn(false);
//                stopKeepAlives();
//
//                if (mAbort) {
//                    Log.i(TAG, "Connection aborted, shutting down.");
//                } else {
//                    try {
//                        s.close();
//                    } catch (final IOException e) {
//                        // Do nothing.
//                    }
//
//                    synchronized (QuizClient.this) {
//                        mConnection = null;
//                    }
//
//                    if (isNetworkAvailable()) {
//                        scheduleReconnect(startTime);
//                    }
//                }
//            }
//        }
//
//        /**
//         * Sends the PING word to the server.
//         *
//         * @throws java.io.IOException    if an error occurs while writing to this stream.
//         * @throws org.json.JSONException
//         */
//        public void sendKeepAlive(final Boolean forced) throws IOException, JSONException {
//            final JSONObject ping = new JSONObject();
//
//            final Socket s = mSocket;
//            s.getOutputStream().write((ping.toString() + "\r\n").getBytes());
//        }
//
//        /**
//         * Aborts the connection with the server.
//         */
//        public void abort(boolean manual) {
//            mAbort = manual;
//
//            try {
//                // Close the output stream.
//                mSocket.shutdownOutput();
//            } catch (final IOException e) {
//                // Do nothing.
//            }
//
//            try {
//                // Close the input stream.
//                mSocket.shutdownInput();
//            } catch (final IOException e) {
//                // Do nothing.
//            }
//
//            try {
//                // Close the socket.
//                mSocket.close();
//            } catch (final IOException e) {
//                // Do nothing.
//            }
//
//            while (true) {
//                try {
//                    join();
//                    break;
//                } catch (final InterruptedException e) {
//                    // Do nothing.
//                }
//            }
//        }
//    }
//
//    public void processMessagesFromServer(final DataOutputStream dos, final String receivedMessage) throws IOException {
//    }
//
//}