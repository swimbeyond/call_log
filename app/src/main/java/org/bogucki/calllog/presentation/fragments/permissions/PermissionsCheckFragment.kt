package org.bogucki.calllog.presentation.fragments.permissions

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import org.bogucki.calllog.R
import org.bogucki.calllog.databinding.FragmentPermissionsCheckBinding
import org.bogucki.calllog.server.ServerWorker
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class PermissionsCheckFragment : Fragment() {

    private lateinit var binding: FragmentPermissionsCheckBinding

    private val workManager: WorkManager get() = WorkManager.getInstance(requireContext())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPermissionsCheckBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSettings.setOnClickListener {

            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            val uri = Uri.fromParts("package", requireContext().packageName, null);
            intent.data = uri;
            startActivity(intent);
        }
    }

    override fun onStart() {
        super.onStart()
        startServerAndProceedWithPermissionCheck()
    }

    @NeedsPermission(
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.POST_NOTIFICATIONS
    )
    fun startServerAndProceed() {
        val workRequest = OneTimeWorkRequestBuilder<ServerWorker>().build()
        workManager.enqueue(workRequest)
        findNavController().navigate(R.id.start_main_fragment)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }
}