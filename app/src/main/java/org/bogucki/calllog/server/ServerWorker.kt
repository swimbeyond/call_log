package org.bogucki.calllog.server

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.google.gson.Gson
import io.ktor.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.sync.Mutex
import org.bogucki.calllog.R
import org.bogucki.calllog.domain.usecases.GetCallLogUseCase
import org.bogucki.calllog.domain.usecases.GetCallStatusWithCounterIncrementUseCase
import org.bogucki.calllog.domain.usecases.GetPortNumberUseCase
import org.bogucki.calllog.domain.usecases.GetServerAddressUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Instant
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class ServerWorker(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams), KoinComponent {

    private val channelId = "CALL_LOG_SERVER_CHANNEL"

    private val gson: Gson by inject()
    private val getCallStatus: GetCallStatusWithCounterIncrementUseCase by inject()
    private val getCallLog: GetCallLogUseCase by inject()
    private val getServerAddress: GetServerAddressUseCase by inject()
    private val getPortNumber: GetPortNumberUseCase by inject()

    private var startTime = Instant.now()

    private val endpoints =
        mapOf(
            "" to { getIndex() },
            "status" to { getCallStatus() },
            "log" to { getCallLog().value }
        )

    private val server = embeddedServer(Netty, getPortNumber()) {
        routing {
            endpoints.forEach { endpoint ->
                get("/${endpoint.key}") {
                    call.respond(gson.toJson(endpoint.value.invoke()))
                }
            }
        }
    }

    private fun getIndex(): IndexResponse =
        IndexResponse(
            startTime = startTime,
            services = endpoints.filter { it.key.isNotEmpty() }
                .map {
                    ServiceInfo(
                        it.key,
                        "${getServerAddress()}/${it.key}"
                    )
                })

    private val mutex = Mutex(true)

    override suspend fun doWork(): Result {
        setForeground(getForegroundInfo())
        startTime = Instant.now()
        try {
            server.start(true)
            mutex.lock()
        } finally {
            server.stop(1, 3, TimeUnit.SECONDS)
            mutex.unlock()
            return Result.success()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val notification = NotificationCompat.Builder(context, channelId)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
            notification.setChannelId(channelId)
        }

        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(id)

        notification.setContentTitle(context.getString(R.string.server_notification_title))
            .setTicker(context.getString(R.string.server_notification_title))
            .setContentText(
                context.getString(
                    R.string.server_notification_message,
                    getServerAddress()
                )
            )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setSilent(true)
            .addAction(
                android.R.drawable.ic_delete,
                context.getString(R.string.server_notification_cancel_button_text),
                intent
            )
            .setOngoing(true)

        val notificationId = Random.nextInt()
        return ForegroundInfo(notificationId, notification.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val name = "Call log service"
        val descriptionText = ""
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(channelId, name, importance)
        mChannel.description = descriptionText
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

    data class ServiceInfo(val name: String, val url: String)
    data class IndexResponse(val startTime: Instant, val services: List<ServiceInfo>)
}