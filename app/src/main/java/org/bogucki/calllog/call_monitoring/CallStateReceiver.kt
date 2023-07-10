package org.bogucki.calllog.call_monitoring

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.EXTRA_STATE_IDLE
import org.bogucki.calllog.domain.usecases.SetCallStatusUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CallStateReceiver : BroadcastReceiver(), KoinComponent {

    val setCallStatus: SetCallStatusUseCase by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)
        val phoneNumber = intent?.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

        //The broadcast is received twice. Once with number, other with null number
        //We're ignoring the one with null
        phoneNumber?.let { number ->
            setCallStatus(state != EXTRA_STATE_IDLE, number)
        }
    }
}