package fr.mastersid.pic2.escapegame.utils

import android.bluetooth.*
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
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
// ... (Add other message types here as needed.)

@Singleton
class EGBluetooth @Inject constructor(
    @ApplicationContext appContext: Context
) {
    private lateinit var manage: DataThread
    private lateinit var serverManage: DataThread

    private val _bluetoothAdapter: BluetoothAdapter? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            appContext.getSystemService(BluetoothManager::class.java).adapter
        } else {
            @Suppress("DEPRECATION")
            BluetoothAdapter.getDefaultAdapter()
        }

    private val _message = MutableStateFlow("")
    val message get() = _message

    init {
        // Check if BT is supported and enabled
        Log.d(TAG, "Bluetooth supported : " + (_bluetoothAdapter != null).toString())
        if (_bluetoothAdapter != null){
            if (!_bluetoothAdapter.isEnabled) {
                Log.d(TAG, "Bluetooth disabled, enabling...")
                _bluetoothAdapter.enable()
            }
            Log.d(TAG, "Bluetooth enabled : ${_bluetoothAdapter.isEnabled}")
            Log.d(TAG, "MAC : ${getBluetoothMacAddress()}")

            GlobalScope.launch {
                ServerThread().run()
            }
        }
    }

    var handler = Handler(Looper.getMainLooper()) { msg ->
        when (msg.what) {
            MESSAGE_READ -> {
                val readBuff = msg.obj as ByteArray
                _message.value = String(readBuff, 0, msg.arg1)
            }
        }
        true
    }

    fun writeTo(mac: String, message: String) {
        val device = _bluetoothAdapter?.getRemoteDevice(mac)

        if (device != null) {
            Log.d(TAG, "Device created : ${device.name}")
            GlobalScope.launch {
                ClientThread(device).run()
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

    fun respond(message:String) {
        Log.d(TAG, "responding $message")
        GlobalScope.launch {
            serverManage.write(message.toByteArray())
            serverManage.cancel()
            ServerThread().run()
        }
    }

    fun getBluetoothMacAddress(): String {
        //TODO (send request to connected device. For now, MAC hardcoded)
        val id = Build.ID
        Log.d(TAG, id)
        return when (id.toString()) {
            "RP1A.200720.012" -> "E0:D0:83:DC:82:F0" //TINA
            "PQ3B.190801.002" -> "18:F0:E4:E3:20:B8" //MI A1
            "QKQ1.191215.002" -> "DC:B7:2E:6D:5D:0B" //AMEL
            "QKQ1.191002.002" -> "F4:60:E2:FF:EA:F3" //MI A2 Lite
            "RKQ1.200826.002" -> "98:F6:21:CB:FA:F1" //Mateo
            else -> "02:00:00:00:00:00"
        }
    }

    inner class ServerThread : Thread() {
        private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            _bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord("EG_Bluetooth",
                Companion.MY_UUID
            )
        }

        override fun run() {
            // Keep listening until exception occurs or a socket is returned.
            var shouldLoop = true
            while (shouldLoop) {
                val socket: BluetoothSocket? = try {
                    mmServerSocket?.accept()
                } catch (e: IOException) {
                    Log.e(Companion.TAG, "Socket's accept() method failed", e)
                    shouldLoop = false
                    null
                }
                socket?.also {
                    GlobalScope.launch {
                        serverManage = DataThread(socket)
                        serverManage.start()
                    }
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
                Log.e(Companion.TAG, "Could not close the connect socket", e)
            }
        }
    }


    inner class ClientThread(private val device: BluetoothDevice) : Thread() {

        private val btSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(Companion.MY_UUID)
        }

        override fun run() {
            Log.d(Companion.TAG, "Start connect thread run to ${device.address}")
            // Cancel discovery because it otherwise slows down the connection.
            _bluetoothAdapter?.cancelDiscovery()

            btSocket?.let {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                Log.d(Companion.TAG, "Connecting bluetooth socket...")
                try {
                    it.connect()
                    Log.d(Companion.TAG, "Bluetooth socket connected")
                } catch (e: Exception) {
                    Log.e(Companion.TAG, "No connection available : $e")
                    return
                }

                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.
                manage = DataThread(it)
                manage.start()
            }
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                btSocket?.close()
            } catch (e: IOException) {
                Log.e(Companion.TAG, "Could not close the client socket", e)
            }
        }
    }


    inner class DataThread(private val btSocket: BluetoothSocket) : Thread() {
        private val btInStream: InputStream = btSocket.inputStream
        private val btOutStream: OutputStream = btSocket.outputStream
        private val btBuffer = ByteArray(1024) // mmBuffer store for the stream


        override fun run() {
            var numBytes: Int // nombre d'octets à lire dans le ByteArray

            while (true) {
                numBytes = try { // on récupère le nombre d'octets à lire
                    btInStream.read(btBuffer) // on lit le buffer bluetooth
                } catch (e: IOException) {
                    Log.d(Companion.TAG, "InputStream disconnected")
                    break
                }

                val readMsg = handler.obtainMessage(
                    MESSAGE_READ, numBytes, -1, btBuffer
                )

                readMsg.sendToTarget()
            }
        }


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
                Log.e(Companion.TAG, "Could not close the connect socket", e)
            }
        }
    }

    companion object {
        val MY_UUID: UUID = UUID.fromString("8989063a-c9af-463a-b3f1-f21d9b2b827b")
        const val TAG = "EG_BT"
    }
}
