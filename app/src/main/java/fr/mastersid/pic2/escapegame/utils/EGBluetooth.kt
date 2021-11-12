package fr.mastersid.pic2.escapegame.utils

import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


// Defines several constants used when transmitting messages between the
// service and the UI.
const val MESSAGE_READ: Int = 0
const val MESSAGE_WRITE: Int = 1
const val MESSAGE_TOAST: Int = 2
const val MESSAGE_REQ_MAC: Int = 3
const val MESSAGE_REQ_ITEMS: Int = 4
// ... (Add other message types here as needed.)

@Singleton
class EGBluetooth @Inject constructor(
    @ApplicationContext appContext: Context
) {
    val MY_UUID: UUID = UUID.fromString("8989063a-c9af-463a-b3f1-f21d9b2b827b")
    val TAG = "EG_BT"

    private lateinit var manage: ConnectedThread

    private val _bluetoothAdapter: BluetoothAdapter? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            appContext.getSystemService(BluetoothManager::class.java).adapter
        } else {
            @Suppress("DEPRECATION")
            BluetoothAdapter.getDefaultAdapter()
        }
    val bluetoothAdapter get() = _bluetoothAdapter

    private val _message = MutableLiveData("")
    val message get() = _message

    init { // Vérification des capacités bluetooth TODO: wait for the intent to succeed before running Thread
        // Check if BT is supported and enabled
        Log.d(TAG, "Bluetooth supported : " + (_bluetoothAdapter != null).toString())
        if (bluetoothAdapter?.isEnabled == false) {
            Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        }
        Log.d(
            TAG,
            "Bluetooth enabled : ${_bluetoothAdapter?.isEnabled}, MAC : ${getBluetoothMacAddress()}"
        )
        GlobalScope.launch {
            AcceptThread().run()
        }
    }

    var handler = Handler(Looper.getMainLooper()) { msg ->
        when (msg.what) {
            // Appelé quand on reçoit une donnée
            MESSAGE_READ -> {
                val readBuff = msg.obj as ByteArray
                val tempMsg = String(readBuff, 0, msg.arg1)
                _message.value = tempMsg
            }
            MESSAGE_REQ_ITEMS -> {

            }
        }
        true
    }


    fun getBluetoothMacAddress(): String {
        //TODO (send request to connected device. For now, MAC hardcoded)
        val id = Build.ID
        Log.d(TAG, id)
        return when (id.toString()) {
            "RP1A.200720.012" -> "E0:D0:83:DC:82:F0"
            "QKQ1.191215.002" -> "dc:b7:2e:6d:5d:0b"
            "RKQ1.200826.002" -> "98:f6:21:cb:fa:f1"
            else -> "02:00:00:00:00:00"
        }
    }


    fun writeTo(mac: String, message: String) {
        val device = _bluetoothAdapter?.getRemoteDevice(mac)

        if (device != null) {
            Log.d(TAG, "Device created : ${device.name}")
            GlobalScope.launch {
                ConnectThread(device).run()
                Log.d(TAG, "Connect thread created")
                try {
                    Log.d(TAG, "Sending message...")
                    manage.write(message.toByteArray())
                    Log.d(TAG, "Message sent")
                } catch (e: Exception) {
                    Log.e(TAG, "Can't send message : $e")
                }
            }
        } else {
            Log.e(TAG, "Device with MAC $mac not found")
        }
    }

    private fun manageConnectedSocket(socket: BluetoothSocket) {
        GlobalScope.launch {
            manage = ConnectedThread(socket)
            manage.start()
        }
    }

    inner class AcceptThread : Thread() { // Server
        private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord("EG_Bluetooth", MY_UUID)
        }

        override fun run() {
            // Keep listening until exception occurs or a socket is returned.
            var shouldLoop = true
            while (shouldLoop) {
                val socket: BluetoothSocket? = try {
                    mmServerSocket?.accept()
                } catch (e: IOException) {
                    Log.e(TAG, "Socket's accept() method failed", e)
                    shouldLoop = false
                    null
                }
                socket?.also {
                    manageConnectedSocket(it)

                    mmServerSocket?.close()
                    shouldLoop = false
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        fun cancel() {
            try {
                mmServerSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the connect socket", e)
            }
        }
    }


    inner class ConnectThread(private val device: BluetoothDevice) : Thread() { // Client

        private val btSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(MY_UUID)
        }

        override fun run() {
            Log.d(TAG, "Start connect thread run to ${device.address}")
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter?.cancelDiscovery()

            btSocket?.let {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                Log.d(TAG, "Connecting bluetooth socket...")
                try {
                    it.connect()
                    Log.d(TAG, "Bluetooth socket connected")
                } catch (e: Exception) {
                    Log.e(TAG, "No connection available : $e")
                    return
                }

                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.
                manageConnectedSocket(it)
            }
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                btSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the client socket", e)
            }
        }
    }


    inner class ConnectedThread(private val btSocket: BluetoothSocket) : Thread() {
        private val btInStream: InputStream = btSocket.inputStream
        private val btOutStream: OutputStream = btSocket.outputStream
        private val btBuffer = ByteArray(1024) // mmBuffer store for the stream


        override fun run() {
            var numBytes: Int // nombre d'octets à lire dans le ByteArray

            while (true) {
                numBytes = try { // on récupère le nombre d'octets à lire
                    btInStream.read(btBuffer) // on lit le buffer bluetooth
                } catch (e: IOException) {
                    Log.d(TAG, "InputStream déconnecté")
                    break
                }

                val readMsg = handler.obtainMessage(
                    MESSAGE_READ, numBytes, -1, btBuffer
                )

                readMsg.sendToTarget()
            }
        }

        // Call this from the main activity to send data to the remote device.
        fun write(bytes: ByteArray) {
            try {
                btOutStream.write(bytes)
            } catch (e: IOException) {
                Log.e("hello", "Error occurred when sending data", e)

                // Send a failure message back to the activity.
                val writeErrorMsg = handler.obtainMessage(MESSAGE_TOAST)
                val bundle = Bundle().apply {
                    putString("toast", "Couldn't send data to the other device")
                }

                writeErrorMsg.data = bundle
                handler.sendMessage(writeErrorMsg)
                return
            }
            //Share the sent message with the UI activity.
            val writtenMsg = handler.obtainMessage(
                MESSAGE_WRITE, -1, -1, btBuffer
            )
            writtenMsg.sendToTarget()
        }

        // Call this method from the main activity to shut down the connection.
        fun cancel() {
            try {
                btSocket.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the connect socket", e)
            }
        }
    }
}
